package com.whitedeath.organizationchart.Model;

import javax.validation.constraints.NotNull;

public class PutEmployee extends Employee {

    @NotNull
    private Boolean replace;

    public PutEmployee() {
    }

    public PutEmployee(String name, String jobTitle, Integer managerId, Boolean replace) {
        super(name, jobTitle, managerId);
        this.replace = replace;
    }

    public PutEmployee(Boolean replace) {
        this.replace = replace;
    }

    public Boolean getReplace() {
        return replace;
    }

    public void setReplace(Boolean replace) {
        this.replace = replace;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", jobTitle='" + super.getJobTitle() + '\'' +
                ", managerId=" + super.getManagerId() +
                ", replace=" + replace +
                '}';
    }

}
