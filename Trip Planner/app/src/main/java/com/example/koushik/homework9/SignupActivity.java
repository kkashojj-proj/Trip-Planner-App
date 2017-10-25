package com.example.koushik.homework9;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class SignupActivity extends AppCompatActivity {
    EditText et_fname,et_lname,et_password,et_email;
    Button signup;
    RadioGroup rg;
    String gender;
    String photourl;
    CardView addPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_signup);

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        et_email = (EditText) findViewById(R.id.et_email );
        et_password = (EditText) findViewById(R.id.et_password );
        et_fname = (EditText) findViewById(R.id.et_fname );
        et_lname = (EditText) findViewById(R.id.et_lname );
        signup = (Button) findViewById(R.id.btn_signup);
        addPhoto= (CardView) findViewById(R.id.addphoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup.setEnabled(false);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
            }
        });
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.male) {
                    gender = "Male";
                }else if(checkedId == R.id.female){
                    gender = "Female";
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // need to post this data on firebase and take user to login page
                User u = new User();
                if(et_email.getText().toString()==null||et_lname.getText().toString()==null || gender == null ||et_password.getText().toString()==null){
                    Toast.makeText(SignupActivity.this,"Please check the details and sign up again",Toast.LENGTH_SHORT).show();
                }else{
                    u.setEmail(et_email.getText().toString());
                    u.setFirstNmame(et_fname.getText().toString());
                    u.setLastName(et_lname.getText().toString());
                    u.setGender(gender);
                    u.setImageUrl(photourl);
                    u.setPassword(et_password.getText().toString());
                    DatabaseReference push = databaseReference.push();
                    u.setId(push.getKey());
                    push.setValue(u);
                    Toast.makeText(SignupActivity.this,"Account Sucessfully Created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri imageUri = data.getData();
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            FirebaseStorage.getInstance().getReference("Images").child(UUID.randomUUID().toString()).putStream(inputStream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    photourl=downloadUrl.toString();
                    Toast.makeText(SignupActivity.this,"Uploaded",Toast.LENGTH_SHORT).show();
                    signup.setEnabled(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignupActivity.this,"Upload failed",Toast.LENGTH_SHORT).show();
                    signup.setEnabled(true);
                }
            });

        }
    }
}

