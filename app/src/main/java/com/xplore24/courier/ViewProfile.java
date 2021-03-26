package com.xplore24.courier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputLayout;
import com.xplore24.courier.Model.ProfileModel;
import com.xplore24.courier.Utils.DetectConnection;
import com.xplore24.courier.Utils.LoaderDialog;
import com.xplore24.courier.Utils.LoadingDialog;
import com.xplore24.courier.Utils.SharedPrefManager;
import com.xplore24.courier.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;




public class ViewProfile extends AppCompatActivity {
    private String marname, companyname, companyAddress, email, fblink;
    private EditText marnameView, companynameView, companyAddressView, emailView, fblinkView;
    private ImageView gotoUpdate;
    private RequestQueue mQueue;
    private String url;

    private TextInputLayout marchantnameprofile1,companynameprofile1,companyaddressprofile1,emailaddressprofile1,pagelinkprofile1;
    private MaterialButton profileupdate;
    private LoaderDialog loaderDialog;
    private String marchantname,marchantcompany,marchantcomaddress,marchantemail,marchantpagelink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);



        marnameView = findViewById(R.id.marnameproView);
        companynameView = findViewById(R.id.companynameproView);
        companyAddressView = findViewById(R.id.companyaddressproView);
        emailView = findViewById(R.id.emailProfileview);
        fblinkView = findViewById(R.id.fblinkproView);
         mQueue = Volley.newRequestQueue(this);
         jsonParse();
        loaderDialog=new LoaderDialog(this);
        loaderDialog.create();

        marchantnameprofile1=findViewById(R.id.merchantnameprofile1);
        companyaddressprofile1=findViewById(R.id.companyaddressprofile1);
        companynameprofile1=findViewById(R.id.companynameprofile1);
        emailaddressprofile1=findViewById(R.id.merchantemailprofile1);
        pagelinkprofile1=findViewById(R.id.merchantfbpageprofile1);
        profileupdate=findViewById(R.id.profileupdatebutton);



        profileupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DetectConnection.checkInternetConnection(getApplicationContext())) {
                     Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();

                }
                else if (marnameView.getText().toString().length() == 0) {
                    marchantnameprofile1.setError("Please Write Your Name");
                } else if (companyAddressView.getText().toString().length() == 0) {
                    companyaddressprofile1.setError("Write Your Company Address");

                } else if (companynameView.getText().toString().length() == 0) {
                    companynameprofile1.setError("Write Your Company Name");

                } else if (!isValidEmail(emailView.getText()) ) {
                    emailaddressprofile1.setError("Insert Valid Email");

                } else if (!isValidURL(fblinkView.getText())) {

                    pagelinkprofile1.setError("Insert Valid URL");


                } else {



                    marchantname=marnameView.getText().toString().trim();
                    marchantcompany=companynameView.getText().toString().trim();
                    marchantcomaddress=companyAddressView.getText().toString().trim();
                    marchantemail=emailView.getText().toString().trim();
                    marchantpagelink=fblinkView.getText().toString().trim();
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

    @Override
    protected void onStart() {

        if(!SharedPrefManager.getInstance(this).isprofileDataExist()){
           profileupdate.setText("Create Profile");
            url="https://fastfly.com.bd/api/profile/post/";
        }else {
            profileupdate.setText("Update Profile");
            url="https://fastfly.com.bd/api/profile/update/";
        }

        SharedPreferences myPrefs = getSharedPreferences("volleyregisterlogin", MODE_PRIVATE);
        String namesp = myPrefs.getString("name", null);
        String companysp = myPrefs.getString("companyName", null);
        String emailsp = myPrefs.getString("email", null);
        String addresssp = myPrefs.getString("address", null);
        String fbsp = myPrefs.getString("companyFbLink", null);
        marnameView.setText(namesp);
        companyAddressView.setText(addresssp);
        companynameView.setText(companysp);
        emailView.setText(emailsp);
        fblinkView.setText(fbsp);

        super.onStart();
    }




    private void sendprofileToDatabase() {
        loaderDialog.show();
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
                        Toasty.success(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT, true).show();
                     loaderDialog.cancel();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    }else {
                        Toasty.error(getApplicationContext(), "Please Try Again Later", Toast.LENGTH_SHORT, true).show();
                    }
                    loaderDialog.cancel();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loaderDialog.cancel();
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



    public void jsonParse() {


        String url = "https://fastfly.com.bd/api/profile/get/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("profile");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject profile = jsonArray.getJSONObject(i);
                                String name = profile.getString("name");
                                String comname = profile.getString("companyName");
                                String comaddress = profile.getString("address");
                                String fblink = profile.getString("companyFbLink");
                                String email = profile.getString("email");

                                ProfileModel profileModel=new ProfileModel(
                                        name,comname,comaddress,fblink,email
                                );
                                SharedPrefManager.getInstance(getApplicationContext()).profileData(profileModel);




                            }
                        } catch (JSONException e) {
                            // loadingDialog.dialogDismiss();

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
                //loadingDialog.dialogDismiss();

                error.printStackTrace();
            }
        }) {





            @Override
            public Map<String, String> getHeaders () throws AuthFailureError {
                SharedPreferences myPrefs = getSharedPreferences("volleyregisterlogin", MODE_PRIVATE);
                String token = myPrefs.getString("token", null);

                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
                return headers;








            }




        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);


    }

    public static boolean isValidEmail(CharSequence Email) {
        return (!TextUtils.isEmpty(Email) && Patterns.EMAIL_ADDRESS.matcher(Email).matches());
    }
    public static boolean isValidURL(CharSequence URL) {
        return (!TextUtils.isEmpty(URL) && Patterns.WEB_URL.matcher(URL).matches());
    }




    @Override
    public void onBackPressed() {
        Intent intent =new Intent(ViewProfile.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


}