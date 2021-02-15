package com.xplore24.courier;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Signup extends AppCompatActivity {

    TextInputLayout  regPhoneNo1, regPassword1,regPasswordTwo1;
    TextInputEditText regPhoneNo, regPassword,regPasswordTwo;
    String ph,pass;

    Button getOtp, regToLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        regPhoneNo = findViewById(R.id.reg_phoneNo);
        regPhoneNo1 = findViewById(R.id.reg_phoneNo1);
        regPassword = findViewById(R.id.reg_password);
        regPassword1 = findViewById(R.id.reg_password1);
        regPasswordTwo = findViewById(R.id.reg_passwordtwo);
        regPasswordTwo1 = findViewById(R.id.reg_passwordtwo1);



        getOtp = findViewById(R.id.getotp);
        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(regPhoneNo.getText().toString().length()!=11){
                    regPhoneNo1.setError("Invalid Phone Number");
                }else if(regPassword.getText().toString().length()==0){
                    regPassword1.setError("Insert Password");

                }else if(regPasswordTwo.getText().toString().length()==0){
                    regPasswordTwo1.setError("Insert Password");

                }else if(!regPassword.getText().toString().equals(regPasswordTwo.getText().toString())){
                    regPasswordTwo1.setError("Password Not Matched");

                }else {

                    ph=regPhoneNo.getText().toString().trim();
                    pass=regPasswordTwo.getText().toString().trim();
                    Intent intent=new Intent(Signup.this,OTPVerification.class);
                    intent.putExtra("Phone",ph);
                    intent.putExtra("Pass",pass);
                    startActivity(intent);
                    regPassword1.setErrorEnabled(false);
                    regPasswordTwo1.setErrorEnabled(false);
                    regPhoneNo1.setErrorEnabled(false);
                }


            }
        });
        regToLoginBtn = findViewById(R.id.reg_login_btn);
        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Signup.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Signup.this,Login.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}