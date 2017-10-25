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

public class RecyclerPendingAdapter extends RecyclerView.Adapter<RecyclerPendingHolder> {
    LayoutInflater inflater;
    List<User> users;
    Context context;
    SharedPreferences tempPref;
    SharedPreferences.Editor editor;
    ItemClickCallback itemClickCallback;
    int layout;
    boolean temp_type;
    //editor=tempPref.edit();


    public RecyclerPendingAdapter(ArrayList<User> users, Context context, int layout) {
        this.users=users;
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.itemClickCallback= (ItemClickCallback) context;
        this.layout=layout;

    }


    @Override
    public RecyclerPendingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(this.layout, parent, false);
        return new RecyclerPendingHolder(view,itemClickCallback);
    }

    @Override
    public void onBindViewHolder(RecyclerPendingHolder holder, int position) {
        User user = users.get(position);
        Picasso.with(context).load(R.mipmap.respond).resize(100,100).into(holder.imageButton);
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

