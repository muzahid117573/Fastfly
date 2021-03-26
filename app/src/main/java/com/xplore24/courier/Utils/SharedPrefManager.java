package com.xplore24.courier.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.xplore24.courier.Login;
import com.xplore24.courier.Model.OrderCancel;
import com.xplore24.courier.Model.OrderComplete;
import com.xplore24.courier.Model.OrderPlaced;
import com.xplore24.courier.Model.OrderShipment;
import com.xplore24.courier.Model.OrderStatus;
import com.xplore24.courier.Model.ProfileModel;
import com.xplore24.courier.Model.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_NAME = "name";
    private static final String KEY_COMNAME = "companyName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_COMADDRESS = "address";
    private static final String KEY_FBLINK = "companyFbLink";
    private static final String KEY_PLACED = "placed";
    private static final String KEY_CANCEL = "cancel";
    private static final String KEY_INSHIP = "inshipment";
    private static final String KEY_COMPLETE = "complete";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, user.getToken());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, null) != null;
    }



    public void profileData(ProfileModel profileModel){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, profileModel.getName());
        editor.putString(KEY_COMNAME, profileModel.getCompanyName());
        editor.putString(KEY_COMADDRESS, profileModel.getAddress());
        editor.putString(KEY_EMAIL, profileModel.getEmail());
        editor.putString(KEY_FBLINK, profileModel.getCompanyFbLink());
        editor.apply();

    }

    public void statusplaced(OrderPlaced orderPlaced){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PLACED, orderPlaced.getPlaced());


        editor.apply();

    }

    public void statusinship(OrderShipment orderShipment){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_INSHIP, orderShipment.getInshipment());


        editor.apply();

    }

    public void statuscomplete(OrderComplete orderComplete){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_COMPLETE, orderComplete.getComplete());


        editor.apply();

    }

    public void statuscancel(OrderCancel orderCancel){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_CANCEL, orderCancel.getCancel());

        editor.apply();

    }


    public boolean isprofileDataExist() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null) != null;

    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_TOKEN, null),
                sharedPreferences.getString(KEY_PHONE, null));
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }
}