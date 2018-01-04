package com.example.koushik.homework9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import static com.example.koushik.homework9.MainActivity.LOGIN;
import static com.example.koushik.homework9.MainActivity.googleApiClient;

public class UserActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,EditProfileFragment.OnFragmentInteractionListener,CreateTripFragment.OnFragmentInteractionListener,TripsFragment.OnFragmentInteractionListener,ItemTripCallback,NavigationView.OnNavigationItemSelectedListener,
        ItemClickCallback,ItemPeopleClickback,DiscoverFragment.OnFragmentInteractionListener,FriendsFragment.OnFragmentInteractionListener {

    public static User user;
    Fragment fragment;
    String url;
    String photourl;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Tryps");

        sharedPreferences= getSharedPreferences(LOGIN,MODE_PRIVATE);
        editor=sharedPreferences.edit();

        user= (User) getIntent().getExtras().getSerializable("USER");
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user=dataSnapshot.child(user.getId()).getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(user!=null){
            View header=navigationView.getHeaderView(0);
            TextView textView= (TextView) header.findViewById(R.id.tv_name);
            textView.setText(user.getFirstNmame()+" "+user.getLastName());
            ImageView img= (ImageView) header.findViewById(R.id.imageView);
            if(user.getImageUrl()!=null){
                Picasso.with(this).load(user.getImageUrl()).into(img);
            }
            TextView textView1= (TextView) header.findViewById(R.id.textView);
            textView1.setText(user.getEmail());
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.usercontent,new TripsFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackEntryCount > 0) {
                super.onBackPressed();  // write your code to switch between fragments.
            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.discover) {
            displayFragment(new DiscoverFragment());

        } else if (id == R.id.friends) {
            displayFragment(new FriendsFragment());
        } else if (id == R.id.trips) {
            displayFragment(new TripsFragment());
        } /*else if (id == R.id.nav_manage) {

        } */else if (id == R.id.edit) {
            fragment=new EditProfileFragment();
            displayFragment(fragment);
        } else if (id == R.id.logout) {
            editor.putString(MainActivity.EMAIL,null);
            editor.commit();
            MainActivity.signout();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.usercontent,fragment,"FRIENDS").commit();
    }

    @Override
    public void OnSubItemCLick(int position) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        User fromUser= DiscoverFragment.req_received.get(position);
        user.getRequestReceived().remove(fromUser.getId());
        fromUser.getRequestSent().remove(user.getId());
        user.addFriend(fromUser.getId());
        fromUser.addFriend(user.getId());
        databaseReference.child(user.getId()).setValue(user);
        databaseReference.child(fromUser.getId()).setValue(fromUser);
        displayFragment(new DiscoverFragment());
    }

    @Override
    public void OnContainerClick(int object) {

    }

    @Override
    public void POnContainerClick(int position) {

    }

    @Override
    public void POnSubItemCLick(int position) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        User touser= DiscoverFragment.remaining.get(position);
        touser.addRequestReceived(user.getId());
        user.addRequestSent(touser.getId());
        databaseReference.child(user.getId()).setValue(user);
        databaseReference.child(touser.getId()).setValue(touser);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSave() {
        final Intent intent =new Intent(this,UserActivity.class);
        DatabaseReference dbr=FirebaseDatabase.getInstance().getReference("Users");
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user1=dataSnapshot.child(UserActivity.user.getId()).getValue(User.class);
                intent.putExtra("USER",user1);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onTripCardClick(int position) {
        Intent i=new Intent(UserActivity.this,TripDetailsActivity.class);
        i.putExtra("Trip",TripsFragment.trips.get(position));
        startActivity(i);
    }

    @Override
    public void onTripJoinClick(int position) {
        Trip t=TripsFragment.trips.get(position);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Trips");
        t.addMember(UserActivity.user.getId());
        databaseReference.child(t.getTripId()).setValue(t);
        getSupportFragmentManager().beginTransaction().replace(R.id.usercontent,new TripsFragment()).commit();
    }

    @Override
    public void onCreateTripClick() {
        fragment=new CreateTripFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.usercontent,fragment).addToBackStack(null).commit();
    }

    @Override
    public void addTrip(Trip trip) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Trips");
        DatabaseReference ref=databaseReference.push();
        trip.setTripId(ref.getKey());
        trip.setCreatedBy(user.getId());
        trip.addMember(user.getId());
        ref.setValue(trip);
        getSupportFragmentManager().beginTransaction().replace(R.id.usercontent,new TripsFragment()).commit();
    }

    @Override
    public String addPhoto(String photo) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
        return photourl;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==100){
            photourl=null;
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
                        ((CreateTripFragment)fragment).onImageReceived(photourl);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserActivity.this,"Upload failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if(requestCode==200){
            url=null;
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
                        url=downloadUrl.toString();
                        //Toast.makeText(UserActivity.this,"Uploaded",Toast.LENGTH_SHORT).show();
                        ((EditProfileFragment)fragment).OnImageReceived(url);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserActivity.this,"Upload failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
