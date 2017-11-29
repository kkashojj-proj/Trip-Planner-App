package com.example.koushik.homework9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static com.example.koushik.homework9.TripDetailsActivity.trip;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TripDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TripDetailsFragment extends Fragment {
    CardView members,chatroom,locationCard;
    TextView title,location;
    ImageView iv;

    private OnFragmentInteractionListener mListener;

    public TripDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_trip_details, container, false);
        title = (TextView) v.findViewById(R.id.textView4);
        location = (TextView) v.findViewById(R.id.textView5);
        locationCard= (CardView) v.findViewById(R.id.location);
        chatroom= (CardView) v.findViewById(R.id.chatroom);
        iv= (ImageView) v.findViewById(R.id.imageView3);
        members= (CardView) v.findViewById(R.id.members);
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
        if(trip!=null){
            title.setText(trip.getTitle());
            location.setText(trip.getLocation());
            if(trip.getImageUrl()!=null){
                Picasso.with(getContext()).load(trip.getImageUrl()).into(iv);
            }
        }

        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMembersClick();
            }
        });

        chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(trip.getMembers().contains(UserActivity.user.getId())){
                    mListener.onChatRoomClick();
                }else{
                    Toast.makeText(getContext(),"You are not a member. Please join",Toast.LENGTH_SHORT).show();
                }
            }
        });

        locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(trip.getMembers().contains(UserActivity.user.getId())){
                    mListener.onLocationClick();
                }else{
                    Toast.makeText(getContext(),"You are not a member. Please join",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMembersClick();
        void onChatRoomClick();
        void onLocationClick();
    }
}
