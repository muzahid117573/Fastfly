package com.xplore24.courier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.xplore24.courier.Model.User;
import com.xplore24.courier.Utils.LoadingDialog;

import com.xplore24.courier.Utils.SharedPrefManager;
import com.xplore24.courier.Utils.Urls;
import com.xplore24.courier.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import es.dmoral.toasty.Toasty;


public class OTPVerification extends AppCompatActivity {
    private String mobile,pass,code;
    private String mVerificationId;
    private EditText editTextCode;
    private TextView countdown;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    //firebase auth object
    private FirebaseAuth mAuth;
    private Button resendbutton;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verification);
        loadingDialog=new LoadingDialog(OTPVerification.this);

        Intent intent = getIntent();
        mobile = intent.getStringExtra("Phone");
        pass = intent.getStringExtra("Pass");


        countdown=findViewById(R.id.countdown);
        resendbutton=findViewById(R.id.resendotp);
        resendbutton.setVisibility(View.INVISIBLE);

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdown.setText("Remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                countdown.setText("Resend Now!");
                resendbutton.setVisibility(View.VISIBLE);
            }
        }.start();
        resendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(mobile,mResendToken);
            }
        });

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.otpedit);



        findViewById(R.id.verifyotp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = editTextCode.getText().toString().trim();
                if (code.length() == 6 && isNetworkConnected()) {
                    verifyVerificationCode(code);
                    loadingDialog.startLoadingdialog();


                }else {
                    Toasty.error(OTPVerification.this,"Enter A Valid OTP", Toast.LENGTH_LONG, true).show();

                    //verifying the code entered manually


                }
            }
        });



    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }




    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+88"+mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+88"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }




    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
                loadingDialog.startLoadingdialog();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (loadingDialog.loading()) {
                loadingDialog.dialogDismiss();
                Toasty.error(OTPVerification.this, e.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };



    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPVerification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {


                                loadingDialog.dialogDismiss();
                                Toasty.error(OTPVerification.this,"Phone Number Already Exist", Toast.LENGTH_LONG, true).show();




                        }
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                Toasty.error(OTPVerification.this,"Invalid code entered", Toast.LENGTH_LONG, true).show();
                                loadingDialog.dialogDismiss();



                        }
                        if (task.isSuccessful()) {
                            sendDataToDatabase();

                        }
                    }
                });
    }

    private void sendDataToDatabase() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            if(obj.getString("successMessage").equals("Registration success. Please signin.")){
                                Toasty.success(getApplicationContext(), "Registration success. Please Login!", Toast.LENGTH_SHORT, true).show();

                                    loadingDialog.dialogDismiss();

                                new Handler().postDelayed(new Runnable() {


                                    @Override

                                    public void run() {


                                        Intent intent = new Intent(OTPVerification.this, Login.class);
                                        startActivity(intent);
                                        finish();


                                    }

                                }, 1000);





                            }else {
                                Toasty.error(getApplicationContext(), obj.getString("errorMessage"), Toast.LENGTH_LONG, true).show();

                                    loadingDialog.dialogDismiss();

                            }






                        } catch (JSONException e) {
                            e.printStackTrace();

                                loadingDialog.dialogDismiss();

                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loadingDialog.dialogDismiss();

                        Toasty.error(OTPVerification.this,"Phone Number Already Exist", Toast.LENGTH_SHORT, true).show();

                        new Handler().postDelayed(new Runnable() {


                            @Override

                            public void run() {


                                Intent intent = new Intent(OTPVerification.this, Signup.class);
                                startActivity(intent);
                                finish();


                            }

                        }, 1000);


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
                params.put("phone", mobile);
                params.put("password", pass);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    protected void onStart() {

        if(!isNetworkConnected()){
            Toasty.info(OTPVerification.this, "Please Check Your Internet Connection.", Toast.LENGTH_LONG, true).show();


        }else {

            sendVerificationCode(mobile);

        }

        super.onStart();
    }
}


