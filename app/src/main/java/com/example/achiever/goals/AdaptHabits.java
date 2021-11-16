package com.example.achiever.goals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.achiever.R;
import com.example.achiever.User;

import java.util.ArrayList;

public class AdaptHabits extends BaseAdapter implements ListAdapter {
    private ArrayList<Habit> list;
    private Context context;
    private User user;

    public AdaptHabits(Context context, ArrayList<Habit> list, User user) {
        this.list = list;
        this.context = context;
        this.user = user;
    }

    @Override
    public int getCount() {
        return (list==null)?0:list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //OLD: list.get(pos).getId()
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.display_habit_item, null);
        }
        //Handle TextView and display string from your list
        TextView tvContact= (TextView)view.findViewById(R.id.tvContact);
        tvContact.setText(list.get(position).getDescription());

        //Handle buttons and add onClickListeners
        Button callbtn= (Button)view.findViewById(R.id.btn);

        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                user.slot = position;
                Intent habitIntent = new Intent(context, DesignHabit.class);
                v.getContext().startActivity(habitIntent);
            }
        });

        return view;
    }
}