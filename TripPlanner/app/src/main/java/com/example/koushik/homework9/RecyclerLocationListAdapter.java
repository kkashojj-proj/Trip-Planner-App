package com.example.koushik.homework9;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by KOUSHIK on 01-04-2017.
 */

public class RecyclerLocationListAdapter extends RecyclerView.Adapter<RecyclerLocationHolder> {
    LayoutInflater inflater;
    Context context;
    SharedPreferences tempPref;
    SharedPreferences.Editor editor;
    int layout;
    ArrayList<TripLocation> places;
    boolean temp_type;
    ItemPeopleClickback itemClickCallback;
    //editor=tempPref.edit();


    public RecyclerLocationListAdapter(ArrayList<TripLocation> places, Context context, int layout) {
        this.places=places;
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.layout=layout;
        this.itemClickCallback= (ItemPeopleClickback) context;


    }


    @Override
    public RecyclerLocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(this.layout, parent, false);
        return new RecyclerLocationHolder(view,itemClickCallback);
    }

    @Override
    public void onBindViewHolder(RecyclerLocationHolder holder, int position) {
        String s = places.get(position).getLocality();
        holder.textView.setText(s);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}

