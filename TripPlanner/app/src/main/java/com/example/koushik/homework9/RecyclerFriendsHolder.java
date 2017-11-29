package com.example.koushik.homework9;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by KOUSHIK on 01-04-2017.
 */

public class RecyclerFriendsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView textView;
    ImageView imageView;
    ImageButton imageButton;
    View container;
    ItemClickCallback itemClickCallback;



    public RecyclerFriendsHolder(View itemView, ItemClickCallback itemClickCallback) {
        super(itemView);
            textView= (TextView) itemView.findViewById(R.id.textView3);
            imageView= (ImageView) itemView.findViewById(R.id.imageView2);
            imageButton= (ImageButton) itemView.findViewById(R.id.imageButton2);
            container=itemView.findViewById(R.id.container_friend);
            imageButton.setOnClickListener(this);
            container.setOnClickListener(this);
            this.itemClickCallback=itemClickCallback;

        }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.imageButton2){
            itemClickCallback.OnSubItemCLick(getAdapterPosition());
        }else{
            itemClickCallback.OnContainerClick(getAdapterPosition());

        }
    }
}