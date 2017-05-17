package edu.sdsu.anuragg.hometownapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class UserHelperActivity extends AppCompatActivity implements MapFragment.SelectedLocation{
    private String selectedCountry, selectedState, city,selectedLatitude,selectedLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_helper);
        Bundle bundle = getIntent().getExtras();
        selectedCountry = bundle.getString("Country");
        selectedState = bundle.getString("State");
        city = bundle.getString("City");

        bundle = new Bundle();
        bundle.putString("Country", selectedCountry);
        bundle.putString("State", selectedState);
        bundle.putString("City", city);

        MapFragment newMapFragment = new MapFragment();
        newMapFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_user_helper, newMapFragment)
                .commit();
    }

    public void getLatLon(String latitude, String longitude){
        selectedLatitude = latitude;
        selectedLongitude = longitude;
    }

    public void setLocationCoordinates(View button){
        if(selectedLatitude!=null&&selectedLongitude!=null) {
            Intent returnData = getIntent();
            Log.i("selectedLat,Longitude",selectedLatitude + " " + selectedLongitude);
            returnData.putExtra("Latitude", selectedLatitude);
            returnData.putExtra("Longitude", selectedLongitude);
            setResult(RESULT_OK, returnData);
            finish();
        }
        else{
            Toast.makeText(this,"Please Place Marker",Toast.LENGTH_SHORT).show();
        }
    }
}
