package com.whitedeath.organizationchart.Model;


public class PostDesignation extends Designation {
    private String superiorDesignation;
    private Boolean colleague;

    public PostDesignation(Integer levelId, String name, String superiorDesignation, Boolean colleague) {
        super(levelId, name);
        this.superiorDesignation = superiorDesignation;
        this.colleague = colleague;
    }

    public String getSuperiorDesignation() {
        return superiorDesignation;
    }

    public void setSuperiorDesignation(String superiorDesignation) {
        this.superiorDesignation = superiorDesignation;
    }

    public Boolean getColleague() {
        return colleague;
    }

    public void setColleague(Boolean colleague) {
        this.colleague = colleague;
    }

    @Override
    public String toString() {
        return "PostDesignation{" +
                "designationId=" + super.getDesignationId() +
                "levelId=" + super.getLevelId() +
                "name=" + super.getName() +
                "superiorDesignation=" + superiorDesignation +
                ", colleague=" + colleague +
                '}';
    }
}
