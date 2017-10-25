package com.example.koushik.homework9;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    EditText et_fname,et_lname,et_email;
    CardView card_edit,card_delete,card_save;
    RadioGroup radioGroup;
    RadioButton male,female;
    ImageView photo;
    String photourl;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String gender;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context context;

    private OnFragmentInteractionListener mListener;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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

        View v=inflater.inflate(R.layout.fragment_edit_profile, container, false);
        context=getContext();

        photo= (ImageView) v.findViewById(R.id.photo);
        et_email= (EditText) v.findViewById(R.id.et_email);
        et_lname= (EditText) v.findViewById(R.id.et_lname);
        et_fname= (EditText) v.findViewById(R.id.et_fname);
        radioGroup= (RadioGroup) v.findViewById(R.id.radioGroup);
        male= (RadioButton) v.findViewById(R.id.male);
        female= (RadioButton) v.findViewById(R.id.female);
        card_edit= (CardView) v.findViewById(R.id.card_edit);
        card_delete= (CardView) v.findViewById(R.id.card_delete);
        card_save= (CardView) v.findViewById(R.id.card_save);
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
        et_fname.setText(UserActivity.user.getFirstNmame());
        et_lname.setText(UserActivity.user.getLastName());
        et_email.setText(UserActivity.user.getEmail());
        gender = UserActivity.user.getGender();
        if(UserActivity.user.getImageUrl()!=null){
            photourl=UserActivity.user.getImageUrl();
            Picasso.with(context).load(UserActivity.user.getImageUrl()).into(photo);
        }else{
            card_delete.setEnabled(false);
        }
        if(UserActivity.user.getGender()!=null){
            if(UserActivity.user.getGender().equalsIgnoreCase("male")){
                male.setChecked(true);
            }else if(UserActivity.user.getGender().equalsIgnoreCase("female")){
                female.setChecked(true);
            }
        }
         et_email.setEnabled(false);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i==R.id.male){
                    gender="Male";
                }
                else if(i==R.id.female){
                    gender="Female";
                }
            }
        });
        card_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                card_save.setEnabled(false);
                getActivity().startActivityForResult(intent, 200);
            }
        });
        card_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Photo removed",Toast.LENGTH_SHORT).show();
                if(photourl!=null){
                    photourl=null;
                }
                Picasso.with(context).load(R.mipmap.dummy_image).into(photo);
            }
        });

        card_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname=et_fname.getText().toString();
                String lname=et_lname.getText().toString();
                String Email=et_email.getText().toString();
                String radio=gender;

                if(fname.length()>0&&lname.length()>0&&Email.length()>0&&radio.length()>0){
                    DatabaseReference dref= FirebaseDatabase.getInstance().getReference("Users");
                    UserActivity.user.setFirstNmame(fname);
                    UserActivity.user.setLastName(lname);

                    UserActivity.user.setImageUrl(photourl);
                    UserActivity.user.setGender(radio);
                    dref.child(UserActivity.user.getId()).setValue(UserActivity.user);
                    Toast.makeText(context,"Prfile updated",Toast.LENGTH_SHORT).show();

                    mListener.onSave();
                }else{
                    Toast.makeText(context,"Please fill all the details",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void OnImageReceived(String url){
        //Toast.makeText(context,"Please",Toast.LENGTH_SHORT).show();
        card_save.setEnabled(true);
        if(url!=null){
            photourl=url;
            Picasso.with(context).load(url).into(photo);
        }

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
        void onSave();
    }
}
