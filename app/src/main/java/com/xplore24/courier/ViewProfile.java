package com.xplore24.courier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.xplore24.courier.Utils.LoaderDialog;
import com.xplore24.courier.Utils.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class ViewProfile extends AppCompatActivity {
    private String marname, companyname, companyAddress, email, fblink;
    private TextView marnameView, companynameView, companyAddressView, emailView, fblinkView;
    private ImageView gotoUpdate;
    private RequestQueue mQueue;
    private LoadingDialog loadingDialog;
    private LoaderDialog loaderDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        marnameView = findViewById(R.id.marnameproView);
        companynameView = findViewById(R.id.companynameproView);
        companyAddressView = findViewById(R.id.companyaddressproView);
        emailView = findViewById(R.id.emailProfileview);
        fblinkView = findViewById(R.id.fblinkproView);
        gotoUpdate = findViewById(R.id.gotoupdateProfile);
         mQueue = Volley.newRequestQueue(this);

        loadingDialog=new LoadingDialog(this);
        loaderDialog=new LoaderDialog(this);
        loaderDialog.create();
        loaderDialog.show();
        //loadingDialog.startLoadingdialog();

         jsonParse();

        gotoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public void jsonParse() {


        String url = "https://fastfly.com.bd/api/auth/getprofile";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("profileData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject profile = jsonArray.getJSONObject(i);
                                String name = profile.getString("name");
                                String comname = profile.getString("companyName");
                                String comaddress = profile.getString("address");
                                String fblink = profile.getString("companyFbLink");
                                String email = profile.getString("email");
                                marnameView.setText(name);
                                emailView.setText(email);
                                companynameView.setText(comname);
                                companyAddressView.setText(comaddress);
                                fblinkView.setText(fblink);
                               // loadingDialog.dialogDismiss();
                                loaderDialog.cancel();
                            }
                        } catch (JSONException e) {
                           // loadingDialog.dialogDismiss();
                            loaderDialog.cancel();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), "Please Try Again Later", Toast.LENGTH_SHORT, true).show();
                //loadingDialog.dialogDismiss();
                loaderDialog.cancel();
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

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(ViewProfile.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


}