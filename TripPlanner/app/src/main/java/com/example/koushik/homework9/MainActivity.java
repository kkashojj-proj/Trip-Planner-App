package com.example.koushik.homework9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int GSIGNIN=9001;
    RelativeLayout rel;
    int flag=0;
    Button btn_signup,btn_login;
    SignInButton signInButton;
    GoogleApiClient googleApiClient;
    TextView tv_email,tv_password;
    EditText et_email,et_password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String LOGIN="MYPREF";
    public static final String EMAIL="email";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences= getSharedPreferences(LOGIN,MODE_PRIVATE);
        editor=sharedPreferences.edit();

        btn_login= (Button) findViewById(R.id.btn_login);
        btn_signup= (Button) findViewById(R.id.btn_signup);
        rel= (RelativeLayout) findViewById(R.id.activity_main);
        tv_email= (TextView) findViewById(R.id.tv_email);
        tv_password= (TextView) findViewById(R.id.tv_password);
        et_email= (EditText) findViewById(R.id.et_email);
        et_password= (EditText) findViewById(R.id.et_password);
        //Log.d("demo",sharedPreferences.getString(EMAIL,null));

        if(sharedPreferences.getString(EMAIL,null)!=null){
            final String email=sharedPreferences.getString(EMAIL,null);
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot snap : children) {
                        User value = snap.getValue(User.class);
                        if(email.equalsIgnoreCase(value.getEmail())){
                            Intent intent=new Intent(MainActivity.this,UserActivity.class);
                            intent.putExtra("USER",value);
                            startActivity(intent);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_email.getText().toString().length()>0&&et_password.getText().toString().length()>0){
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot snap : children) {
                                User value = snap.getValue(User.class);
                                if(value.getEmail().equalsIgnoreCase(et_email.getText().toString())){
                                    if(value.getPassword().equals(et_password.getText().toString())){
                                        Intent intent=new Intent(MainActivity.this,UserActivity.class);
                                        intent.putExtra("USER",value);
                                        editor.putString(EMAIL,value.getEmail());
                                        editor.commit();
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(MainActivity.this,"Invalid credentials",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


        signInButton= (SignInButton) findViewById(R.id.GoogleSignin);
        GoogleSignInOptions gso=new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,GSIGNIN);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GSIGNIN){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){

            final GoogleSignInAccount account=result.getSignInAccount();
            final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");



            //tv.setText("Email :"+account.getEmail()+"\nDisplay name:"+account.getDisplayName()+"\nPhoto url: "+account.getPhotoUrl()+"\nGiven name: "+account.getGivenName()+"\nGender:");
            //after login successfullsve the profile data to firebase
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot snap :
                            children) {
                        User value = snap.getValue(User.class);
                        if(value.getEmail().equals(account.getEmail())){
                            flag=1;
                            User user=value;
                            Intent intent=new Intent(MainActivity.this,UserActivity.class);
                            intent.putExtra("USER",user);
                            editor.putString(EMAIL,user.getEmail());
                            editor.commit();
                            startActivity(intent);
                        }
                    }
                    if(flag==0){
                        User user= new User();
                        user.setEmail(account.getEmail());
                        user.setFirstNmame(account.getGivenName());
                        user.setLastName(account.getFamilyName());
                        if(account.getPhotoUrl()!=null){
                            user.setImageUrl(account.getPhotoUrl().toString());
                        }else{
                            user.setImageUrl(null);
                        }
                        DatabaseReference push = databaseReference.push();
                        user.setId(push.getKey());
                        push.setValue(user);
                        Intent intent=new Intent(MainActivity.this,UserActivity.class);
                        intent.putExtra("USER",user);
                        startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }else{
            Toast.makeText(MainActivity.this,"Invalid credentials",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
