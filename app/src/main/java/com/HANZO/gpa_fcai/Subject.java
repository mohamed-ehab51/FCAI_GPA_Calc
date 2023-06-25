package com.HANZO.gpa_fcai;

import java.io.Serializable;

public class Subject {
    String name;
    int hours;
    Double grade;

    public Subject(String name, int hours, Double grade) {
        this.name = name;
        this.hours = hours;
        this.grade = grade;
    }
    public Subject(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
