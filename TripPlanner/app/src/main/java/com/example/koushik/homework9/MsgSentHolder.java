package com.example.koushik.homework9;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by HARSH on 21-04-2017.
 */

public class MsgSentHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    ImageView iv;
    TextView tv_message,tv_sender,tv_time;
    CardView scard;
    ItemClickCallback itemClickCallback;

    public MsgSentHolder(View itemView,ItemClickCallback itemClickCallback) {
        super(itemView);
        scard= (CardView) itemView.findViewById(R.id.scard);
        iv = (ImageView) itemView.findViewById(R.id.iv_sent);
        tv_message = (TextView) itemView.findViewById(R.id.msg_sent);
        tv_sender =  (TextView) itemView.findViewById(R.id.tv_sender_sent);
        tv_time =  (TextView) itemView.findViewById(R.id.tv_pretty);
        this.itemClickCallback=itemClickCallback;
        scard.setOnLongClickListener(this);

    }



    @Override
    public boolean onLongClick(View view) {
        itemClickCallback.OnContainerClick(getAdapterPosition());
        return true;
    }
}

