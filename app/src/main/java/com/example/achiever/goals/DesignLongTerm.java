package com.example.achiever.goals;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.achiever.DateHandler;
import com.example.achiever.Firebase.FireBaseCloud;
import com.example.achiever.R;
import com.example.achiever.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DesignLongTerm extends AppCompatActivity

    {

        FireBaseCloud mCloud = new FireBaseCloud();

        DateHandler dateHandler = new DateHandler();
        User user;
        String description;
        String stringEndDate;
        TextView descriptionView;
        private DatePickerDialog datePickerDialog;
        private Button dateButton;
        int longTermSlot;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        // The activity linked to this class edits the long term and takes the intent from
        // the display long term activity/goal not achieved activity.
        // In case that it is a new goal the position will be null, so the fields will be blank.
        // In case the position is not null, the fields will be filled with the description
        // and the end date of the goal.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_long_term);
        user = ((User) this.getApplication());
        initDatePicker();
        descriptionView = (TextView) findViewById(R.id.longTermName);
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(dateHandler.getTodaysDate());

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            longTermSlot = getIntent().getIntExtra("Position", -1);
            loadLongTerm();
        } else {
            longTermSlot = -1;
        }
    }

    public void loadLongTerm() {
        // Loads the long term goal that is going to be edited.
        if (longTermSlot < 0)
            return;
        descriptionView.setText(user.longTerms.get(longTermSlot).getDescription());
        dateButton.setText(user.longTerms.get(longTermSlot).getEndDate());
    }
        private void initDatePicker(){
        // Initializes a Date Picker dialog button.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet (DatePicker datePicker, int year, int month, int day) {
                // Sets the date within the button when it is selected from the dialog.
                month = month + 1;
                String date = dateHandler.makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());

        }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    public void saveLongTerm(View view) {


        // Saves the long term goal using the user reference.
        description = descriptionView.getText().toString();
        stringEndDate = dateButton.getText().toString();

        LongTerm newLongTerm = new LongTerm(description, stringEndDate);

        if (longTermSlot < 0){
            user.longTerms.add(newLongTerm);
        }
        else {
            user.longTerms.set(longTermSlot, newLongTerm);
        }
        mCloud.saveGoal(stringEndDate, description);
        finish();
    }
}