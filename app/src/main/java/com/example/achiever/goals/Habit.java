package com.example.achiever.goals;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TreeMap;

public class Habit extends Goal{
    public LinkedHashMap<String,Boolean> scheduledDays;
    public LinkedHashMap<String,Boolean> completedDays;
    String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private boolean dailyCompletion;
    public int streak;

    @Override
    public String toString() {
        return (getDescription());
    }

    public Habit() {
        scheduledDays = new LinkedHashMap<>();
        completedDays = new LinkedHashMap<>();
        for (int i = 0;i < 7;i++){
            scheduledDays.put(days[i], false);
            completedDays.put(days[i], false);
        }
    }

    public Habit(String description) {
        setDescription(description);
        scheduledDays = new LinkedHashMap<>();
        completedDays = new LinkedHashMap<>();
        for (int i = 0;i < 7;i++){
            scheduledDays.put(days[i], false);
            completedDays.put(days[i], false);
        }
    }

    public Habit(boolean[] checkedDays, String description, String reward) {
        scheduledDays = new LinkedHashMap<>();
        completedDays = new LinkedHashMap<>();
        for (int i = 0;i < 7;i++){
            scheduledDays.put(days[i], checkedDays[i]);
            completedDays.put(days[i], false);
        }
        setDescription(description);
        setReward(reward);
    }


    public void updateStreak() {
        streak++;
        dailyCompletion = true;
    }


    public void dailyCheck() {
        if (dailyCompletion){
            dailyCompletion = false;
            return;
        }

        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        if(!scheduledDays.get(weekday_name)){
            streak = 0;
        }
    }
}
