package com.example.koushik.homework9;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerTripsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView textView;
    ImageView imageView;
    CardView join;
    View container;
    ItemTripCallback itemClickCallback;



    public RecyclerTripsHolder(View itemView, ItemTripCallback itemClickCallback) {
        super(itemView);

            textView= (TextView) itemView.findViewById(R.id.trip_text);
            imageView= (ImageView) itemView.findViewById(R.id.trip_img);
            container=itemView.findViewById(R.id.trip_card);
            join= (CardView) itemView.findViewById(R.id.join);
            join.setOnClickListener(this);
            this.itemClickCallback=itemClickCallback;
            container.setOnClickListener(this);


        }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.join){
            itemClickCallback.onTripJoinClick(getAdapterPosition());
        }else{
            itemClickCallback.onTripCardClick(getAdapterPosition());
        }

    }
}