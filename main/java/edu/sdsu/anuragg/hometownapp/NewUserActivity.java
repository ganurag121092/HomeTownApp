package edu.sdsu.anuragg.hometownapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class NewUserActivity extends AppCompatActivity {
    private EditText mNickname, mPassword, mCity, mLatitude, mLongitude;
    Spinner countrySpinner, stateSpinner, yearSpinner;
    String selectedCountry, selectedState, selectedYear, nickname, password, city, selectedLat, selectedLon;
    public List<String> countries,states;
    public List<String> years = new ArrayList<>();
    ArrayAdapter<String> countryAdapter,stateAdapter,yearAdapter;
    DataAccessLayer dataAccessLayer;
    private static final int INTENT_LOCATION_REQUEST = 125;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        dataAccessLayer = new DataAccessLayer();
        countries = new ArrayList<>();
        states = new ArrayList<>();
        states.add("Select State");

        years.add("Select Year");
        for (int i = 1970; i <= 2017; i++) {
            years.add(Integer.toString(i));
        }

        countries = dataAccessLayer.getCountriesList(this);
        countries.add(0, "Select Country");


        mNickname = (EditText) this.findViewById(R.id.nicknametextid);
        mPassword = (EditText) this.findViewById(R.id.passwordtextid);
        mCity = (EditText) this.findViewById(R.id.citytextid);
        mLatitude = (EditText) this.findViewById(R.id.lattextid);
        mLongitude = (EditText) this.findViewById(R.id.lontextid);
        mNickname.setSelection(mNickname.getText().length());


        Button resetButton = (Button) this.findViewById(R.id.resetid);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Reset cliked", "RESET clicked");
                clearFields();
                /*mNickname.setText("");
                mPassword.setText("");
                mCity.setText("");
                mLatitude.setText("");
                mLongitude.setText("");
                countrySpinner.setSelection(0);
                stateSpinner.setSelection(0);
                yearSpinner.setSelection(0);
                mNickname.requestFocus();
                selectedCountry = "Select Country";
                selectedState = "Select State";
                selectedYear = "Select Year";*/
            }
        });


            countrySpinner = (Spinner) this.findViewById(R.id.countrylist_spinner);
        stateSpinner = (Spinner) this.findViewById(R.id.statelist_spinner);
        yearSpinner = (Spinner) this.findViewById(R.id.year_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        countryAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, countries);
        stateAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, states);
        yearAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, years);
