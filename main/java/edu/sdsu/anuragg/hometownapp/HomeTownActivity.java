package edu.sdsu.anuragg.hometownapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

public class HomeTownActivity extends AppCompatActivity {//implements NewUserActivity.LocationInfo{
    private static EditText mNickname, mPassword, mCity, mYear, mLatitude, mLongitude;
    private String selectedCountry, selectedState, selectedCity;
    private TextView mCountry, mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_town);
        HomeOptionsFragment homeOptionsFragment = new HomeOptionsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_home_town, homeOptionsFragment).commit();
        }

    public void createNewUser(View button){
        /*NewUserActivity newUserFragment = new NewUserActivity();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_home_town,newUserFragment)
                .addToBackStack("Hometown Options")
                .commit();*/
        Intent intent = new Intent(this,NewUserActivity.class);
        this.startActivity(intent);
    }

    public void findExistingUsers(View button){
        Log.i("IN Find","Check is in progress");
        Intent intent = new Intent(this,FindUsersActivity.class);

        this.startActivity(intent);
        /*FindUsersFragment findUsersFragment = new FindUsersFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_home_town,findUsersFragment)
                .addToBackStack("Hometown Options")
                .commit();*/
    }



}
