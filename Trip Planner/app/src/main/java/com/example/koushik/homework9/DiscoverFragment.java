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

import static com.example.koushik.homework9.UserActivity.user;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiscoverFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    RecyclerView rv_pending,rv_all;
    public static ArrayList<User> remaining, req_received,friends,all;
    int f1=0,f2=0;
    TextView tv2,tv3;
    RecyclerPeopleAdapter adapter1;
    RecyclerPendingAdapter adapter;
    Context context;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_discover, container, false);
        tv2= (TextView) v.findViewById(R.id.textView2);
        tv3= (TextView) v.findViewById(R.id.textView3);
        rv_pending= (RecyclerView) v.findViewById(R.id.rv_pending);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //rv_pending = (RecyclerView) getActivity().findViewById(R.id.rv_pending);
        rv_all= (RecyclerView) getActivity().findViewById(R.id.rv_all);
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(f1==0){
                    f1=1;
                    context=getContext();
                }
                remaining =new ArrayList<>();
                all=new ArrayList<>();
                friends=new ArrayList<>();
                req_received =new ArrayList<>();
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    User p=d.getValue(User.class);
                    all.add(p);
                }
                user=dataSnapshot.child(user.getId()).getValue(User.class);
                for(String id:user.getRequestReceived()){
                    req_received.add(dataSnapshot.child(id).getValue(User.class));
                }

                if(req_received.size()>0){
                    tv2.setVisibility(View.INVISIBLE);
                    adapter=new RecyclerPendingAdapter(req_received,context,R.layout.friend_request);
                    rv_pending.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv_pending.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    tv2.setVisibility(View.VISIBLE);
                }

                remaining=getPeopleYouMayKnow();

                if(remaining.size()>0){
                    tv3.setVisibility(View.INVISIBLE);
                    adapter1=new RecyclerPeopleAdapter(remaining,context,R.layout.friend_request);
                    rv_all.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv_all.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();

                }else{
                    tv3.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<User> getPeopleYouMayKnow(){
        ArrayList<User> output = new ArrayList<>();
        for (User u: all) {
            int flag =0;
            for(String id:user.friends){
                if(u.getId().equals(id)){
                    flag=1;
                }
            }
            for(String id:user.requestReceived){
                if(u.getId().equals(id)){
                    flag=1;
                }
            }
            for(String id:user.requestSent){
                if(u.getId().equals(id)){
                    flag=1;
                }
            }
            if(u.getId().equals(user.getId())){
                flag=1;
            }
            if(flag==0){
                output.add(u);
            }
        }
        return output;
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
