package com.example.koushik.homework9;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ChatRoomActivity extends AppCompatActivity implements ItemClickCallback {
    RecyclerView recyclerView;
    ArrayList<Message> MessageList,temp;
    ImageButton send,image;
    EditText message;
    RecyclerChat adapter;
    String photourl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //MessageList =  new ArrayList<>();
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        recyclerView = (RecyclerView) findViewById(R.id.rv_chat);
        send = (ImageButton) findViewById(R.id.ib_send);
        image= (ImageButton) findViewById(R.id.ib_img);
        message = (EditText) findViewById(R.id.et_msg);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserActivity.user=dataSnapshot.child(UserActivity.user.getId()).getValue(User.class);
                FirebaseDatabase.getInstance().getReference("Messages").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        MessageList=new ArrayList<Message>();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            Message msg=dataSnapshot1.getValue(Message.class);
                            if(UserActivity.user.getMessages().contains(msg.getMeg_key())){
                                if(msg.getTripId().equals(TripDetailsActivity.trip.getTripId())){
                                    MessageList.add(msg);
                                }
                            }
                        }
                        inflateRecyclerView(MessageList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MessageList=new ArrayList<Message>();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Message msg=dataSnapshot1.getValue(Message.class);
                    if(UserActivity.user.getMessages().contains(msg.getMeg_key())){
                        if(msg.getTripId().equals(TripDetailsActivity.trip.getTripId())){
                            MessageList.add(msg);
                        }
                    }
                }
                inflateRecyclerView(MessageList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //in chats activity we need to fetch the chat id from trip id(currently clicked)
        //go to database reference of this user id in this chat id
        //fetch all the message id`s from the above branch
        //fetch all the messages for above id
        /*DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("Messages");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MessageList =  new ArrayList<>();
                temp=new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Message msg=postSnapshot.getValue(Message.class);
                    temp.add(msg);
                    *//*if(msg.getTripId().equals(TripDetailsActivity.trip.getTripId())){
                            if(UserActivity.user.getMessages().contains(msg.getMeg_key())){
                                Log.d("demo","message added"+msg.getMessage());
                                MessageList.add(msg);
                            }
                    }*//*

                }
                //getMessages(temp);
                //inflateRecyclerView(MessageList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Messages");
                final Message m = new Message();
                m.setMessage(message.getText().toString());
                //need to change this to id
                m.setSent_by(UserActivity.user.getId());
                m.setTime(System.currentTimeMillis());
                m.setTripId(TripDetailsActivity.trip.getTripId());
                final DatabaseReference push = databaseReference.push();
                m.setMeg_key(push.getKey());

                message.setText("");
                push.setValue(m);
                for(final String s:TripDetailsActivity.trip.getMembers()){
                    final DatabaseReference dburef = FirebaseDatabase.getInstance().getReference("Users");
                    dburef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User u=dataSnapshot.child(s).getValue(User.class);
                            u.addMessage(m.getMeg_key());
                            dburef.child(s).setValue(u);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserActivity.user=dataSnapshot.child(UserActivity.user.getId()).getValue(User.class);
                        FirebaseDatabase.getInstance().getReference("Messages").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                MessageList=new ArrayList<Message>();
                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                    Message msg=dataSnapshot1.getValue(Message.class);
                                    if(UserActivity.user.getMessages().contains(msg.getMeg_key())){
                                        if(msg.getTripId().equals(TripDetailsActivity.trip.getTripId())){
                                            MessageList.add(msg);
                                        }
                                    }
                                }
                                inflateRecyclerView(MessageList);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseDatabase.getInstance().getReference("Messages").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        MessageList=new ArrayList<Message>();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            Message msg=dataSnapshot1.getValue(Message.class);
                            if(UserActivity.user.getMessages().contains(msg.getMeg_key())){
                                if(msg.getTripId().equals(TripDetailsActivity.trip.getTripId())){
                                    MessageList.add(msg);
                                }
                            }
                        }
                        inflateRecyclerView(MessageList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    /*private void getMessages(final ArrayList<Message> t) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u=dataSnapshot.child(UserActivity.user.getId()).getValue(User.class);
                for(Message m:t){
                    if(u.getMessages().contains(m.getMeg_key())){
                        MessageList.add(m);
                    }
                }
                inflateRecyclerView(MessageList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/




    private void inflateRecyclerView(ArrayList<Message> messageList) {
        adapter=new RecyclerChat(MessageList,ChatRoomActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatRoomActivity.this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
        llm.scrollToPositionWithOffset(messageList.size()-1 , messageList.size());
    }

    @Override
    public void OnSubItemCLick(int object) {

    }

    @Override
    public void OnContainerClick(final int object) {

        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u=dataSnapshot.child(UserActivity.user.getId()).getValue(User.class);
                u.getMessages().remove(MessageList.get(object).getMeg_key());
                ref.child(u.getId()).setValue(u);
                Toast.makeText(ChatRoomActivity.this,"Message deleted successfully",Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("Messages").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        MessageList=new ArrayList<Message>();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            Message msg=dataSnapshot1.getValue(Message.class);
                            if(UserActivity.user.getMessages().contains(msg.getMeg_key())){
                                if(msg.getTripId().equals(TripDetailsActivity.trip.getTripId())){
                                    MessageList.add(msg);
                                }
                            }
                        }
                        inflateRecyclerView(MessageList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(ChatRoomActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                User user= dataSnapshot.child(UserActivity.user.getId()).getValue(User.class);
                String s=MessageList.get(object).getMeg_key();
                MessageList.remove(object);
                user.getMessages().remove(s);
                ref.child(user.getId()).removeValue();
                ref.child(user.getId()).setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        //inflateRecyclerView(MessageList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri imageUri = data.getData();
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            FirebaseStorage.getInstance().getReference("Images").child(UUID.randomUUID().toString()).putStream(inputStream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    photourl=downloadUrl.toString();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Messages");
                    final Message m = new Message();
                    m.setImgurl(photourl);
                    m.setSent_by(UserActivity.user.getId());
                    m.setTime(System.currentTimeMillis());
                    m.setTripId(TripDetailsActivity.trip.getTripId());
                    final DatabaseReference push = databaseReference.push();
                    m.setMeg_key(push.getKey());
                    push.setValue(m);
                    photourl=null;
                    for(final String s:TripDetailsActivity.trip.getMembers()){
                        final DatabaseReference dburef = FirebaseDatabase.getInstance().getReference("Users");
                        dburef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User u=dataSnapshot.child(s).getValue(User.class);
                                u.addMessage(m.getMeg_key());
                                dburef.child(s).setValue(u);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserActivity.user=dataSnapshot.child(UserActivity.user.getId()).getValue(User.class);
                            FirebaseDatabase.getInstance().getReference("Messages").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    MessageList=new ArrayList<Message>();
                                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                        Message msg=dataSnapshot1.getValue(Message.class);
                                        if(UserActivity.user.getMessages().contains(msg.getMeg_key())){
                                            if(msg.getTripId().equals(TripDetailsActivity.trip.getTripId())){
                                                MessageList.add(msg);
                                            }
                                        }
                                    }
                                    inflateRecyclerView(MessageList);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    FirebaseDatabase.getInstance().getReference("Messages").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            MessageList=new ArrayList<Message>();
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                Message msg=dataSnapshot1.getValue(Message.class);
                                if(UserActivity.user.getMessages().contains(msg.getMeg_key())){
                                    if(msg.getTripId().equals(TripDetailsActivity.trip.getTripId())){
                                        MessageList.add(msg);
                                    }
                                }
                            }
                            inflateRecyclerView(MessageList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatRoomActivity.this,"Upload failed",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
