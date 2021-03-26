package com.xplore24.courier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
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
import com.xplore24.courier.Adapter.ViewOrderAdapter;
import com.xplore24.courier.Model.ViewOrderModel;
import com.xplore24.courier.Utils.LoaderDialog;
import com.xplore24.courier.Utils.LoadingDialog;
import com.xplore24.courier.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ViewOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RequestQueue mQueue;
    private List<ViewOrderModel> order;
    private LoaderDialog loaderDialog;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        mQueue = Volley.newRequestQueue(this);
        recyclerView = (RecyclerView) findViewById(R.id.vieworderrecycle);
        recyclerView.setHasFixedSize(true);

        toolbar=findViewById(R.id.toolbarorderdetails);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        order = new ArrayList<>();
        loaderDialog=new LoaderDialog(this);
        loaderDialog.create();
        loaderDialog.show();

        sendRequest();



    }

    private void sendRequest() {

        String url = "https://fastfly.com.bd/api/order/get";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("order");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(jsonArray.length()==0){
                            loaderDialog.cancel();
                            Toasty.info(getApplicationContext(), "No Order Available", Toast.LENGTH_SHORT, true).show();


                        }else {


                            for (int i = 0; i < jsonArray.length(); i++) {


                                ViewOrderModel viewOrderModel = new ViewOrderModel();
                                try {
                                    JSONObject order = jsonArray.getJSONObject(i);
                                    String status = order.getString("status");
                                    String orderId = order.getString("_id");
                                    String phone = order.getString("phone");
                                    String address = order.getString("address");
                                    String money = order.getString("money");
                                    String deliveryCharge = order.getString("deliveryCharge");
                                    viewOrderModel = new ViewOrderModel(status, orderId, phone, address, money, deliveryCharge);

                                    loaderDialog.cancel();

                                } catch (JSONException e) {
                                    loaderDialog.cancel();
                                    e.printStackTrace();
                                }

                                order.add(viewOrderModel);
                            }




                        }

                            mAdapter = new ViewOrderAdapter(ViewOrderActivity.this, order);
                            recyclerView.setAdapter(mAdapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), "Please Try Again Later", Toast.LENGTH_SHORT, true).show();
                loaderDialog.cancel();
                error.printStackTrace();
            }
        }) {





            @Override
            public Map<String, String> getHeaders () throws AuthFailureError {
                SharedPreferences myPrefs = getSharedPreferences("volleyregisterlogin", MODE_PRIVATE);
                String token = myPrefs.getString("token", null);
                Log.d("myTag", token);


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
        Intent intent =new Intent(ViewOrderActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}