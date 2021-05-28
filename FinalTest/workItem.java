package com.example.finaltest;

public class workItem {
    private String day;
    private String workType;

    public workItem(String day, String workType) {
        this.day = day;
        this.workType = workType;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }


}
