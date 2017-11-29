package com.example.koushik.homework9;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripDetailsActivity extends AppCompatActivity implements ItemPeopleClickback,
        LocationFragment.OnFragmentInteractionListener,TripDetailsFragment.OnFragmentInteractionListener,TripMembersFragment.OnFragmentInteractionListener,ItemClickCallback {
    CardView members,chatroom;
    TextView title,location;
    RecyclerView rv_members;
    public static ArrayList<User> tripusers;
    public static Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        trip= (Trip) getIntent().getExtras().getSerializable("Trip");
        getSupportFragmentManager().beginTransaction().replace(R.id.usercontent,new TripDetailsFragment()).commit();
        final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tripusers=new ArrayList<User>();
                for(String s:trip.getMembers()){
                    User user=dataSnapshot.child(s).getValue(User.class);
                    tripusers.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void OnSubItemCLick(int object) {

    }

    @Override
    public void OnContainerClick(int position) {


    }

    @Override
    public void onMembersClick() {
        getSupportFragmentManager().beginTransaction().replace(R.id.usercontent,new TripMembersFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onChatRoomClick() {
        Intent intent =new Intent(TripDetailsActivity.this,ChatRoomActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLocationClick() {
        //getSupportFragmentManager().beginTransaction().replace(R.id.usercontent,new LocationFragment()).addToBackStack(null).commit();
        Intent intent=new Intent(this,AddLocationActivity.class);
        startActivity(intent);
    }

    @Override
    public void POnContainerClick(final int position) {




    }

    @Override
    public void POnSubItemCLick(int position) {

    }
}