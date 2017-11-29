package com.example.koushik.homework9;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by HARSH on 21-04-2017.
 */

public class MsgRecievedHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
   ImageView iv;
    TextView tv_message,tv_sender,tv_time;
    CardView rcard;
    ItemClickCallback i;

    public MsgRecievedHolder(View itemView,ItemClickCallback itemClickCallback) {
        super(itemView);
        iv = (ImageView) itemView.findViewById(R.id.img_recieved);
        rcard= (CardView) itemView.findViewById(R.id.rcard);
        tv_message = (TextView) itemView.findViewById(R.id.msg_recieved);
        tv_sender =  (TextView) itemView.findViewById(R.id.tv_sender_sent);
        tv_time =  (TextView) itemView.findViewById(R.id.time_recieved);
        this.i=itemClickCallback;
        rcard.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
        i.OnContainerClick(getAdapterPosition());
        return true;
    }
}
