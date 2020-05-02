package com.example.covid19;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class worldinfoAdapter extends ArrayAdapter<cases> {


    public worldinfoAdapter(Activity context ,ArrayList<cases> word){
        super(context,0,word);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.worldinfo,parent,false);
        }

        cases cur = getItem(position);

        String confirmed ;
        confirmed = Long.toString(cur.confirmed);

        TextView Tactive = view.findViewById(R.id.worldquerryans);
        Tactive.setText(confirmed);


        TextView state = view.findViewById(R.id.worldquerry);
        state.setText(cur.date);




        return view;
    }
}
