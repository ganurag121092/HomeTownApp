package edu.sdsu.anuragg.hometownapp;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindUsersActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    Spinner countrySpinner, stateSpinner, yearSpinner, pageSizeSpinner;
    static String selectedCountry, selectedState, selectedYear, city, pageSize;
    public List<String> countries;
    public List<String> states;
    public List<String> years = new ArrayList<>();
    public List<String> pages = new ArrayList<>();
    ArrayAdapter<String> countryAdapter,stateAdapter,yearAdapter, pageAdapter;
    DataAccessLayer dataAccessLayer;
    public static ArrayList<UserDataModel> userList;
    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    FindUserMapFragment findUserMapFragment;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        bottomNavigation = (BottomNavigationView)findViewById(R.id.navigation);

        dataAccessLayer = new DataAccessLayer();

        Log.i("IN Useractivity state",selectedState!=null?selectedState:"NULL");
        Log.i("IN Useractivity city",city!=null?city:"NULL");
        countries = new ArrayList<>();
        states = new ArrayList<>();
        pages = new ArrayList<>();
        years.add("Select Year");
        for(int i=1970;i<=2017;i++){
            years.add(Integer.toString(i));
        }
        pages = Arrays.asList("25","50","75","100");
        countries = dataAccessLayer.getCountriesList(this);
        countries.add(0,"Select Country");

        states.add("Select State");

        countrySpinner = (Spinner) this.findViewById(R.id.countrylist_spinner);
        stateSpinner = (Spinner) this.findViewById(R.id.statelist_spinner);
        yearSpinner = (Spinner) this.findViewById(R.id.year_spinner);
        pageSizeSpinner = (Spinner) this.findViewById(R.id.pagesize_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        countryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,countries);
        stateAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,states);
        yearAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,years);
        pageAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,pages);
