package com.example.achiever;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.achiever.calendar.Event;
import com.example.achiever.goals.Habit;
import com.example.achiever.goals.LongTerm;
import com.google.gson.Gson;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class User extends Application {
    public static ArrayList<Habit> habits  = new ArrayList<>();
    public static ArrayList<LongTerm> longTerms = new ArrayList<>();
    public int slot = -1;
    Gson gson = new Gson();

    private String email;
    private String habit;
    private String goal;

    public User(){

    }

    public User(String email, String habit, String goal)
    {
        this.email = email;
        this.habit = habit;
        this.goal = goal;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<Habit> habitsForDate(LocalDate date)
    {
        ArrayList<Habit> tempHabits = new ArrayList<>();

        DayOfWeek dow = date.getDayOfWeek();
        String dayS = dow.getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);

        for(Habit habits : habits)
        {
            if(habits.scheduledDays.get(dayS))
                tempHabits.add(habits);
        }
        return tempHabits;
    }

    //Functions for cloud storage
    public String getEmail() {
        return email;
    }

    public String getHabit() {
        return habit;
    }

    public String getGoal() {
        return goal;
    }

}
