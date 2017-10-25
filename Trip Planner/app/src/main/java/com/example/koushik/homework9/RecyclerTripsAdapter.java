package com.example.koushik.homework9;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerTripsAdapter extends RecyclerView.Adapter<RecyclerTripsHolder> {
    LayoutInflater inflater;
    List<Trip> trips;
    Context context;
    SharedPreferences tempPref;
    SharedPreferences.Editor editor;
    ItemTripCallback itemClickCallback;
    int layout;
    boolean temp_type;
    //editor=tempPref.edit();


    public RecyclerTripsAdapter(ArrayList<Trip> users, Context context, int layout) {
        this.trips =users;
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.layout=layout;
        this.itemClickCallback= (ItemTripCallback) context;

    }


    @Override
    public RecyclerTripsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(this.layout, parent, false);
        return new RecyclerTripsHolder(view,itemClickCallback);
    }

    @Override
    public void onBindViewHolder(RecyclerTripsHolder holder, int position) {
        Trip t = trips.get(position);
        if(t.getImageUrl()!=null){
            Picasso.with(context).load(t.getImageUrl()).into(holder.imageView);
        }
        if(UserActivity.user.getId().equals(t.getCreatedBy())){
            holder.join.setVisibility(View.INVISIBLE);
        }
        if(t.getMembers()!=null){
            if(t.getMembers().contains(UserActivity.user.getId())){
                holder.join.setVisibility(View.INVISIBLE);
            }
        }

        holder.textView.setText(t.getTitle());
    }



    @Override
    public int getItemCount() {
        return trips.size();
    }
}

