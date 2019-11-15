package com.whitedeath.organizationchart.Controller;

import com.whitedeath.organizationchart.Model.Designation;
import com.whitedeath.organizationchart.Repository.DesignationRepo;
import com.whitedeath.organizationchart.service.DesignationService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
public class DesignationController {
    private DesignationService designationService;

    public DesignationController(DesignationRepo designationRepo, DesignationService designationService) {
        this.designationService = designationService;
    }

    @GetMapping("/designations")
    @ResponseBody
    public ResponseEntity<List<Designation>> showDesignations() {
        //ResponseEntity<List<Designation>> responseEntity=
        return designationService.getAllDesignations();
        //return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }


    @GetMapping("/designations/{designation_id}")
    @ResponseBody
    public ResponseEntity<Object> showDetails(@PathVariable("designation_id") Integer designation_id) {
        //ResponseEntity<Object> responseEntity=
        return designationService.getSingleDesignations(designation_id);
        //return new ResponseEntity<>(responseEntity.getBody(),responseEntity.getStatusCode());
        //Designation designation = designationRepo.findById(designation_id).orElse(null);
        //return new ResponseEntity<>(designation, HttpStatus.OK);
    }


    @PostMapping("/designations")
    public ResponseEntity<Object> addDesignation(@RequestBody @Valid Designation d) {
        //ResponseEntity<Object> responseEntity=
        return designationService.insertDesignations(d);
        //return new ResponseEntity<>(responseEntity.getBody(),responseEntity.getStatusCode());
    }


    @Transactional
    @DeleteMapping("designations/{designation_id}")
    public ResponseEntity<Object> deleteDesignation(@PathVariable("designation_id") Integer designation_id) {
        //ResponseEntity<Object> responseEntity=
        return designationService.deleteDesignation(designation_id);
        //return new ResponseEntity<>(responseEntity.getBody(),responseEntity.getStatusCode());
    }


}
