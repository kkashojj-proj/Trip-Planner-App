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

public class RecyclerPeopleAdapter extends RecyclerView.Adapter<RecyclerPeopleHolder> {
    LayoutInflater inflater;
    List<User> users;
    Context context;
    SharedPreferences tempPref;
    SharedPreferences.Editor editor;
    ItemPeopleClickback itemClickCallback;
    int layout;
    //editor=tempPref.edit();


    public RecyclerPeopleAdapter(ArrayList<User> users, Context context, int layout) {
        this.users=users;
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.itemClickCallback= (ItemPeopleClickback) context;
        this.layout=layout;

    }


    @Override
    public RecyclerPeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(this.layout, parent, false);
        return new RecyclerPeopleHolder(view,itemClickCallback);
    }

    @Override
    public void onBindViewHolder(RecyclerPeopleHolder holder, int position) {
        User user = users.get(position);
        if(user.getImageUrl()!=null){
            Picasso.with(context).load(user.getImageUrl()).into(holder.imageView);
        }
        holder.textView.setText(user.getFirstNmame()+" "+user.getLastName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

