package edu.sdsu.anuragg.hometownapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by AnuragG on 18-Mar-17.
 */

public class UserDataModel implements Serializable{
    public int id;
    public String nickname;
    public String password;
    public Double longitude;
    public Double latitude;
    public int year;
    public String city;
    public String state;
    public String country;
    public String timestamp;
}
