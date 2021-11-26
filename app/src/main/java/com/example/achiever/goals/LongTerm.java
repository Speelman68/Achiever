package com.example.achiever.goals;


public class LongTerm {
    public String getDescription () {
        return description;
    }


    public String getEndDate () {
        return endDate;
    }


    String description;
    String endDate;

    public LongTerm (String longTermDescription, String stringEndDate) {
        this.endDate = stringEndDate;
        this.description = longTermDescription;
    }
}
