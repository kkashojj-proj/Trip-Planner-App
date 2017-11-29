package com.example.koushik.homework9;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.List;

/**
 * Created by HARSH on 21-04-2017.
 */

public class RecyclerChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    User user=UserActivity.user;
    List<Message> messagelist;
    Context context;
    final int SEND = 0;
    final int RECIEVED = 1;
    ItemClickCallback itemClickCallback;

    public RecyclerChat(List<Message> messagelist, Context context) {
        this.messagelist = messagelist;
        this.context = context;
        this.itemClickCallback= (ItemClickCallback) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder=null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case SEND:
                View v1 = inflater.inflate(R.layout.msg_sent_layout, parent, false);
                viewHolder = new MsgSentHolder(v1,itemClickCallback);
                Log.d("demo","Send");
                break;
            case RECIEVED:
                View v2 = inflater.inflate(R.layout.msg_received_layout, parent, false);
                Log.d("demo","Recieved");
                viewHolder = new MsgRecievedHolder(v2,itemClickCallback);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SEND:
                MsgSentHolder sent_holder = (MsgSentHolder) holder;
                configureViewHolder_RECIEVE(sent_holder, position);
                break;
            case RECIEVED:
                MsgRecievedHolder recievedHolder = (MsgRecievedHolder) holder;
                configureViewHolder_SEND(recievedHolder, position);
                break;
        }
    }

    private void configureViewHolder_RECIEVE(MsgSentHolder sent_holder, final int position) {
        if(messagelist.get(position).getMessage()!=null){
            sent_holder.tv_sender.setText(user.getFirstNmame()+" "+user.getLastName());
            sent_holder.tv_message.setText(messagelist.get(position).getMessage());
            sent_holder.iv.setVisibility(View.INVISIBLE);

        }else {
            sent_holder.tv_sender.setText(user.getFirstNmame()+" "+user.getLastName());
            Picasso.with(this.context).load(messagelist.get(position).getImgurl()).resize(300,300).into(sent_holder.iv);
            sent_holder.tv_message.setVisibility(View.INVISIBLE);
        }
        PrettyTime p = new PrettyTime();
        sent_holder.tv_time.setText(p.format(new Date(messagelist.get(position).getTime())));
    }

    private void configureViewHolder_SEND(final MsgRecievedHolder recievedHolder, final int position) {
        if(messagelist.get(position).getMessage()!=null){
            recievedHolder.tv_sender.setText(messagelist.get(position).getSent_by());
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User u=dataSnapshot.child(messagelist.get(position).sent_by).getValue(User.class);
                    recievedHolder.tv_sender.setText(u.getFirstNmame()+" "+u.getLastName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            recievedHolder.tv_message.setText(messagelist.get(position).getMessage());
            recievedHolder.iv.setVisibility(View.INVISIBLE);
        }else {
            //recievedHolder.tv_sender.setText(messagelist.get(position).getSent_by());
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User u=dataSnapshot.child(messagelist.get(position).sent_by).getValue(User.class);
                    recievedHolder.tv_sender.setText(u.getFirstNmame()+" "+u.getLastName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Picasso.with(this.context).load(messagelist.get(position).getImgurl()).resize(300,300).into(recievedHolder.iv);
            recievedHolder.tv_message.setVisibility(View.INVISIBLE);
        }
        PrettyTime p = new PrettyTime();
        recievedHolder.tv_time.setText(p.format(new Date(messagelist.get(position).getTime())));
    }

    @Override
    public int getItemCount() {
        return this.messagelist.size();
    }

    @Override
    public int getItemViewType(int position) {
        //Add UserActivity here
        if(user.getId().equals(messagelist.get(position).getSent_by())){
            return SEND;
        }else{
            return RECIEVED;
        }
    }
}
