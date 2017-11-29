package com.example.koushik.homework9;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements ItemPeopleClickback{

    ArrayList<TripLocation> locations;
    Button button2map;
    RecyclerView rv_locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_location);

        button2map= (Button) findViewById(R.id.button2map);
        rv_locations= (RecyclerView) findViewById(R.id.rv_locations);

        locations= (ArrayList<TripLocation>) getIntent().getExtras().getSerializable("LOCATIONS");

        RecyclerLocationListAdapter adapter=new RecyclerLocationListAdapter(locations,this,R.layout.location_item);
        rv_locations.setLayoutManager(new LinearLayoutManager(this));
        rv_locations.setAdapter(adapter);


        button2map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MapActivity.this,MapsActivity.class);
                intent.putExtra("LOCATIONS",locations);
                startActivity(intent);
            }
        });
    }

    @Override
    public void POnContainerClick(final int position) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Are you sure to delete this location?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("Trips");
                        TripDetailsActivity.trip.getLocations().remove(position);
                        dbref.child(TripDetailsActivity.trip.getTripId()).setValue(TripDetailsActivity.trip);
                        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                TripDetailsActivity.trip=dataSnapshot.child(TripDetailsActivity.trip.getTripId()).getValue(Trip.class);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        locations=TripDetailsActivity.trip.getLocations();
                        RecyclerLocationListAdapter adapter=new RecyclerLocationListAdapter(TripDetailsActivity.trip.getLocations()
                                ,MapActivity.this,R.layout.location_item);
                        rv_locations.setLayoutManager(new LinearLayoutManager(MapActivity.this));
                        rv_locations.setAdapter(adapter);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void POnSubItemCLick(int position) {

    }
}
