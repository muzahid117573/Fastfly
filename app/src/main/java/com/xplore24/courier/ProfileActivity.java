package com.xplore24.courier;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.xplore24.courier.Utils.LoaderDialog;
import com.xplore24.courier.Utils.LoadingDialog;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
public class ProfileActivity extends AppCompatActivity {
    private String marchantname,marchantcompany,marchantcomaddress,marchantemail,marchantpagelink;
    private MaterialButton profileupdate;
    private LoadingDialog loadingDialog;
    private LoaderDialog loaderDialog;
    private TextInputLayout marchantnameprofile1,companynameprofile1,companyaddressprofile1,emailaddressprofile1,pagelinkprofile1;
    private TextInputEditText marchantnameprofile,companynameprofile,companyaddressprofile,emailaddressprofile,pagelinkprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        marchantnameprofile=findViewById(R.id.merchantnameprofile);
        companyaddressprofile=findViewById(R.id.companyaddressprofile);
        companynameprofile=findViewById(R.id.companynameprofile);
        emailaddressprofile=findViewById(R.id.merchantemailprofile);
        pagelinkprofile=findViewById(R.id.merchantfbpageprofile);
        loadingDialog=new LoadingDialog(this);
        marchantnameprofile1=findViewById(R.id.merchantnameprofile1);
        companyaddressprofile1=findViewById(R.id.companyaddressprofile1);
        companynameprofile1=findViewById(R.id.companynameprofile1);
        emailaddressprofile1=findViewById(R.id.merchantemailprofile1);
        pagelinkprofile1=findViewById(R.id.merchantfbpageprofile1);
        profileupdate=findViewById(R.id.profileupdatebutton);






        profileupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marchantnameprofile.getText().toString().length() == 0) {
                    marchantnameprofile1.setError("Please Write Your Name");
                } else if (companyaddressprofile.getText().toString().length() == 0) {
                    companyaddressprofile1.setError("Write Your Company Address");

                } else if (companynameprofile.getText().toString().length() == 0) {
                    companynameprofile1.setError("Write Your Company Name");

                } else if (!isValidEmail(emailaddressprofile.getText()) ) {
                    emailaddressprofile1.setError("Insert Valid Email");

                } else if (!isValidURL(pagelinkprofile.getText())) {

                    pagelinkprofile1.setError("Insert Valid URL");


                } else {

                    loadingDialog.startLoadingdialog();

                     marchantname=marchantnameprofile.getText().toString().trim();
                     marchantcompany=companynameprofile.getText().toString().trim();
                     marchantcomaddress=companyaddressprofile.getText().toString().trim();
                     marchantemail=emailaddressprofile.getText().toString().trim();
                     marchantpagelink=pagelinkprofile.getText().toString().trim();
                    marchantnameprofile1.setErrorEnabled(false);
                    companyaddressprofile1.setErrorEnabled(false);
                    companynameprofile1.setErrorEnabled(false);
                    emailaddressprofile1.setErrorEnabled(false);
                    pagelinkprofile1.setErrorEnabled(false);
                    sendprofileToDatabase();


                }
            }
        });



    }

    private void sendprofileToDatabase() {

        String url="https://fastfly.com.bd/api/auth/profile/";
        HashMap<String, String> body = new HashMap<>();
        body.put("name", marchantname);
        body.put("address", marchantcomaddress);
        body.put("companyName", marchantcompany);
        body.put("companyFbLink", marchantpagelink);
        body.put("email", marchantemail);
        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.POST, url, new JSONObject(body), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    if(response.getBoolean("success")){
                        Toasty.success(getApplicationContext(), "Profile Created Successfully", Toast.LENGTH_SHORT, true).show();
                        new Handler().postDelayed(new Runnable() {


                            @Override

                            public void run() {


                                Intent intent = new Intent(ProfileActivity.this, ViewProfile.class);
                                startActivity(intent);
                                finish();


                            }

                        }, 1000);

                    }else {
                        Toasty.error(getApplicationContext(), "Please Try Again Later", Toast.LENGTH_SHORT, true).show();
                    }
                    loadingDialog.dialogDismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dialogDismiss();
                        Toasty.error(getApplicationContext(), "Profile Already Created", Toast.LENGTH_SHORT, true).show();

                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException | JSONException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } // returned data is not JSONObject?

                        }





                    }
                })


        {



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences myPrefs = getSharedPreferences("volleyregisterlogin", MODE_PRIVATE);
                String token=  myPrefs.getString("token", null);

                HashMap<String,String> headers =new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization",token);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


    }

    public static boolean isValidEmail(CharSequence Email) {
        return (!TextUtils.isEmpty(Email) && Patterns.EMAIL_ADDRESS.matcher(Email).matches());
    }
    public static boolean isValidURL(CharSequence URL) {
        return (!TextUtils.isEmpty(URL) && Patterns.WEB_URL.matcher(URL).matches());
    }



    @Override
    public void onBackPressed() {
        Intent intent =new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}