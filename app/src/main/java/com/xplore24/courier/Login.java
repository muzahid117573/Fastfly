package com.xplore24.courier;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xplore24.courier.Model.User;
import com.xplore24.courier.Utils.LoaderDialog;
import com.xplore24.courier.Utils.LoadingDialog;
import com.xplore24.courier.Utils.SharedPrefManager;
import com.xplore24.courier.Utils.Urls;
import com.xplore24.courier.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity {
    Button callSignUp, login_btn;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout phonenumber1, password1;
    TextInputEditText phonenumber, password;
    private Toast backToast;
    private long backpressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);





        callSignUp = findViewById(R.id.signup_screen);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogan_name);
        phonenumber = findViewById(R.id.phonenumber);
        password = findViewById(R.id.password);
        phonenumber1 = findViewById(R.id.phonenumber1);
        password1 = findViewById(R.id.password1);
        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkConnected()){
                    Toasty.info(Login.this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT, true).show();


                }else {
                    userLogin();
                }

            }
        });
        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void userLogin() {

        final String phonenum = phonenumber.getText().toString().trim();
        final String pass = password.getText().toString().trim();


        if (phonenum.length()!=11) {
            phonenumber1.setError("Insert Valid Number");
            phonenumber1.requestFocus();
            return;
        } else if (TextUtils.isEmpty(pass)) {
            password1.setError("Please enter your password");


            return;
        } else {
            phonenumber1.setErrorEnabled(false);
            password1.setErrorEnabled(false);

            LoaderDialog loaderDialog=new LoaderDialog(this);
            loaderDialog.create();
            loaderDialog.show();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                //converting response to json object
                                JSONObject obj = new JSONObject(response);


                                if(obj.getString("successMessage").equals("User is varified")){
                                    Toasty.success(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT, true).show();

                                     loaderDialog.cancel();

                                    new Handler().postDelayed(new Runnable() {


                                        @Override

                                        public void run() {


                                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();


                                        }

                                    }, 1000);



                                    User user = new User(
                                            obj.getString("token")
                                    );
                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);


                                }else {
                                    Toasty.error(getApplicationContext(), "Invalid User.", Toast.LENGTH_SHORT, true).show();
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
                            Toasty.error(getApplicationContext(), "Invalid User.", Toast.LENGTH_SHORT, true).show();

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
                    params.put("password", pass);
                    return params;
                }

            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }

    }
    @Override
    public void onBackPressed(){

        if (backpressedTime+2000> System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;

        }else {
            backToast  =  Toast.makeText(this,"Press Back Again To Exit",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backpressedTime=System.currentTimeMillis();

    }

    @Override
    protected void onStart() {
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {

            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        super.onStart();
    }

}



