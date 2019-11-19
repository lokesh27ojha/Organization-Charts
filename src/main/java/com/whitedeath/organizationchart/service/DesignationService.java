package com.whitedeath.organizationchart.service;

import com.whitedeath.organizationchart.Model.Designation;
import com.whitedeath.organizationchart.Repository.DesignationRepo;
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

    private final static Logger log = LoggerFactory.getLogger(DesignationService.class);

    public DesignationService(DesignationRepo drepo) {
        designationComparator = (d1, d2) -> {
            if (d1.getLevelId().equals(d2.getLevelId()))
                return d1.getName().compareTo(d2.getName());
            else
                return d1.getLevelId() < d2.getLevelId() ? -1 : 1;
        };
        this.designationRepo = drepo;
    }


    public ResponseEntity<List<Designation>> getAllDesignations() {
        List<Designation> designation = designationRepo.findAll();
        designation.sort(designationComparator);
        log.info("List of Designations is fetched");
        return new ResponseEntity<>(designation, HttpStatus.OK);
    }


    public ResponseEntity<Object> getSingleDesignations(Integer designation_id) {
        if (designation_id < 0)
            return new ResponseEntity<>("Not Valid Designation ID: " + designation_id, HttpStatus.BAD_REQUEST);
        if (!designationRepo.existsById(designation_id))
            return new ResponseEntity<>("Not Valid Designation ID: " + designation_id, HttpStatus.NOT_FOUND);

        Designation d = designationRepo.findById(designation_id).orElse(null);
        return new ResponseEntity<>(d, HttpStatus.OK);
    }


    public ResponseEntity<Object> insertDesignations(Designation d) {
        if (d.getLevelId() < 0)
            return new ResponseEntity<>("Can Not Insert Record, Level Id is less than 0", HttpStatus.BAD_REQUEST);
        if (d.getLevelId() == null)
            return new ResponseEntity<>("Can Not Insert Record, Level Id is Null", HttpStatus.BAD_REQUEST);
        if (d.getName().isEmpty() || d.getName() == null)
            return new ResponseEntity<>("Can Not Insert Record, Name is Empty", HttpStatus.BAD_REQUEST);
        if (d.getDesignationId() != null)
            return new ResponseEntity<>("Can Not Insert Record, Don't Provide designationId", HttpStatus.BAD_REQUEST);
        designationRepo.save(d);
        return new ResponseEntity<>(d, HttpStatus.CREATED);
    }


    public ResponseEntity<Object> deleteDesignation(int designation_id) {
        if (designation_id < 0)
            return new ResponseEntity<>("No Record Exist for ID : " + designation_id, HttpStatus.BAD_REQUEST);
        if (!designationRepo.existsById(designation_id))
            return new ResponseEntity<>("No Record Exist for ID: " + designation_id, HttpStatus.NOT_FOUND);
        Designation d = designationRepo.getOne(designation_id);
        designationRepo.delete(d);
        return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
    }

}
