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
import java.util.HashMap;
import java.util.Locale;

public class User extends Application {
    public static ArrayList<Habit> habits  = new ArrayList<>();
    public static ArrayList<LongTerm> longTerms = new ArrayList<>();
    public int slot = -1;
    Gson gson = new Gson();

    private static String email;
    private HashMap scheduledDays;
    private HashMap completedDays;
    private String habitDescription;
    private String habitReward;
    private int streak;

    private String goalDate;
    private String goalDescription;


    public User(){

    }

    public User(String email, HashMap habit, HashMap goal)
    {
        this.email = email;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap getScheduledDays() {
        return scheduledDays;
    }

    public void setScheduledDays(HashMap scheduledDays) {
        this.scheduledDays = scheduledDays;
    }

    public HashMap getCompletedDays() {
        return completedDays;
    }

    public void setCompletedDays(HashMap completedDays) {
        this.completedDays = completedDays;
    }

    public String getHabitDescription() {
        return habitDescription;
    }

    public void setHabitDescription(String habitDescription) {
        this.habitDescription = habitDescription;
    }

    public String getHabitReward() {
        return habitReward;
    }

    public void setHabitReward(String habitReward) {
        this.habitReward = habitReward;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public String getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(String goalDate) {
        this.goalDate = goalDate;
    }

    public String getGoalDescription() {
        return goalDescription;
    }

    public void setGoalDescription(String goalDescription) {
        this.goalDescription = goalDescription;
    }
}
