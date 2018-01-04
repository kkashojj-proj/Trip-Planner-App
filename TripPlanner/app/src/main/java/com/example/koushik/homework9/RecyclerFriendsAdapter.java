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

/**
 * Created by KOUSHIK on 01-04-2017.
 */

public class RecyclerFriendsAdapter extends RecyclerView.Adapter<RecyclerFriendsHolder> {
    LayoutInflater inflater;
    List<User> users;
    Context context;
    SharedPreferences tempPref;
    SharedPreferences.Editor editor;
    ItemClickCallback itemClickCallback;
    int layout;
    boolean temp_type;
    //editor=tempPref.edit();


    public RecyclerFriendsAdapter(ArrayList<User> users, Context context, int layout) {
        this.users=users;
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.itemClickCallback= (ItemClickCallback) context;
        this.layout=layout;
    }


    @Override
    public RecyclerFriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(this.layout, parent, false);
        return new RecyclerFriendsHolder(view,itemClickCallback);
    }

    @Override
    public void onBindViewHolder(RecyclerFriendsHolder holder, int position) {
        User user = users.get(position);
        if(user.getImageUrl()!=null){
            Picasso.with(context).load(user.getImageUrl()).into(holder.imageView);
        }
        holder.imageButton.setVisibility(View.INVISIBLE);
        holder.textView.setText(user.getFirstNmame()+" "+user.getLastName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

