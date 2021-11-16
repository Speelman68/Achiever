package com.example.achiever;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.achiever.goals.Habit;
import com.example.achiever.goals.LongTerm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

public class User extends Application {
    public ArrayList<Habit> habits  = new ArrayList<>();
    public ArrayList<LongTerm> longTerms = new ArrayList<>();
    public int slot = -1;
    public User(){

    }


}
