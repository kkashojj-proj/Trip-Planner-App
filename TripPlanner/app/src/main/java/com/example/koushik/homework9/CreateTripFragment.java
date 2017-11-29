package com.example.koushik.homework9;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateTripFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CreateTripFragment extends Fragment {
    EditText et_title,et_location;
    CardView addphoto;
    String photourl;
    Button btn_addtrip;
    Context context;

    private OnFragmentInteractionListener mListener;

    public CreateTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_create_trip, container, false);
        this.context=getContext();
        et_location= (EditText) v.findViewById(R.id.et_location);
        et_title= (EditText) v.findViewById(R.id.et_title);
        btn_addtrip= (Button) v.findViewById(R.id.btn_addtrip);
        btn_addtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location=et_location.getText().toString();
                String title=et_title.getText().toString();
                if(title.length()>0 && location.length()>0){
                    Geocoder geocoder=new Geocoder(context);
                    Address a=null;
                    TripLocation locationTrip=new TripLocation();
                    try {
                        List<Address> fromLocationName = geocoder.getFromLocationName(location, 1);
                        if(fromLocationName!=null && fromLocationName.size()>0){
                            a=fromLocationName.get(0);
                            locationTrip.setLocality(a.getLocality()+" ,"+a.getCountryCode());
                            locationTrip.setLat(a.getLatitude());
                            locationTrip.setLng(a.getLongitude());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    if(a!=null && a.getLocality()!=null && a.getCountryCode()!=null){
                        Trip t=new Trip();
                        t.setTitle(title);
                        t.setLocation(a.getLocality()+" ,"+a.getCountryCode());
                        t.addLocation(locationTrip);
                        if(photourl!=null){
                            t.setImageUrl(photourl);
                        }
                        mListener.addTrip(t);
                    }else{
                        Toast.makeText(getContext(),"No such location found",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,"Please enter all details",Toast.LENGTH_SHORT).show();
                }

            }
        });

        addphoto = (CardView) v.findViewById(R.id.addphoto);
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photourl=mListener.addPhoto(null);
            }
        });
        return v;
    }

    public void onImageReceived(String url){
        photourl=url;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
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
        void addTrip(Trip trip);
        String addPhoto(String photoOrNull);
    }
}