// Specify the layout to use when the list of choices appears
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        countrySpinner.setAdapter(countryAdapter);
        stateSpinner.setAdapter(stateAdapter);
        yearSpinner.setAdapter(yearAdapter);
        pageSizeSpinner.setAdapter(pageAdapter);
        pageSizeSpinner.setVisibility(View.INVISIBLE);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedYear = years.get(position);
                if(position!=0) {
                    Log.i("selected Year", selectedYear);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pageSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                pageSize = pages.get(position);
                Log.i("selected Page", pageSize);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedState = states.get(position);
                if(position!=0) {
                    Log.i("selected State", selectedState);
                    if(selectedState.contains(" ")){
                        String[] s = selectedState.split(" ");

                        selectedState = "";
                        for(int i=0;i<s.length;i++){
                            if(i==0){
                                selectedState = s[i];
                            }else {
                                selectedState = selectedState + "%20" + s[i];
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Toast.makeText(getBaseContext(), "Please Press Bottom Navigation For Viewing List/Map", Toast.LENGTH_LONG).show();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_list:
                        if(selectedCountry=="Select Country"){
                            selectedCountry = null;
                        }
                        if(selectedYear=="Select Year"){
                            selectedYear = null;
                        }
                        if(selectedState=="Select State"){
                            selectedState = null;
                        }
                        if(selectedCountry!=null) {
                            if(selectedState!=null) {
                                if (selectedCountry != null && selectedState != null && selectedYear != null) {
                                    url = "http://bismarck.sdsu.edu/hometown/users?country=" + selectedCountry + "&state=" + selectedState + "&year=" + String.valueOf(selectedYear);
                                } else if (selectedCountry != null && selectedState != null) {
                                    url = "http://bismarck.sdsu.edu/hometown/users?country=" + selectedCountry + "&state=" + selectedState;
                                }
                                getUserList("menu_list",url);
                            }
                            else{
                                if(selectedYear!=null) {
                                    url = "http://bismarck.sdsu.edu/hometown/users?country=" + selectedCountry + "&year=" + String.valueOf(selectedYear);
                                }
                                else {
                                    url = "http://bismarck.sdsu.edu/hometown/users?country=" + selectedCountry;
                                }
                                getUserList("menu_list",url);
                                //Toast.makeText(getBaseContext(), "Please Select State", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            if(selectedYear!=null){
                                url = "http://bismarck.sdsu.edu/hometown/users?year="+selectedYear;
                            }
                            else{
                                url = "http://bismarck.sdsu.edu/hometown/users";
                            }
                            getUserList("menu_list",url);
                            //Toast.makeText(getBaseContext(), "Please Select Country", Toast.LENGTH_LONG).show();
                        }


                        return true;
                    case R.id.menu_map:
                        if(recyclerView!=null){
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                        Toast.makeText(getBaseContext(), "Please Wait Till Map Loads", Toast.LENGTH_LONG).show();
                        if(selectedCountry=="Select Country"){
                            selectedCountry = null;
                        }
                        if(selectedYear=="Select Year"){
                            selectedYear = null;
                        }
                        if(selectedState=="Select State"){
                            selectedState = null;
                        }
                        if(selectedCountry!=null) {
                            if(selectedState!=null) {
                                if (selectedCountry != null && selectedState != null && selectedYear != null) {
                                    url = "http://bismarck.sdsu.edu/hometown/users?country=" + selectedCountry + "&state=" + selectedState + "&year=" + String.valueOf(selectedYear);
                                } else if (selectedCountry != null && selectedState != null) {
                                    url = "http://bismarck.sdsu.edu/hometown/users?country=" + selectedCountry + "&state=" + selectedState;
                                }
                                getUserList("map_view",url);
                            }
                            else{
                                if(selectedYear!=null) {
                                    url = "http://bismarck.sdsu.edu/hometown/users?country=" + selectedCountry + "&year=" + String.valueOf(selectedYear);
                                }
                                else {
                                    url = "http://bismarck.sdsu.edu/hometown/users?country=" + selectedCountry;
                                }
                                getUserList("map_view",url);
                                //Toast.makeText(getBaseContext(), "Please Select State", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            if(selectedYear!=null){
                                url = "http://bismarck.sdsu.edu/hometown/users?year="+selectedYear;
                                getUserList("map_view",url);
                            }
                            else{
                               // url = "http://bismarck.sdsu.edu/hometown/users";
                                //getUserList("map_view",url);
                                getAllUsersList();
                            }
                            //Toast.makeText(getBaseContext(), "Please Select Country", Toast.LENGTH_LONG).show();
                        }
                        return true;
                }
                return true;
            }
        });


    }

    public ArrayList<UserDataModel> getUserList(final String view_option, String usersFetchUrl){
        userList = new ArrayList<>();
        Log.d("Final tech URL", usersFetchUrl);

        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        if (response != null) {
                            JSONObject person = (JSONObject) response.get(i);
                            UserDataModel model = new UserDataModel();
                            model.id = person.getInt("id");
                            model.nickname = person.getString("nickname");
                            model.longitude = person.getDouble("longitude");
                            model.latitude = person.getDouble("latitude");
                            model.year = person.getInt("year");
                            model.city = person.getString("city");
                            model.state = person.getString("state");
                            model.country = person.getString("country");
                            model.timestamp = person.getString("time-stamp");
                            userList.add(model);
                            Log.d("Data User List rew", userList.get(i).toString() + String.valueOf(userList.size()));
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userList", userList);
                    bundle.putString("selectedCountry",selectedCountry);
                    bundle.putString("selectedState",selectedState);
                    bundle.putString("selectedCity",city);
                            if(userList.size()!=0) {
                                if (view_option == "map_view") {
                                    Log.d("Map View SIZE", String.valueOf(userList.size()));
                                    findUserMapFragment = new FindUserMapFragment();
                                    findUserMapFragment.setArguments(bundle);
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.activity_find_users, findUserMapFragment)
                                           // .addToBackStack("Map Fragment")
                                            .commit();
                                } else if(view_option == "menu_list"){
                                    if(recyclerView!=null){
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }
                                    if(findUserMapFragment!=null){
                                    getSupportFragmentManager().beginTransaction().remove(findUserMapFragment).commit();}
                                    Log.d("User List SIZE", String.valueOf(userList.size()));
                                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                                    userListAdapter = new UserListAdapter(userList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(userListAdapter);

                                }
                            }
                            else {
                                if (view_option == "map_view") {
                                    getSupportFragmentManager().beginTransaction().remove(findUserMapFragment).commit();
                                }else if(view_option == "menu_list"){
                                    if(recyclerView!=null) {
                                        recyclerView.setVisibility(View.INVISIBLE);
                                    }
                                }
                                Toast.makeText(getBaseContext(), "Please Select Other Country and State", Toast.LENGTH_LONG).show();
                            }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };



        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };

        JsonArrayRequest getRequest = new JsonArrayRequest(usersFetchUrl, success, failure);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getRequest);
        return userList;
    }

    public void getAllUsersList(){
        AllUsersMap allUsersMap = new AllUsersMap();
        allUsersMap.execute();
    }

    private class AllUsersMap extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            userList = new ArrayList<>();
            Log.d("UserActivity", "IN FIND USER ACT");
            String usersFetchUrl = "http://bismarck.sdsu.edu/hometown/users";
            Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                           // if (response != null) {
                                JSONObject person = (JSONObject) response.get(i);
                                UserDataModel model = new UserDataModel();
                                model.id = person.getInt("id");
                                model.nickname = person.getString("nickname");
                                model.longitude = person.getDouble("longitude");
                                model.latitude = person.getDouble("latitude");
                                model.year = person.getInt("year");
                                model.city = person.getString("city");
                                model.state = person.getString("state");
                                model.country = person.getString("country");
                                model.timestamp = person.getString("time-stamp");
                                userList.add(model);
                                Log.d("Data User List rew", userList.get(i).toString() + String.valueOf(userList.size()));
                            //}
                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userList", userList);
                        bundle.putString("selectedCountry",selectedCountry);
                        bundle.putString("selectedState",selectedState);
                        bundle.putString("selectedCity",city);
                        if(userList.size()!=0) {
                                Log.d("Map View SIZE", String.valueOf(userList.size()));
                                findUserMapFragment = new FindUserMapFragment();
                                findUserMapFragment.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.activity_find_users, findUserMapFragment)
                                        //.addToBackStack("Map Fragment")
                                        .commit();

                        }
                        else {
                            Toast.makeText(getBaseContext(), "Please Select Other Country and State", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            };



            Response.ErrorListener failure = new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Log.d("rew", error.toString());
                }
            };

            JsonArrayRequest getRequest = new JsonArrayRequest(usersFetchUrl, success, failure);
            RequestQueue queue = Volley.newRequestQueue(getBaseContext());
            queue.add(getRequest);
        return null;
        }

        @Override
        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground
            // Good for toggling visibility of a progress indicator
        }

        @Override
        protected void onPostExecute(Void result) {
        }

    }
}
