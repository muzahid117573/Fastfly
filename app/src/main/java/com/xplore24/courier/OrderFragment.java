package com.xplore24.courier;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
import com.xplore24.courier.Utils.DetectConnection;
import com.xplore24.courier.Utils.LoaderDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

public class OrderFragment extends Fragment {
    private TextInputLayout deliveryAddress1,customerPhone1,collectionMoney1,customername1,customerref1,pickupaddress1,marphone1;
    private TextInputEditText deliveryAddress,customerPhone,collectionMoney,customername,customerref,pickupaddress,marphone;
    private MaterialButton placeorder;
    private String money ="0",address,phone,name,referenceNo,pickUpAdd,merPhone;
    private String charge ="50";
    private MaterialSpinner spinnerweight,spinnerCondition,spinnerarea ;
    private TextView calcharge;
    private Toolbar toolbar;
    private LoaderDialog loaderDialog;
    private CheckBox checkBox;
    private LinearLayout linearcheckbox;
    private String stateweight="0 to 1 kg",statearea ="Inside City",statecharge="Delivery Charge+Collection Money";


    public OrderFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);


        calcharge=view.findViewById(R.id.calcharge);
        calcharge.setText("50");

        deliveryAddress=view.findViewById(R.id.deliveryaddress);
        deliveryAddress1=view.findViewById(R.id.deliveryaddress1);
        customerPhone=view.findViewById(R.id.customerphone);
        customerPhone1=view.findViewById(R.id.customerphone1);
        marphone=view.findViewById(R.id.merphone);
        marphone1=view.findViewById(R.id.merphone1);
        customerref=view.findViewById(R.id.customerref);
        customerref1=view.findViewById(R.id.customerref1);
        pickupaddress=view.findViewById(R.id.pickupaddress);
        pickupaddress1=view.findViewById(R.id.picupaddress1);
        collectionMoney=view.findViewById(R.id.collectedmoney);
        collectionMoney1=view.findViewById(R.id.collectedmoney1);
        customername=view.findViewById(R.id.customername);
        customername1=view.findViewById(R.id.customername1);
        placeorder=view.findViewById(R.id.placeorder);
        toolbar=view.findViewById(R.id.toolbarorder);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        loaderDialog=new LoaderDialog(getActivity());

        linearcheckbox=view.findViewById(R.id.linearcheckbox);
        checkBox =view.findViewById(R.id.checkboxpickup);
        linearcheckbox.setVisibility(View.VISIBLE);
        SharedPreferences myPrefs = getActivity().getSharedPreferences("volleyregisterlogin", Context.MODE_PRIVATE);
        String addresssp = myPrefs.getString("address", null);
        String phonenum = myPrefs.getString("phone", null);
        marphone.setText(phonenum);
        pickupaddress.setText(addresssp);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    if (linearcheckbox.getVisibility() == View.VISIBLE) {
                        linearcheckbox.setVisibility(View.GONE);
                    }
                }
                if(!b){

                    linearcheckbox.setVisibility(View.VISIBLE);



                }

            }
        });



        spinnerweight= view.findViewById(R.id.spinnerweight);
        spinnerweight.setItems("0 to 1 kg", "1 to 2 kg", "2 to 3 kg", "3 to 4 kg","4 to 5 kg");

        spinnerweight.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {



                if(item.equals("0 to 1 kg")){
                    stateweight=item;
                    checkCharge();
                }
                if(item.equals("1 to 2 kg")){
                    stateweight=item;
                    checkCharge();
                }
                if(item.equals("2 to 3 kg")){
                    stateweight=item;
                    checkCharge();
                }
                if(item.equals("3 to 4 kg")){
                    stateweight=item;
                    checkCharge();
                }
                if(item.equals("4 to 5 kg")){
                    stateweight=item;
                    checkCharge();
                }
                stateweight=item;
                checkCharge();

            }
        });


        spinnerarea= view.findViewById(R.id.spinnerarea);
        spinnerarea.setItems("Inside City", "Outside City");

        spinnerarea.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if(item.equals("Inside City")){
                    statearea=item;
                    checkCharge();



                }
                if(item.equals("Outside City")){

                    statearea=item;
                    checkCharge();


                }
                statearea=item;
                checkCharge();

            }

        });


        spinnerCondition= view.findViewById(R.id.spinnercondition);
        spinnerCondition.setItems("Delivery Charge+Collection Money","Only Delivery(All Paid)", "Delivery Charge(Unpaid Charge)");

        spinnerCondition.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if(item.equals("Only Delivery(All Paid)")){
                    collectionMoney1.setVisibility(View.INVISIBLE);
                    statecharge=item;
                    checkCharge();
                }
                if(item.equals("Delivery Charge+Collection Money")){
                    collectionMoney1.setVisibility(View.VISIBLE);

                    statecharge=item;
                    checkCharge();

                }
                if(item.equals("Delivery Charge(Unpaid Charge)")){
                    statecharge=item;
                    checkCharge();
                    collectionMoney1.setVisibility(View.INVISIBLE);

                }
                statecharge=item;
            }
        });


        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DetectConnection.checkInternetConnection(getActivity())) {
                    Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT).show();

                }else if(pickupaddress.getText().toString().length()==0){
                    pickupaddress1.setError("Insert Address");
                }
                else if(marphone.getText().toString().length()!=11){
                    marphone1.setError("Insert Valid Phone Number");
                }
                else if(customername.getText().toString().length()==0){
                    customername1.setError("Insert Full Name");
                }else if(customerPhone.getText().toString().length()!=11){
                    customerPhone1.setError("Insert Valid Phone Number");
                }else if(collectionMoney.getText().toString().length()==0){
                    collectionMoney.setText("0");
                }
                else if(deliveryAddress.getText().toString().length()==0){
                    deliveryAddress1.setError("Insert Address");
                }else if(customerref.getText().toString().length()==0){
                    customerref1.setError("Insert Reference No");
                }
                else {
                    name=customername.getText().toString().trim();
                    phone=customerPhone.getText().toString().trim();
                    address=deliveryAddress.getText().toString().trim();
                    referenceNo=customerref.getText().toString().trim();
                    money=collectionMoney.getText().toString().trim();
                    pickUpAdd=pickupaddress.getText().toString().trim();
                    merPhone=marphone.getText().toString().trim();

                    loaderDialog.create();
                    loaderDialog.show();

                    setOrder();
                    pickupaddress1.setErrorEnabled(false);
                    marphone1.setErrorEnabled(false);
                    customerref1.setErrorEnabled(false);
                    customername1.setErrorEnabled(false);
                    customerPhone1.setErrorEnabled(false);
                    deliveryAddress1.setErrorEnabled(false);
                    collectionMoney1.setErrorEnabled(false);
                }
            }
        });







        return view;
    }

    private void checkCharge() {

        if(statearea.equals("Inside City")){
            if(statecharge.equals("Only Delivery(All Paid)")){
                calcharge.setText("0");
                charge=calcharge.getText().toString().trim();
            }
            else if(stateweight.equals("0 to 1 kg")){
                calcharge.setText("50");
                charge=calcharge.getText().toString().trim();
            }
            else if(stateweight.equals("1 to 2 kg")){
                calcharge.setText("65");
                charge=calcharge.getText().toString().trim();
            } else if(stateweight.equals("2 to 3 kg")){
                calcharge.setText("80");
                charge=calcharge.getText().toString().trim();
            }
            else if(stateweight.equals("3 to 4 kg")){
                calcharge.setText("100");
                charge=calcharge.getText().toString().trim();
            }
            else {
                calcharge.setText("120");
                charge=calcharge.getText().toString().trim();
            }
        }
        if(statearea.equals("Outside City")){
            if(statecharge.equals("Only Delivery(All Paid)")){
                calcharge.setText("0");
                charge=calcharge.getText().toString().trim();
            }
            else if(stateweight.equals("0 to 1 kg")){
                calcharge.setText("100");
                charge=calcharge.getText().toString().trim();
            }
            else if(stateweight.equals("1 to 2 kg")){
                calcharge.setText("110");
                charge=calcharge.getText().toString().trim();
            }
            else if(stateweight.equals("2 to 3 kg")){
                calcharge.setText("120");
                charge=calcharge.getText().toString().trim();
            }
            else if(stateweight.equals("3 to 4 kg")){
                calcharge.setText("135");
                charge=calcharge.getText().toString().trim();
            }
            else {
                calcharge.setText("150");
                charge=calcharge.getText().toString().trim();
            }
        }

    }


    private void setOrder() {
        String url="https://fastfly.com.bd/api/order/place/";
        HashMap<String, String> body = new HashMap<>();
        body.put("phone", phone);
        body.put("name", name);
        body.put("address", address);
        body.put("money", money);
        body.put("deliveryCharge",charge);
        body.put("refferenceID",referenceNo);
        body.put("pickupAddress",pickUpAdd);
        body.put("merchentPhone",merPhone);

        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.POST, url, new JSONObject(body), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {



                    if(response.getString("successMessage").equals("Order Creation Success")){
                        Toasty.success(getActivity(), "Order Submit Successfully", Toast.LENGTH_SHORT, true).show();

                        loaderDialog.cancel();
                        startActivity(new Intent(getActivity(),MainActivity.class));


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
                        //Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();

                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                                Toasty.error(getActivity(), obj.getString("errorMessage"), Toast.LENGTH_SHORT, true).show();

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
                SharedPreferences myPrefs = getActivity().getSharedPreferences("volleyregisterlogin", Context.MODE_PRIVATE);
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
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);



    }



    }













