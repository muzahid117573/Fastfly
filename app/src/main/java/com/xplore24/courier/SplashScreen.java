package com.xplore24.courier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.xplore24.courier.Utils.SharedPrefManager;


public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {


            @Override

            public void run() {




                if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                    finish();
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    return;
                }
                if (!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                    finish();
                    Intent intent=new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }

            }

        }, 2300);
    }
}