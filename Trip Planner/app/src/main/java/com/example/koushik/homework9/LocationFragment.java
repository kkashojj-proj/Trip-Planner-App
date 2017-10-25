package com.example.koushik.homework9;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LocationFragment extends Fragment{

    private OnFragmentInteractionListener mListener;
    EditText et_location;
    Context context;
    RecyclerView rv_locations;
    Button button2map,button;
    public static ArrayList<Address> locationNames = null;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.context=getContext();

        View v=inflater.inflate(R.layout.activity_maps, container, false);
        et_location= (EditText) v.findViewById(R.id.et_location);
        rv_locations= (RecyclerView) v.findViewById(R.id.rv_locations);
        button= (Button) v.findViewById(R.id.button);
        button2map= (Button) v.findViewById(R.id.button2map);
        button2map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MapsActivity.class);
                intent.putExtra("LOCATIONS",TripDetailsActivity.trip.getLocations());
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=et_location.getText().toString();
                if(s.length()>0){
                    Geocoder geocoder=new Geocoder(context);
                    Address a=null;
                    try {
                        List<Address> fromLocationName = geocoder.getFromLocationName(s, 1);
                        a=fromLocationName.get(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    TripLocation location=new TripLocation();
                    location.setLocality(a.getLocality()+" ,"+a.getCountryCode());
                    location.setLat(a.getLatitude());
                    location.setLng(a.getLongitude());
                    if(a!=null && a.getLocality()!=null && a.getCountryCode()!=null){
                        int flag=0;
                        for(TripLocation l:TripDetailsActivity.trip.getLocations()){
                            if(l.getLat().equals(location.getLat()) && l.getLng().equals(location.getLng())){
                                flag=1;
                                break;
                            }
                        }

                        if(flag==0){
                            TripDetailsActivity.trip.addLocation(location);

                            DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("Trips");
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
                            RecyclerLocationListAdapter adapter=new RecyclerLocationListAdapter(TripDetailsActivity.trip.getLocations(),context,R.layout.location_item);
                            rv_locations.setLayoutManager(new LinearLayoutManager(context));
                            rv_locations.setAdapter(adapter);
                        }else{
                            Toast.makeText(context,"Location exists",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(context,"No such location found",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,"Please enter a location",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("Trips");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TripDetailsActivity.trip=dataSnapshot.child(TripDetailsActivity.trip.getTripId()).getValue(Trip.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        RecyclerLocationListAdapter adapter=new RecyclerLocationListAdapter(TripDetailsActivity.trip.getLocations(),context,R.layout.location_item);
        rv_locations.setLayoutManager(new LinearLayoutManager(context));
        rv_locations.setAdapter(adapter);




    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
