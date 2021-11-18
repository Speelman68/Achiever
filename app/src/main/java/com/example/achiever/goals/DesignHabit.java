package com.example.achiever.goals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.achiever.R;
import com.example.achiever.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DesignHabit extends AppCompatActivity {
    String description;
    String reward;
    TextView descriptionView;
    TextView rewardView;
    boolean[] checkedDays;
    ArrayList<CheckBox> dayCheckBox;
    User user;
    int habitSlot = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_design_habit);
        user = ((User) this.getApplication());
        checkedDays = new boolean[7];
        checkDayList();

        descriptionView = (TextView) findViewById(R.id.habitName);
        rewardView = (TextView) findViewById(R.id.reward);

        loadHabit();
        super.onCreate(savedInstanceState);
    }

    public void loadHabit() {
        if (user.slot < 0)
            return;

        habitSlot = user.slot;

        int i = 0;
        for (LinkedHashMap.Entry<String, Boolean> entry : user.habits.get(habitSlot).scheduledDays.entrySet()) {
            dayCheckBox.get(i).setChecked(entry.getValue());
            checkedDays[i] = entry.getValue();
            i++;
        }

        descriptionView.setText(user.habits.get(habitSlot).getDescription());
        rewardView.setText(user.habits.get(habitSlot).getReward());
    }

    private void checkDayList(){
        if (dayCheckBox != null)
            return;

        dayCheckBox = new ArrayList<CheckBox>(7);
        dayCheckBox.add((CheckBox) findViewById(R.id.sunday));
        dayCheckBox.add((CheckBox) findViewById(R.id.monday));
        dayCheckBox.add((CheckBox) findViewById(R.id.tuesday));
        dayCheckBox.add((CheckBox) findViewById(R.id.wednesday));
        dayCheckBox.add((CheckBox) findViewById(R.id.thursday));
        dayCheckBox.add((CheckBox) findViewById(R.id.friday));
        dayCheckBox.add((CheckBox) findViewById(R.id.saturday));

    }

    public void isSundayChecked(View view) {
        checkedDays[0] = ((CheckBox) view).isChecked();
    }

    public void isMondayChecked(View view) {
        checkedDays[1] = ((CheckBox) view).isChecked();
    }

    public void isTuesdayChecked(View view) {
        checkedDays[2] = ((CheckBox) view).isChecked();
    }

    public void isWednesdayChecked(View view) {
        checkedDays[3] = ((CheckBox) view).isChecked();
    }

    public void isThursdaysChecked(View view) {
        checkedDays[4] = ((CheckBox) view).isChecked();
    }

    public void isFridayChecked(View view) {
        checkedDays[5] = ((CheckBox) view).isChecked();
    }

    public void isSaturdayChecked(View view) {
        checkedDays[6] = ((CheckBox) view).isChecked();
    }


    public void saveHabit(View view) {
        description = descriptionView.getText().toString();
        reward = rewardView.getText().toString();

        Habit newHabit = new Habit(checkedDays, description, reward);
        if(habitSlot < 0){
            user.habits.add(newHabit);
        }
        else {
            user.habits.set(habitSlot, newHabit);
        }
        finish();
    }
}