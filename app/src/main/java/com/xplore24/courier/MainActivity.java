package com.xplore24.courier;

import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.wang.avi.AVLoadingIndicatorView;
import com.xplore24.courier.Adapter.ViewOrderAdapter;
import com.xplore24.courier.Model.ViewOrderModel;
import com.xplore24.courier.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MaterialButton placeorder;
    static final float END_SCALE = 0.7f;
    private Toast backToast;
    private long backpressedTime;
    ImageView logoutbutton;
    private Button trackbutton;
    private EditText trackEdittext;
    private TextView trackingtext;
    AVLoadingIndicatorView avLoadingIndicatorView;

    ImageView menuIcon;
    LinearLayout contentView;
    TextView searchView;
    private String track;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        avLoadingIndicatorView=findViewById(R.id.avi);
        avLoadingIndicatorView.hide();
        trackbutton=findViewById(R.id.trackbutton);
        trackEdittext=findViewById(R.id.trackorderhome);
        trackingtext=findViewById(R.id.serachText);
        trackbutton.setOnClickListener(new View.OnClickListener() {
                @Override
           public void onClick(View v) {

                    if(trackEdittext.getText().toString().length()==0) {
                        Toasty.error(getApplicationContext(), "Please Insert Order ID", Toast.LENGTH_SHORT, true).show();

                    }else {
                        track=trackEdittext.getText().toString().trim();
                        startAnim();

                        tracking();
                    }

                    }
                     }
        );



        logoutbutton=findViewById(R.id.logoutbutton);
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           SharedPrefManager.getInstance(getApplicationContext()).logout();
           Intent intent =new Intent(MainActivity.this,Login.class);
           startActivity(intent);
           finish();
            }
        });

        placeorder=findViewById(R.id.placeorder);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,OrderActivity.class));
                finish();
            }
        });
        //Hooks
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        naviagtionDrawer();





    }

    private void tracking() {

        String url="https://fastfly.com.bd/api/auth/id/";
        HashMap<String, String> body = new HashMap<>();
        body.put("id", track);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(body),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {






                            try {
                                stopAnim();
                                trackEdittext.setText("");
                                JSONObject userJson = response.getJSONObject("order");



                                    Toasty.success(getApplicationContext(), "Stay with us", Toast.LENGTH_SHORT, true).show();


                                    String status = userJson.getString("status");
                                    trackingtext.setText(status);




                            } catch (JSONException e) {
                                stopAnim();

                                e.printStackTrace();
                            }






                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), "Please Try Again Later", Toast.LENGTH_SHORT, true).show();


                stopAnim();
                error.printStackTrace();

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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);





    }


    //Navigation Drawer Functions
    private void naviagtionDrawer() {

        //Naviagtion Drawer

        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.nav_productsstatus:

                startActivity(new Intent(this,ViewOrderActivity.class));
                finish();

                return true;
            case R.id.nav_profile:
                  startActivity(new Intent(this,ProfileActivity.class));
                 finish();

                return true;
            case R.id.nav_profile_view:
                  startActivity(new Intent(this,ViewProfile.class));
                 finish();


                return true;
            case R.id.nav_add_order:
                startActivity(new Intent(this,OrderActivity.class));
                finish();


                return true;
            case R.id.nav_website:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fastfly.com.bd/"));
                startActivity(browserIntent);

                return true;

            case R.id.nav_page:
                Intent page = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/fastdeliverybd"));
                startActivity(page);
                return true;

            case R.id.nav_about:
                startActivity(new Intent(this,AboutActivity.class));
                finish();
                return true;



        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
    @Override
    public void onBackPressed(){

        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);



        }else {

            if (backpressedTime+2000> System.currentTimeMillis() ){

                backToast.cancel();
                super.onBackPressed();
                return;

            }else {


                backToast  =  Toast.makeText(this,"Press Back Again To Exit",Toast.LENGTH_SHORT);
                backToast.show();
            }
            backpressedTime=System.currentTimeMillis();

        }



    }

    @Override
    protected void onStart() {
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            Intent intent=new Intent(this, Login.class);
            startActivity(intent);
            return;
        }
        super.onStart();
    }

    void startAnim(){
        avLoadingIndicatorView.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avLoadingIndicatorView.hide();
        // or avi.smoothToHide();
    }

}