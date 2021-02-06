package com.whitedeath.organizationchart.Model;


import com.fasterxml.jackson.annotation.JsonView;
import com.whitedeath.organizationchart.View.Views;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Employee implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.External.class)
    private Integer id;

    @Pattern(regexp = "[a-zA-Z\\s]{3,}", message = "name's pattern is not matched")
    @NotNull(message = "Name is mandatory")
    @JsonView(Views.External.class)
    private String name;

    @NotNull(message = "JobTitle is mandatory")
    @JsonView(Views.External.class)
    private String jobTitle;


    @JsonView(Views.Internal.class)
    private Integer managerId;

    public Employee() {
    }

    public Employee(String name, String jobTitle, Integer managerId) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.managerId = managerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + id +
                ", name='" + name + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", managerId=" + managerId +
                '}';
    }
}
