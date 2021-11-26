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

public class AdaptLongTerms extends BaseAdapter implements ListAdapter {
    private ArrayList<LongTerm> list;
    private Context context;
    private User user;
    public AdaptLongTerms adaptLongTerms;

    public AdaptLongTerms (Context context, ArrayList<LongTerm> list, User user) {
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
        String itemDescription;
        String itemEndDate;

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.display_long_term_item, null);
        }
        //Handle TextView and display string from your list
        TextView tvContact= (TextView)view.findViewById(R.id.tvContact);
        itemDescription = list.get(position).getDescription();
        itemEndDate = list.get(position).getEndDate();
        tvContact.setText(itemDescription + " by " + itemEndDate);

        //Handle buttons and add onClickListeners
        Button callbtn= (Button)view.findViewById(R.id.btn);

        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                user.slot = position;
                Intent longTermIntent = new Intent(context, DesignLongTerm.class);
                v.getContext().startActivity(longTermIntent);
            }
        });

        Button removebtn= (Button)view.findViewById(R.id.remove);

        removebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                user.longTerms.remove(position);
                adaptLongTerms.notifyDataSetChanged();
            }
        });

        return view;
    }
}