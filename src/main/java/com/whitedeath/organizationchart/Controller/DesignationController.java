package com.whitedeath.organizationchart.Controller;

import com.whitedeath.organizationchart.Model.Designation;
import com.whitedeath.organizationchart.Model.PostDesignation;
import com.whitedeath.organizationchart.service.DesignationService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
public class DesignationController {
    private DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @GetMapping("/designations")
    @ResponseBody
    public ResponseEntity<List<Designation>> showDesignations() {
        return designationService.getAllDesignations();
    }


    @GetMapping("/designations/{jobTitle}")
    @ResponseBody
    public ResponseEntity<Object> showDetails(@PathVariable("jobTitle") String jobTitle) {
        return designationService.getSingleDesignations(jobTitle);
    }

    @Transactional
    @PostMapping("/designations")
    public ResponseEntity<Object> addDesignation(@RequestBody @Valid PostDesignation d) {
        return designationService.insertDesignations(d);
    }


    @Transactional
    @DeleteMapping("designations/{jobTitle}")
    public ResponseEntity<Object> deleteDesignation(@PathVariable("jobTitle") String jobTitle) {
        return designationService.deleteDesignation(jobTitle);
    }


}
