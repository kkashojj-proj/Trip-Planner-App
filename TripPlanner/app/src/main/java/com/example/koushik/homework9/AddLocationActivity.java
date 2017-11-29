package com.example.koushik.homework9;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText et_lcation;
    Button button,button2map,button2loc;
    GoogleMap mMap;
    ProgressBar pb;
    TripLocation tripLocation;
    MapView mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        mapFragment = (MapView) findViewById(R.id.map);
        mapFragment.onCreate(savedInstanceState);
        mapFragment.getMapAsync(this);

        pb= (ProgressBar) findViewById(R.id.progressBar2);
        pb.setVisibility(View.VISIBLE);

        et_lcation= (EditText) findViewById(R.id.et_location);
        button2map= (Button) findViewById(R.id.button2map);
        button2loc= (Button) findViewById(R.id.button2loc);
        button= (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loc=et_lcation.getText().toString();
                if(loc.length()>0){
                    Geocoder geocoder=new Geocoder(getApplicationContext());
                    try {
                        List<Address> fromLocationName = geocoder.getFromLocationName(loc, 1);
                        if(fromLocationName!=null && fromLocationName.size()>0){
                            Address address=fromLocationName.get(0);
                            if(address.getCountryCode()=="null"||address.getCountryCode()==null||address.getLocality()=="null"||address.getLocality()==null){
                                Toast.makeText(AddLocationActivity.this, "No such location exists", Toast.LENGTH_SHORT).show();
                            }else{
                                LatLng origin=new LatLng(address.getLatitude(),address.getLongitude());
                                String title=address.getLocality()+" ,"+address.getCountryCode();
                                MarkerOptions mo=new MarkerOptions();
                                tripLocation=new TripLocation();
                                tripLocation.setLat(origin.latitude);
                                tripLocation.setLng(origin.longitude);
                                tripLocation.setLocality(title);
                                mo.position(origin);
                                mo.title(title);
                                mMap.addMarker(mo);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,8f));
                                Toast.makeText(AddLocationActivity.this,title+" exists",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(AddLocationActivity.this, "No such location exists", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        Toast.makeText(AddLocationActivity.this, "Error retrieving location", Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(AddLocationActivity.this,"Please enter a location to search",Toast.LENGTH_SHORT).show();
                }

            }
        });

        button2map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tripLocation!=null){
                    int flag=0;
                    for(TripLocation l:TripDetailsActivity.trip.getLocations()){
                        if(l.getLat().equals(tripLocation.getLat()) && l.getLng().equals(tripLocation.getLng())){
                            flag=1;
                            break;
                        }
                    }

                    if(flag==0){
                        TripDetailsActivity.trip.addLocation(tripLocation);

                        DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("Trips");
                        dbref.child(TripDetailsActivity.trip.getTripId()).setValue(TripDetailsActivity.trip);
                        Toast.makeText(AddLocationActivity.this,"Added",Toast.LENGTH_SHORT).show();
                        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                TripDetailsActivity.trip=dataSnapshot.child(TripDetailsActivity.trip.getTripId()).getValue(Trip.class);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(AddLocationActivity.this,"Already added to Trip",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddLocationActivity.this, "Please search and Select a location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button2loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddLocationActivity.this,MapActivity.class);
                intent.putExtra("LOCATIONS",TripDetailsActivity.trip.getLocations());
                startActivity(intent);
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mapFragment.onResume();
        pb.setVisibility(View.INVISIBLE);
    }
}
