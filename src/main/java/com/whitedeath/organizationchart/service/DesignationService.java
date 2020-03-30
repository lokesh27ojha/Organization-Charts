package com.whitedeath.organizationchart.service;

import com.whitedeath.organizationchart.Model.Designation;
import com.whitedeath.organizationchart.Model.PostDesignation;
import com.whitedeath.organizationchart.Repository.DesignationRepo;
import com.whitedeath.organizationchart.Repository.EmployeeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DesignationService {
    private Comparator<Designation> designationComparator;
    private DesignationRepo designationRepo;
    private EmployeeRepo employeeRepo;

    private final static Logger log = LoggerFactory.getLogger(DesignationService.class);

    public DesignationService(DesignationRepo drepo, EmployeeRepo erepo) {
        designationComparator = (d1, d2) -> {
            if (d1.getLevelId().equals(d2.getLevelId()))
                return d1.getName().compareTo(d2.getName());
            else
                return d1.getLevelId() < d2.getLevelId() ? -1 : 1;
        };
        this.designationRepo = drepo;
        this.employeeRepo = erepo;
    }


    public ResponseEntity<List<Designation>> getAllDesignations() {
        List<Designation> designation = designationRepo.findAll();
        designation.sort(designationComparator);
        log.info("List of Designations is fetched");
        return new ResponseEntity<>(designation, HttpStatus.OK);
    }


    public ResponseEntity<Object> getSingleDesignations(String jobTitle) {
        if (designationRepo.getDesignationByName(jobTitle) == null)
            return new ResponseEntity<>("No Record Exist for Designation: " + jobTitle, HttpStatus.NOT_FOUND);
        Designation d = designationRepo.getDesignationByName(jobTitle);
        return new ResponseEntity<>(d, HttpStatus.OK);
    }


    public ResponseEntity<Object> insertDesignations(PostDesignation d) {
        if (d.getDesignationId() != null || d.getLevelId() != null)
            return new ResponseEntity<>("Dont Provide LevelId od DesignationId", HttpStatus.BAD_REQUEST);
        if (designationRepo.getDesignationByName(d.getName()) != null)
            return new ResponseEntity<>("Designation Already Exist : " + d.getName(), HttpStatus.BAD_REQUEST);
        if (designationRepo.getDesignationByName(d.getSuperiorDesignation()) == null)
            return new ResponseEntity<>("Superior Designation Does not Exist : " + d.getSuperiorDesignation(), HttpStatus.BAD_REQUEST);
        Designation designation = new Designation();
        if (d.getName().equals("Director")) {
            designation.setLevelId(1);
            designation.setName(d.getName());
            designationRepo.save(designation);
        }
        designation.setName(d.getName());
        if (d.getColleague()) {
            Designation superior = designationRepo.getDesignationByName(d.getSuperiorDesignation());
            Designation junior = designationRepo.getFirstByLevelIdGreaterThanOrderByLevelId(superior.getLevelId());
            designation.setLevelId(junior.getLevelId());
            designationRepo.save(designation);
        } else {
            Designation superior = designationRepo.getDesignationByName(d.getSuperiorDesignation());
            designationRepo.setAllLowerDesignation(superior.getLevelId());
            designation.setLevelId(superior.getLevelId() + 1);
            designationRepo.save(designation);
        }
        return new ResponseEntity<>(designation, HttpStatus.CREATED);
    }


    public ResponseEntity<Object> deleteDesignation(String jobTitle) {
        if (designationRepo.getDesignationByName(jobTitle) == null)
            return new ResponseEntity<>("No Record Exist for Designation: " + jobTitle, HttpStatus.NOT_FOUND);
        if (employeeRepo.getEmployeeByJobTitle(jobTitle) != null)
            return new ResponseEntity<>("Can not Delete this Designation, some Employee having this post: " + jobTitle, HttpStatus.BAD_REQUEST);
        Designation d = designationRepo.getDesignationByName(jobTitle);
        designationRepo.delete(d);
        return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
    }

}
