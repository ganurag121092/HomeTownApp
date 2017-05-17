package edu.sdsu.anuragg.hometownapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnuragG on 18-Mar-17.
 */

public class DataAccessLayer {
    List<String> countryList, stateList;
    ArrayList<UserDataModel> userList;
    boolean userExisting = false;
    public List<String> getCountriesList(Context context) {
        Log.i("rew", "Start");
        countryList = new ArrayList<>();

        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                Log.d("rew", response.toString() + response.length());

                try {
                    for (int i = 0; i < response.length(); i++) {
                        if (response != null) {
                            countryList.add(response.getString(i));
                        }
                    }
                }
                catch(org.json.JSONException e){
                    throw new RuntimeException(e);
                }
            }
        };



        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };
        String url ="http://bismarck.sdsu.edu/hometown/countries";
        JsonArrayRequest getRequest = new JsonArrayRequest( url, success, failure);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(getRequest);
        return countryList;
    }

    public List<String> getStatesList(Context context, String countryName) {
        Log.i("rew", "State Start");
        stateList = new ArrayList<>();
        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                Log.d("rew", response.toString() + response.length());

                try {
                    for (int i = 0; i < response.length(); i++) {
                        if (response != null) {
                            stateList.add(response.getString(i));
                        }
                    }
                }
                catch(org.json.JSONException e){
                    throw new RuntimeException(e);
                }

            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };
        String url ="http://bismarck.sdsu.edu/hometown/states?country=" + countryName;
        JsonArrayRequest getRequest = new JsonArrayRequest( url, success, failure);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(getRequest);
        return stateList;
    }

    public void postUserData(UserDataModel data, Context context){

        JSONObject userdata = new JSONObject();
        try {
            userdata.put("nickname", data.nickname);
            userdata.put("password", data.password);
            userdata.put("country", data.country);
            userdata.put("state", data.state);
            userdata.put("city", data.city);
            userdata.put("year", data.year);
            userdata.put("latitude", data.latitude);
            userdata.put("longitude", data.longitude);
        } catch (JSONException error) {
            Log.e("rew", "JSON eorror", error);
            return;
        }
        Response.Listener<JSONObject> success = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//Process response here
                Log.i("rew", response.toString());
            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("rew", "post fail " + new String(error.networkResponse.data));
            }
        };
        String url = "http://bismarck.sdsu.edu/hometown/adduser";
        JsonObjectRequest postRequest = new JsonObjectRequest(url, userdata, success, failure);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);


    }

    //Defining the interface which is implemented by the Other Activity class
    public interface UserList{
        public void getUserList(ArrayList<UserDataModel> users);
    }

    public boolean isUserExists(String nickName, Context context){
        Log.d("nickanme checking", nickName);
        Response.Listener<String> success = new Response.Listener<String>() {

            public void onResponse(String response) {
                try {
                    Log.d("rew", response);
                    userExisting = Boolean.parseBoolean(response);
                }
                catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        };



        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };
        String url ="http://bismarck.sdsu.edu/hometown/nicknameexists?name="+nickName;
        StringRequest getRequest = new StringRequest( url, success, failure);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(getRequest);
        return userExisting;
    }

}
