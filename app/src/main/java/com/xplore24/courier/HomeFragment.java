package com.xplore24.courier;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.xplore24.courier.Adapter.SliderAdapter;
import com.xplore24.courier.Model.OrderCancel;
import com.xplore24.courier.Model.OrderComplete;
import com.xplore24.courier.Model.OrderPlaced;
import com.xplore24.courier.Model.OrderShipment;
import com.xplore24.courier.Model.ProfileModel;
import com.xplore24.courier.Model.SliderItemModel;
import com.xplore24.courier.Utils.DetectConnection;
import com.xplore24.courier.Utils.SharedPrefManager;
import com.xplore24.courier.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private LinearLayout linearorderplace, linearinship, linearcom, linearcancel;
    private RequestQueue mQueue;
    static final float END_SCALE = 0.7f;
    private Toast backToast;
    private long backpressedTime;
    ImageView logoutbutton;
    private Button trackbutton;
    private EditText trackEdittext;
    private String marname, companyname, companyAddress, email, fblink;
    private TextView orderplaced, ordership, ordercomplet, ordercancel;


    ImageView menuIcon;
    LinearLayout contentView;
    TextView searchView;
    private String track;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private String cancelnum, completenum, shipmentnum, placednum;
    private MaterialCardView cardviewPlaceorder, cardviewinship, cardviewcancel, cardviewcomplete;

    public HomeFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        mQueue = Volley.newRequestQueue(getContext());

        orderplaced = view.findViewById(R.id.orderplaced);
        ordercancel = view.findViewById(R.id.ordercancel);
        ordership = view.findViewById(R.id.orderinshipment);
        ordercomplet = view.findViewById(R.id.ordercompled);

        cardviewinship = view.findViewById(R.id.cardviewinship);
        cardviewcomplete = view.findViewById(R.id.cardviewordercom);
        cardviewcancel = view.findViewById(R.id.cardviewordercancel);
        cardviewPlaceorder = view.findViewById(R.id.cardviewderplace);


        //slider
        SliderView sliderView = view.findViewById(R.id.imageSlider);
        SliderItemModel sliderItemModel = new SliderItemModel();
        SliderAdapter adapter = new SliderAdapter(getContext());
        adapter.addItem(sliderItemModel);
        List<SliderItemModel> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 1; i < 4; i++) {
            SliderItemModel sliderItem = new SliderItemModel();

            if (i == 1) {
                sliderItem.setImageUrl("https://ekottro.com/fastfly/img3.png");
            } else if (i == 2) {
                sliderItem.setImageUrl("https://ekottro.com/fastfly/img2.png");
            } else if (i == 3) {
                sliderItem.setImageUrl("https://ekottro.com/fastfly/img1.png");


            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems((ArrayList<SliderItemModel>) sliderItemList);


        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();


        cardviewPlaceorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderplaced.getText().toString().equals(String.valueOf(0))) {
                    Toasty.info(getContext(), "There Is No placed Order", Toast.LENGTH_SHORT, true).show();


                } else {
                    startActivity(new Intent(getContext(), PlaceOrderActivity.class));
                    getActivity().finish();

                }
            }
        });


        cardviewinship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ordership.getText().toString().equals(String.valueOf(0))) {
                    Toasty.info(getContext(), "There Is Nothing In shipment", Toast.LENGTH_SHORT, true).show();


                } else {
                    startActivity(new Intent(getContext(), InshipOrderActivity.class));
                    getActivity().finish();

                }
            }
        });


        cardviewcomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ordercomplet.getText().toString().equals(String.valueOf(0))) {
                    Toasty.info(getContext(), "There Is No Completed Order", Toast.LENGTH_SHORT, true).show();


                } else {
                    startActivity(new Intent(getContext(), CompleteOrderActivity.class));
                    getActivity().finish();

                }
            }
        });


        cardviewcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ordercancel.getText().toString().equals(String.valueOf(0))) {
                    Toasty.info(getContext(), "There Is No Cancel Order", Toast.LENGTH_SHORT, true).show();


                } else {


                    startActivity(new Intent(getContext(), CancelOrderActivity.class));
                    getActivity().finish();

                }
            }
        });


        new Handler().postDelayed(new Runnable() {


            @Override

            public void run() {
                TextView textView = view.findViewById(R.id.trackingname);



                if (!SharedPrefManager.getInstance(getActivity()).isprofileDataExist()) {

                    textView.setText("Please Create Profile");

                } else {
                    textView.setText("Track Your Orders");


                }


            }

        }, 3200);


        trackbutton = view.findViewById(R.id.trackbutton);
        trackEdittext = view.findViewById(R.id.trackorderhome);
        trackbutton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               if (trackEdittext.getText().toString().length() == 0) {
                                                   Toasty.error(getActivity(), "Please Insert Order ID", Toast.LENGTH_SHORT, true).show();

                                               } else {
                                                   track = trackEdittext.getText().toString().trim();

                                                   tracking();
                                               }

                                           }
                                       }
        );





        return view;


    }


    public void jsonParse() {


        String url = "https://fastfly.com.bd/api/profile/get";
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

                                ProfileModel profileModel = new ProfileModel(
                                        name, comname, comaddress, fblink, email
                                );
                                SharedPrefManager.getInstance(getActivity()).profileData(profileModel);


                            }
                        } catch (JSONException e) {
                            // loadingDialog.dialogDismiss();

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(), "Please Try Again Later", Toast.LENGTH_SHORT, true).show();
                //loadingDialog.dialogDismiss();

                error.printStackTrace();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences myPrefs = getActivity().getSharedPreferences("volleyregisterlogin", Context.MODE_PRIVATE);
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


    private void tracking() {

        String url = "https://fastfly.com.bd/api/order/id/";
        HashMap<String, String> body = new HashMap<>();
        body.put("id", track);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(body),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            trackEdittext.setText("");
                            JSONObject userJson = response.getJSONObject("order");


                            String status = userJson.getString("status");
                            Toasty.success(getActivity(), status, Toast.LENGTH_LONG, true).show();

                          


                        } catch (JSONException e) {


                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(), "Please Try Again Later", Toast.LENGTH_SHORT, true).show();


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
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences myPrefs = getActivity().getSharedPreferences("volleyregisterlogin", Context.MODE_PRIVATE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
    private void naviagtionDrawer() {

        //Naviagtion Drawer

        navigationView.setCheckedItem(R.id.nav_home);

     /*  menuIcon.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });  */

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
    public void onStart() {

        if (!DetectConnection.checkInternetConnection(getActivity())) {
            Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT).show();

        }else {
            getplaced();
            jsonParse();
            getCompleted();
            getShipment();
            getCanceled();
        }

        SharedPreferences myPrefs = getActivity().getSharedPreferences("volleyregisterlogin", Context.MODE_PRIVATE);

        orderplaced.setText(myPrefs.getString("placed", null));
        ordership.setText(myPrefs.getString("inshipment", null));
        ordercomplet.setText(myPrefs.getString("complete", null));
        ordercancel.setText(myPrefs.getString("cancel", null));


        if (!SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            return;
        }

        super.onStart();
    }





    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Do you want to logout?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        SharedPrefManager.getInstance(getActivity()).logout();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                        getActivity().finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Logout");
        alert.show();


    }

    private void getplaced() {

        String url = "https://fastfly.com.bd/api/order/get/placed/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("orderData");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        placednum = Integer.toString(jsonArray.length());

                        OrderPlaced orderPlaced = new OrderPlaced(
                                placednum
                        );
                        SharedPrefManager.getInstance(getActivity()).statusplaced(orderPlaced);
                        orderplaced.setText(placednum);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences myPrefs = getActivity().getSharedPreferences("volleyregisterlogin", Context.MODE_PRIVATE);
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

    private void getShipment() {

        String url = "https://fastfly.com.bd/api/order/get/shipment/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("orderData");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        shipmentnum = Integer.toString(jsonArray.length());
                        OrderShipment orderShipment = new OrderShipment(
                                shipmentnum
                        );
                        SharedPrefManager.getInstance(getActivity()).statusinship(orderShipment);
                        ordership.setText(shipmentnum);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences myPrefs = getActivity().getSharedPreferences("volleyregisterlogin", Context.MODE_PRIVATE);
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


    private void getCompleted() {

        String url = "https://fastfly.com.bd/api/order/get/completed/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("orderData");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        completenum = Integer.toString(jsonArray.length());

                        OrderComplete orderComplete = new OrderComplete(
                                completenum
                        );
                        SharedPrefManager.getInstance(getActivity()).statuscomplete(orderComplete);
                        ordercomplet.setText(completenum);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences myPrefs = getActivity().getSharedPreferences("volleyregisterlogin", Context.MODE_PRIVATE);
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

    private void getCanceled() {

        String url = "https://fastfly.com.bd/api/order/get/canceled/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("orderData");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cancelnum = Integer.toString(jsonArray.length());
                        OrderCancel orderCancel = new OrderCancel(
                                cancelnum
                        );
                        SharedPrefManager.getInstance(getActivity()).statuscancel(orderCancel);
                        ordercancel.setText(cancelnum);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences myPrefs = getActivity().getSharedPreferences("volleyregisterlogin", Context.MODE_PRIVATE);
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
}