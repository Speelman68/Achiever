package com.example.achiever.calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.achiever.R;
import com.example.achiever.goals.DesignHabit;
import com.example.achiever.goals.DisplayHabit;
import com.example.achiever.goals.Habit;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;


@RequiresApi(api = Build.VERSION_CODES.O)
public class EventAdapter extends ArrayAdapter<Habit> {
    EventAdapter eventAdapter;
    private Context context;
    Intent intent;


    public EventAdapter(@NonNull Context context, List<Habit> events)
    {

        super(context, 0, events);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Habit habit = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        DayOfWeek dow = CalendarUtils.selectedDate.getDayOfWeek();
        String dayS = dow.getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
        String current = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        boolean isCurrentDay = (dayS.equals(current));

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);

        Button btn=(Button) convertView.findViewById(R.id.start);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                habit.completedDays.put(dayS, true);
                eventAdapter.notifyDataSetChanged();

//                Intent timerIntent = new Intent(context, TimerScreen.class);
//                v.getContext().startActivity(timerIntent);
                Intent timerIntent = new Intent(v.getContext(), TimerScreen.class);
                v.getContext().startActivity(timerIntent);

            }
        });

        Boolean showBtn= (!habit.completedDays.get(dayS));
        btn.setVisibility((showBtn && isCurrentDay) ? View.VISIBLE : View.INVISIBLE);

        String eventTitle = habit.getDescription();
        eventCellTV.setText(eventTitle);
        return convertView;
    }
}