// Specify the layout to use when the list of choices appears
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        countrySpinner.setAdapter(countryAdapter);
        stateSpinner.setAdapter(stateAdapter);
        yearSpinner.setAdapter(yearAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedCountry = countries.get(position);
                if(position!=0) {
                    Log.i("selected Country", selectedCountry);
                    states = dataAccessLayer.getStatesList(getBaseContext(),selectedCountry);
                    states.add(0,"Select State");
                    stateAdapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item,states);
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    stateSpinner.setAdapter(stateAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCountry = null;
            }
        });

        if(selectedCountry == "Select Country"){
            selectedCountry= null;
        }
        if(selectedState == "Select State"){
            selectedState = null;
        }

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedState = states.get(position);
                if(position!=0) {
                    Log.i("selected State", selectedState);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedState = null;
            }
        });

        if(selectedYear == "Select Year"){
            selectedYear = null;
        }

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedYear = years.get(position);
                if(position!=0) {
                    Log.i("selected State", selectedYear);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedYear = null;
            }
        });
    }

    public void clearFields(){
        mNickname.setText("");
        mPassword.setText("");
        mCity.setText("");
        mLatitude.setText("");
        mLongitude.setText("");
        countrySpinner.setSelection(0);
        stateSpinner.setSelection(0);
        yearSpinner.setSelection(0);
        mNickname.requestFocus();
        selectedCountry = "Select Country";
        selectedState = "Select State";
        selectedYear = "Select Year";
    }

    public void getCoordinates(View button){
        if (selectedCountry != "Select Country") {
            Log.i("Country Value", selectedCountry);
            if (selectedState != "Select State") {
                if (mCity.getText().toString() != null) {
                    city = mCity.getText().toString();
                }
                Intent userHelper= new Intent(this,UserHelperActivity.class);
                userHelper.putExtra("Country",selectedCountry);
                userHelper.putExtra("State",selectedState);
                userHelper.putExtra("City",city);
                startActivityForResult(userHelper,INTENT_LOCATION_REQUEST);
            } else {
                Toast.makeText(getBaseContext(), "Please Select State", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getBaseContext(), "Please Select Country", Toast.LENGTH_LONG).show();
        }

    }

    public void saveUserData(View button){
        boolean isValid = true;
        nickname = mNickname.getText().toString();
        password = mPassword.getText().toString();
        city = mCity.getText().toString();
        if(selectedYear == "Select Year"){
            selectedYear = null;
        }
        Log.i("Final Name", nickname);
        Log.i("Final Password", password);
        Log.i("Final Country", selectedCountry);
        Log.i("Final State", selectedState);
        Log.i("Final City", city);
        if(selectedYear!=null)
        Log.i("Final Yerar", selectedYear);
        Log.i("Final Coordinates", selectedLat+ " " + selectedLon);
        if(TextUtils.isEmpty(nickname)){
            mNickname.setError("Nickname Required");
        }
        if(dataAccessLayer.isUserExists(mNickname.getText().toString(), getBaseContext())){
            //Toast.makeText(getBaseContext(), "Please Enter new Nickname", Toast.LENGTH_LONG).show();
            mNickname.setError("Nickname Already Exists");
            isValid = false;
        }
        if(password.length()<3){
            mPassword.setError("Must be more than 3 characters");
            isValid = false;
        }
        if(TextUtils.isEmpty(password)){
            //Toast.makeText(getBaseContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
            mPassword.setError("Password Required");
            isValid = false;
        }

        if(selectedCountry==null){
            Toast.makeText(getBaseContext(), "Please Select Country", Toast.LENGTH_LONG).show();
            isValid = false;
        }

        if(selectedState==null){
            Toast.makeText(getBaseContext(), "Please Select State", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(TextUtils.isEmpty(city)){
            mCity.setError("City Name Required");
            //Toast.makeText(getBaseContext(), "Please Enter City", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(selectedYear==null){
            Toast.makeText(getBaseContext(), "Please Select School Year", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(TextUtils.isEmpty(selectedLat)|| TextUtils.isEmpty((selectedLon))){
            Toast.makeText(getBaseContext(), "Please Select Location", Toast.LENGTH_LONG).show();
            isValid = false;
        }

        if (isValid) {
            Log.i("Data Validity","All enter data Valid");
            UserDataModel userData = new UserDataModel();
            userData.nickname = mNickname.getText().toString();
            userData.password = mPassword.getText().toString();
            userData.country = selectedCountry;
            userData.state = selectedState;
            userData.city = city;
            userData.year = selectedYear!=null?Integer.parseInt(selectedYear):null;
            userData.latitude = Double.parseDouble(mLatitude.getText().toString());
            userData.longitude = Double.parseDouble(mLongitude.getText().toString());
            dataAccessLayer.postUserData(userData,getBaseContext());
            clearFields();
            Toast.makeText(getBaseContext(), "User Data Saved", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case INTENT_LOCATION_REQUEST:
                    //Setting Selected Coordinates to the Textview in New User Activity
                    Log.i("LAT_LON", data.getStringExtra("Latitude"));
                    Log.i("LAT_LON", data.getStringExtra("Longitude"));
                    selectedLat = data.getStringExtra("Latitude");
                    selectedLon = data.getStringExtra("Longitude");
                    mLatitude.setText(selectedLat.substring(0,7));
                    mLongitude.setText(selectedLon.substring(0,7));
                    break;
            }
        }
    }
}
