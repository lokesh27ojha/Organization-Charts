package com.whitedeath.organizationchart.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.whitedeath.organizationchart.Model.Employee;
import com.whitedeath.organizationchart.Model.PutEmployee;
import com.whitedeath.organizationchart.View.Views;
import com.whitedeath.organizationchart.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //for fetching all employee details
    @GetMapping("/employees")
    @ResponseBody
    public ResponseEntity<Object> showDetails() {
        return employeeService.getAllEmployees();
    }

    //for fetching a single employee detail
    @JsonView(Views.External.class)
    @GetMapping("/employees/{id}")
    public ResponseEntity<Object> showDetails(@PathVariable("id") int id) {
        return employeeService.getSingleEmployee(id);
    }


    //for inserting the record of employee
    @PostMapping("/employees")
    public ResponseEntity<Object> addDetails(@RequestBody @Valid Employee e) {
        return employeeService.insertEmployee(e);
    }


    //for updating the data
    @Transactional
    @PutMapping("employees/{employeeId}")
    public ResponseEntity<Object> putEmployee(@RequestBody @Valid PutEmployee e, @PathVariable Integer employeeId) {
        return employeeService.updateEmployee(e, employeeId);
    }


    //for deleting the record of employee
    @Transactional
    @DeleteMapping("employees/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable("id") Integer id) {
        return employeeService.deleteEmployee(id);
    }


}
