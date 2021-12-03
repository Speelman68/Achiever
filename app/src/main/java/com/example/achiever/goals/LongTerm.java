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
        // A long term goal has an end date and a description.
        this.endDate = stringEndDate;
        this.description = longTermDescription;
    }
}
