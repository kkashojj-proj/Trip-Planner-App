package com.example.koushik.homework9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
 * {@link TripMembersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TripMembersFragment extends Fragment {
    Context context;
    RecyclerView rv;
    TextView nomem;

    private OnFragmentInteractionListener mListener;

    public TripMembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_trip_members, container, false);
        context=getContext();
        nomem= (TextView) v.findViewById(R.id.nomembers);
        rv= (RecyclerView) v.findViewById(R.id.rv);
        // Inflate the layout for this fragment
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
        final ArrayList<User> us=new ArrayList<User>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    User u=d.getValue(User.class);
                    if(TripDetailsActivity.trip.getMembers().contains(u.getId())||TripDetailsActivity.trip.getCreatedBy().equals(u.getId())){
                        us.add(u);
                    }
                }
                if(us.size()>0){
                    nomem.setVisibility(View.INVISIBLE);
                    RecyclerFriendsAdapter adapter=new RecyclerFriendsAdapter(us,context,R.layout.friend_request);
                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    nomem.setVisibility(View.VISIBLE);
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
        void onFragmentInteraction(Uri uri);
    }
}
