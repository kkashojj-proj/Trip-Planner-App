package com.example.koushik.homework9;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by KOUSHIK on 01-04-2017.
 */

public class RecyclerLocationHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

    TextView textView;
    View container;
    ItemPeopleClickback itemClickCallback;



    public RecyclerLocationHolder(View itemView,ItemPeopleClickback itemClickCallback) {
        super(itemView);
        this.itemClickCallback=itemClickCallback;
        textView= (TextView) itemView.findViewById(R.id.textView10);
        container=itemView.findViewById(R.id.container_all);
        container.setOnLongClickListener(this);


    }


    @Override
    public boolean onLongClick(View view) {
        itemClickCallback.POnContainerClick(getAdapterPosition());
        return true;
    }
}