package com.xplore24.courier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.xplore24.courier.Utils.LoaderDialog;
import com.xplore24.courier.Utils.Utils;
import com.xplore24.courier.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Signup extends AppCompatActivity {

    TextInputLayout  regPhoneNo1, regPassword1,regPasswordTwo1;
    TextInputEditText regPhoneNo, regPassword,regPasswordTwo;
    String ph,pass;

    Button getOtp, regToLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        regPhoneNo = findViewById(R.id.reg_phoneNo);
        regPhoneNo1 = findViewById(R.id.reg_phoneNo1);
        regPassword = findViewById(R.id.reg_password);
        regPassword1 = findViewById(R.id.reg_password1);
        regPasswordTwo = findViewById(R.id.reg_passwordtwo);
        regPasswordTwo1 = findViewById(R.id.reg_passwordtwo1);



        getOtp = findViewById(R.id.getotp);
        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkConnected()){
                    Toasty.info(Signup.this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT, true).show();


                }
                else if(regPhoneNo.getText().toString().length()!=11){
                    regPhoneNo1.setError("Invalid Phone Number");
                }else if(regPassword.getText().toString().length()==0){
                    regPassword1.setError("Insert Password");

                }else if(regPasswordTwo.getText().toString().length()==0){
                    regPasswordTwo1.setError("Insert Password");

                }else if(!regPassword.getText().toString().equals(regPasswordTwo.getText().toString())){
                    regPasswordTwo1.setError("Password Not Matched");

                }else  {

                    ph=regPhoneNo.getText().toString().trim();
                    pass=regPasswordTwo.getText().toString().trim();

                    regPassword1.setErrorEnabled(false);
                    regPasswordTwo1.setErrorEnabled(false);
                    regPhoneNo1.setErrorEnabled(false);
                    checkphone();
                }


            }
        });
        regToLoginBtn = findViewById(R.id.reg_login_btn);
        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Signup.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkphone() {

        LoaderDialog loaderDialog=new LoaderDialog(this);
        loaderDialog.create();
        loaderDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fastfly.com.bd/api/auth/forget/password/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //converting response to json object
                                JSONObject obj = new JSONObject(response);

                             if(!obj.getBoolean("successMessage")){
                                 loaderDialog.cancel();
                                 Intent intent=new Intent(Signup.this,OTPVerification.class);
                                 intent.putExtra("Phone",ph);
                                 intent.putExtra("Pass",pass);
                                 startActivity(intent);
                                 finish();





                                 }







                            //creating a new user object


                            //storing the user in shared preferences
                            //starting the profile activity


                        } catch (JSONException e) {
                            Toasty.success(getApplicationContext(), "Phone Number Already Exist", Toast.LENGTH_LONG, true).show();
                            loaderDialog.cancel();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toasty.success(getApplicationContext(), "Phone Number Already Exist", Toast.LENGTH_LONG, true).show();
                        loaderDialog.cancel();



                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                                Toasty.error(getApplicationContext(), obj.getString("errorMessage"), Toast.LENGTH_SHORT, true).show();

                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }





                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", ph);
                return params;
            }

        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Signup.this,Login.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}