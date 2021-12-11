package com.example.achiever.goals;

import com.example.achiever.Firebase.FireBaseCloud;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

public class Habit extends Goal{
    public HashMap<String,Boolean> scheduledDays;
    public HashMap<String,Boolean> completedDays;

    FireBaseCloud mCloud = new FireBaseCloud();

    String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    public int streak;

    @Override
    public String toString() {
        return (getDescription());
    }

    public Habit() {
        scheduledDays = new HashMap<>();
        completedDays = new HashMap<>();
        for (int i = 0;i < 7;i++){
            scheduledDays.put(days[i], false);
            completedDays.put(days[i], false);
        }
    }

    public Habit(String description) {
        setDescription(description);
        scheduledDays = new HashMap<>();
        completedDays = new HashMap<>();
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
        mCloud.saveHabit(scheduledDays, completedDays, description, reward, streak);
    }

    //Run this function once the activity is completed.
    public void updateStreak() {
        streak++;
        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        completedDays.put(weekday_name, true);
        mCloud.saveHabit(scheduledDays, completedDays, getDescription(), getReward(), streak);
    }

    //This function gets run every night to check the streak;
    public void dailyCheck() {
        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());

        if(scheduledDays.get(weekday_name))
            if (!completedDays.get(weekday_name)) {
                streak = 0;
                mCloud.saveHabit(scheduledDays, completedDays, getDescription(), getReward(), streak);
            }
    }
}
