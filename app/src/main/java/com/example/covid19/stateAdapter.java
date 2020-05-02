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

public class stateAdapter extends ArrayAdapter<cases> {


    public stateAdapter(Activity context ,ArrayList<cases> word){
        super(context,0,word);
    }

   
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.simple,parent,false);
        }

        cases cur = getItem(position);

        String confirmed , recovered, deaths,delconfirmed , delrecovered, deldeaths;
        confirmed = Long.toString(cur.confirmed);
        recovered = Long.toString(cur.recovered);
        deaths = Long.toString(cur.deaths);
        delconfirmed = "\u2191"+ Long.toString(cur.delconfirmed);
        delrecovered = "\u2191" +Long.toString(cur.delrecovered);
        deldeaths = "\u2191" + Long.toString(cur.deldeaths);

        TextView Tactive = view.findViewById(R.id.stateactive);
        Tactive.setText(confirmed);

        TextView Trecovered = view.findViewById(R.id.staterecovered);
        Trecovered.setText(recovered);

        TextView Tdeath = view.findViewById(R.id.statedeaths);
        Tdeath.setText(deaths);

        TextView Tdelconfirmed = view.findViewById(R.id.delstateactive);
        Tdelconfirmed.setText(delconfirmed);

        TextView Tdrecovered = view.findViewById(R.id.delstaterecovered);
        Tdrecovered.setText(delrecovered);

        TextView Tddeath = view.findViewById(R.id.delstatedeaths);
        Tddeath.setText(deldeaths);

        TextView state = view.findViewById(R.id.statename);
        state.setText(cur.date);




        return view;
    }
}
