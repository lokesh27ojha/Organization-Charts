package com.whitedeath.organizationchart.Repository;

import com.whitedeath.organizationchart.Model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepo extends JpaRepository<Designation, Integer> {

    Designation getDesignationByName(String name);
}
