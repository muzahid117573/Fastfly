package com.xplore24.courier;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xplore24.courier.Utils.LoaderDialog;

import com.xplore24.courier.Utils.Utils;
import com.xplore24.courier.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ForgetPhone extends AppCompatActivity {
    private Button phone_btn;

    private TextInputLayout phonenumber1, password1;
    private TextInputEditText phonenumber, password;


   private TextInputLayout   regPassword1,regPasswordTwo1;
   private TextInputEditText  regPassword,regPasswordTwo;
   private String ph,pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_forget_phone);

        Utils.blackIconStatusbar(ForgetPhone.this,R.color.backgroundlight);


        regPassword = findViewById(R.id.for1password);
        regPassword1 = findViewById(R.id.for1password1);
        regPasswordTwo = findViewById(R.id.for2password);
        regPasswordTwo1 = findViewById(R.id.for2password1);


        phone_btn = findViewById(R.id.restphone_btn);



        phonenumber = findViewById(R.id.forphonenumber);

        phonenumber1 = findViewById(R.id.forphonenumber1);


        phone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkConnected()){
                    Toasty.info(ForgetPhone.this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT, true).show();


                }else {
                    userLogin();
                }

            }
        });



    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void userLogin() {




        if (phonenumber.getText().toString().length()!=11) {
            phonenumber1.setError("Insert Valid Number");
            phonenumber1.requestFocus();
        }   else if(regPassword.getText().toString().length()==0){
            regPassword1.setError("Insert Password");

        }else if(regPasswordTwo.getText().toString().length()==0){
            regPasswordTwo1.setError("Insert Password");

        }else if(!regPassword.getText().toString().equals(regPasswordTwo.getText().toString())){
            regPasswordTwo1.setError("Password Not Matched");

        }else {
            phonenumber1.setErrorEnabled(false);
            pass=regPasswordTwo.getText().toString().trim();
            String phonenum = phonenumber.getText().toString().trim();


            regPassword1.setErrorEnabled(false);
            regPasswordTwo1.setErrorEnabled(false);

            LoaderDialog loaderDialog=new LoaderDialog(this);
            loaderDialog.create();
            loaderDialog.show();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fastfly.com.bd/api/auth/forget/updatepassword/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                //converting response to json object
                                JSONObject obj = new JSONObject(response);


                                if(obj.getString("successMessage").equals("Successfully changed the password")){
                                    Toasty.success(getApplicationContext(), "Successfully changed password", Toast.LENGTH_SHORT, true).show();

                                    loaderDialog.cancel();
                                    startActivity(new Intent(getApplicationContext(),Login.class));




                                }else {
                                    Toasty.error(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_SHORT, true).show();
                                    loaderDialog.cancel();
                                }


                                //creating a new user object


                                //storing the user in shared preferences
                                //starting the profile activity


                            } catch (JSONException e) {
                                loaderDialog.cancel();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loaderDialog.cancel();
                            Toasty.error(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_SHORT, true).show();

                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    // Now you can use any deserializer to make sense of data
                                    JSONObject obj = new JSONObject(res);
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
                    params.put("phone", phonenum);
                    params.put("password",pass);
                    return params;
                }

            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }

    }




}

