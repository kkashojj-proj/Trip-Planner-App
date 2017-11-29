package com.example.koushik.homework9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TripsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TripsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    RecyclerView rv;
    public static ArrayList<Trip> trips;
    CardView createTrip;
    TextView notrips;

    Context context;

    public TripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_trips, container, false);
        createTrip= (CardView) v.findViewById(R.id.btn_addtrip);
        rv= (RecyclerView) v.findViewById(R.id.rv_trips);
        notrips= (TextView) v.findViewById(R.id.notrips);
        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCreateTripClick();
            }
        });
        context=getContext();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {

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
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Trips");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trips=new ArrayList<Trip>();
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Trip t=d.getValue(Trip.class);
                    for(String s:UserActivity.user.getFriends()){
                        if(t.getMembers().contains(s)||t.getMembers().contains(UserActivity.user.getId())){
                            if(!trips.contains(t)){
                                trips.add(t);
                            }
                        }
                    }
                }
                if(trips.size()>0){
                    notrips.setVisibility(View.INVISIBLE);
                    RecyclerTripsAdapter adapter=new RecyclerTripsAdapter(trips,context,R.layout.trip_card);
                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    notrips.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
        void onCreateTripClick();
    }
}
