package com.whitedeath.organizationchart.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.whitedeath.organizationchart.Model.Designation;
import com.whitedeath.organizationchart.Model.Employee;
import com.whitedeath.organizationchart.Model.PutEmployee;
import com.whitedeath.organizationchart.Repository.DesignationRepo;
import com.whitedeath.organizationchart.Repository.EmployeeRepo;
import com.whitedeath.organizationchart.View.Views;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class EmployeeController {

    private final Comparator<Employee> employeeComparator;
    private final EmployeeRepo employeeRepo;
    private final DesignationRepo designationRepo;

    public EmployeeController(EmployeeRepo repo, DesignationRepo drepo) {
        super();
        employeeComparator = (o1, o2) -> {
            Designation d1 = drepo.getDesignationByName(o1.getJobTitle());
            Designation d2 = drepo.getDesignationByName(o2.getJobTitle());
            if (d1.getLevelId().equals(d2.getLevelId()))
                return o1.getName().compareTo(o2.getName());
            else
                return d1.getLevelId() < d2.getLevelId() ? -1 : 1;
        };
        this.employeeRepo = repo;
        this.designationRepo = drepo;
    }

    //for fetching all employee details
    @GetMapping("/employees")
    @ResponseBody
    public List<Employee> showDetails() {
        List<Employee> employees = employeeRepo.findAll();
        employees.sort(employeeComparator);
        return employees;
    }


    //for fetching a single employee detail
    @JsonView(Views.External.class)
    @GetMapping("/employees/{id}")
    public ResponseEntity<Object> showDetails(@PathVariable("id") int id) {
        if (id < 0)
            return new ResponseEntity<>("Not Valid Employee ID", HttpStatus.BAD_REQUEST);
        if (!employeeRepo.existsById(id))
            return new ResponseEntity<>("Not Valid Employee ID", HttpStatus.NOT_FOUND);
        Employee employee = employeeRepo.getOne(id);
        List<Employee> colleagues = employeeRepo.getEmployeesByManagerIdAndIdNotLike(employee.getManagerId(), id);
        List<Employee> reportingToo = employeeRepo.getEmployeesByManagerId(employee.getId());
        Employee manager = employeeRepo.findById(employee.getManagerId()).orElse(null);

        colleagues.sort(employeeComparator);
        reportingToo.sort(employeeComparator);

        Map<String, Object> map = new HashMap<>();
        map.put("employee", employee);
        if (manager != null)
            map.put("manager", manager);
        if (colleagues.size() != 0)
            map.put("colleagues", colleagues);
        if (reportingToo.size() != 0)
            map.put("subordinates", reportingToo);
        return new ResponseEntity<>(map, HttpStatus.OK);

    }


    //for inserting the record of employee
    @PostMapping("/employees")
    public ResponseEntity<Object> addDetails(@RequestBody @Valid Employee e) {
        if (e.getManagerId() == null)
            return new ResponseEntity<>("Can Not Insert Record, ManagerId is Null", HttpStatus.BAD_REQUEST);
        Designation designation = designationRepo.getDesignationByName(e.getJobTitle());
        Employee manager = employeeRepo.findById(e.getManagerId()).orElse(null);
        //if(!Pattern.matches("[a-zA-z]{3,}",e.getName()))
        //return new ResponseEntity<>("Can Not Insert Record, Wrong name", HttpStatus.BAD_REQUEST);

        if (e.getName().isEmpty() || e.getJobTitle().isEmpty())
            return new ResponseEntity<>("Can Not Insert Record, Name or Job Title is Empty", HttpStatus.BAD_REQUEST);
        if (e.getId() != null)
            return new ResponseEntity<>("Can Not Insert Record, Don't Provide employeeId", HttpStatus.BAD_REQUEST);
        if (designation == null)
            return new ResponseEntity<>("Can Not Insert Record, Not a Valid Job Title", HttpStatus.BAD_REQUEST);

        if (manager == null && e.getManagerId() != -1)
            return new ResponseEntity<>("Can Not Insert Record, Not a Valid Manager", HttpStatus.BAD_REQUEST);
        else if (e.getManagerId() != -1 && designation.getLevelId() == 1 || e.getManagerId() == -1 && designation.getLevelId() != 1)
            return new ResponseEntity<>("Can Not Insert Record, Director Can't Have any Manager / Invalid Designation", HttpStatus.BAD_REQUEST);

        if (manager != null && designationRepo.getDesignationByName(manager.getJobTitle()).getLevelId() >= designation.getLevelId())
            return new ResponseEntity<>("can not insert record, level missmatch", HttpStatus.BAD_REQUEST);

        employeeRepo.save(e);
        return new ResponseEntity<>(e, HttpStatus.CREATED);

    }


    //for updating the data
    @Transactional
    @PutMapping("employees/{employeeId}")
    public ResponseEntity<Object> putEmployee(@RequestBody @Valid PutEmployee e, @PathVariable Integer employeeId) {
        if (!employeeRepo.existsById(employeeId))
            return new ResponseEntity<>("No Employee exist for such ID", HttpStatus.BAD_REQUEST);

        //if(!Pattern.matches("[a-zA-z]{3,}",e.getName()))
        //return new ResponseEntity<>("Can Not Insert Record, Wrong name", HttpStatus.BAD_REQUEST);
        Designation designation = designationRepo.getDesignationByName(e.getJobTitle());
        if (designation == null)
            return new ResponseEntity<>("not a valid job title", HttpStatus.BAD_REQUEST);
        if (e.getManagerId() == null)
            e.setManagerId(employeeRepo.getOne(employeeId).getManagerId());
        Employee manager = employeeRepo.findById(e.getManagerId()).orElse(null);
        if (manager == null && e.getManagerId() != -1)
            return new ResponseEntity<>("not a valid manager", HttpStatus.BAD_REQUEST);
        else if (e.getManagerId() != -1 && designation.getLevelId() == 1 || e.getManagerId() == -1 && designation.getLevelId() != 1)
            return new ResponseEntity<>("Director cant have any manager/invalid designation", HttpStatus.BAD_REQUEST);

        if (manager != null && designationRepo.getDesignationByName(manager.getJobTitle()).getLevelId() >= designation.getLevelId())
            return new ResponseEntity<>("level miss match with manager", HttpStatus.BAD_REQUEST);

        List<Employee> reportingtoo = employeeRepo.getEmployeesByManagerId(employeeId);
        for (Employee emp : reportingtoo) {
            if (designationRepo.getDesignationByName(emp.getJobTitle()).getLevelId() <= designation.getLevelId())
                return new ResponseEntity<>("level miss match with subordinates", HttpStatus.BAD_REQUEST);
        }


        Employee emp = new Employee();
        emp.setName(e.getName());
        emp.setJobTitle(e.getJobTitle());
        emp.setManagerId(e.getManagerId());

        if (e.getReplace()) {
            Employee employeeReturn = employeeRepo.save(emp);
            employeeRepo.updateEmployee(employeeReturn.getId(), employeeId);
        } else {
            emp.setId(employeeId);
            employeeRepo.save(emp);
        }
        return new ResponseEntity<>(emp, HttpStatus.CREATED);
    }


    //for deleting the record of employee
    @Transactional
    @DeleteMapping("employees/{id}")
    public ResponseEntity<Object> deletePlayer(@PathVariable("id") Integer id) {
        if (id < 0)
            return new ResponseEntity<>("No Record Exist for such ID", HttpStatus.BAD_REQUEST);
        if (!employeeRepo.existsById(id))
            return new ResponseEntity<>("No Record Exist for such ID", HttpStatus.NOT_FOUND);
        Employee e = employeeRepo.getOne(id);

        if (e.getManagerId() == -1 && employeeRepo.getEmployeesByManagerId(id).size() != 0)
            return new ResponseEntity<>("Can Not Delete Employee as He is Director Having Subordinates", HttpStatus.BAD_REQUEST);

        employeeRepo.updateEmployee(e.getManagerId(), e.getId());
        employeeRepo.delete(e);
        return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);

    }


}
