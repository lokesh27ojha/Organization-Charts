package com.whitedeath.organizationchart.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Designation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer designationId;

    private Integer levelId;

    @NotNull(message = "Name is mandatory")
    @Pattern(regexp = "[a-zA-Z\\s]{3,}", message = "designation's pattern is not matched")
    private String name;

    public Designation() {
    }

    public Designation(Integer levelId, @Pattern(regexp = "[a-zA-Z\\s]{3,}", message = "designation's pattern is not matched") String name) {
        this.levelId = levelId;
        this.name = name;
    }

    public Integer getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Integer designationId) {
        this.designationId = designationId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Designation [designationId=" + designationId + ", levelId=" + levelId + ", name=" + name + "]";
    }

}