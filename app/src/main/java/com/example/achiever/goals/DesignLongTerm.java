package com.example.achiever.goals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.achiever.R;
import com.example.achiever.User;

import java.text.BreakIterator;
import java.util.Calendar;
import java.util.Date;

public class DesignLongTerm extends AppCompatActivity

    {
        User user;
        String description;
        String stringEndDate;
        TextView descriptionView;
        int longTermSlot = -1;
        private DatePickerDialog datePickerDialog;
        private Button dateButton;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_long_term);
        user = ((User) this.getApplication());
        initDatePicker();
        descriptionView = (TextView) findViewById(R.id.longTermName);
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
    }

        private String getTodaysDate() {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            month =  month + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            return makeDateString(day, month, year);
        }

        private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet (DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
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

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month){
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";
        //default:
        return "JAN";
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    public void saveLongTerm(View view) {
        description = descriptionView.getText().toString();
        stringEndDate = dateButton.getText().toString();

        LongTerm newLongTerm = new LongTerm(description, stringEndDate);

        if(longTermSlot < 0){
            user.longTerms.add(newLongTerm);
        }
        else {
            user.longTerms.set(longTermSlot, newLongTerm);
        }
        finish();
    }
}