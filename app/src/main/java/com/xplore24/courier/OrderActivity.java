package com.xplore24.courier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.xplore24.courier.Utils.LoaderDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class OrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextInputLayout deliveryAddress1,customerPhone1,collectionMoney1;
    private TextInputEditText deliveryAddress,customerPhone,collectionMoney;
    private MaterialButton placeorder;
    private String money,address,phone;
    private String charge="50";
    private MaterialSpinner spinner ;
    private TextView calcharge;
    private Toolbar toolbar;
    private LoaderDialog loaderDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        calcharge=findViewById(R.id.calcharge);
        calcharge.setText("50");
        deliveryAddress=findViewById(R.id.deliveryaddress);
        deliveryAddress1=findViewById(R.id.deliveryaddress1);
        customerPhone=findViewById(R.id.customerphone);
        customerPhone1=findViewById(R.id.customerphone1);
        collectionMoney=findViewById(R.id.collectedmoney);
        collectionMoney1=findViewById(R.id.collectedmoney1);
        placeorder=findViewById(R.id.placeorder);
        toolbar=findViewById(R.id.toolbarorder);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        loaderDialog=new LoaderDialog(this);






        spinner= findViewById(R.id.spinner);
        spinner.setItems("0 to 1 kg", "1 to 2 kg", "2 to 3 kg", "3 to 4 kg");

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if(item.equals("0 to 1 kg")){
                    calcharge.setText("50");
                    charge=calcharge.getText().toString().trim();
                }
                if(item.equals("1 to 2 kg")){
                    calcharge.setText("70");
                    charge=calcharge.getText().toString().trim();
                }
                if(item.equals("2 to 3 kg")){
                    calcharge.setText("90");
                    charge=calcharge.getText().toString().trim();
                }
                if(item.equals("3 to 4 kg")){
                    calcharge.setText("110");
                    charge=calcharge.getText().toString().trim();
                }
            }
        });

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deliveryAddress.getText().toString().length()==0){
                    deliveryAddress1.setError("Insert Address");
                }else if(customerPhone.getText().toString().length()!=11){
                    customerPhone1.setError("Insert Valid Number");

                }else if(collectionMoney.getText().toString().length()==0) {
                    collectionMoney1.setError("Insert Money");

                }else {

                    phone=customerPhone.getText().toString().trim();
                    address=deliveryAddress.getText().toString().trim();
                    money=collectionMoney.getText().toString().trim();

                    loaderDialog.create();
                    loaderDialog.show();

                    setOrder();

                    customerPhone1.setErrorEnabled(false);
                    deliveryAddress1.setErrorEnabled(false);
                    collectionMoney1.setErrorEnabled(false);
                }
            }
        });







    }


    private void setOrder() {
        String url="https://fastfly.com.bd/api/auth/order/";
        HashMap<String, String> body = new HashMap<>();
        body.put("phone", phone);
        body.put("address", address);
        body.put("money", money);
        body.put("deliveryCharge",charge);

        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.POST, url, new JSONObject(body), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {



                    if(response.getString("successMessage").equals("Order creation success.")){
                        Toasty.success(getApplicationContext(), "Order Submit Successfully", Toast.LENGTH_SHORT, true).show();

                         loaderDialog.cancel();
                    }else {
                        Toasty.error(getApplicationContext(), "Please Try Again Later", Toast.LENGTH_SHORT, true).show();
                        loaderDialog.cancel();
                    }



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
                        Toasty.error(getApplicationContext(), "Please Try Again Later", Toast.LENGTH_SHORT, true).show();

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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if(text.equals("0 to 1 kg")){
            charge="50";

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(OrderActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}