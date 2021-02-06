package com.whitedeath.organizationchart.Repository;

import com.whitedeath.organizationchart.Model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface DesignationRepo extends JpaRepository<Designation, Integer> {

    Designation getDesignationByName(String name);

    Designation getFirstByLevelIdGreaterThanOrderByLevelId(Integer levelId);

    @Modifying
    @Query("update Designation designation set designation.levelId=designation.levelId+1 where designation.levelId>?1")
    void setAllLowerDesignation(Integer levelId);
}
