package com.xplore24.courier;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.xplore24.courier.Utils.DetectConnection;
import com.xplore24.courier.Utils.SharedPrefManager;
import com.xplore24.courier.Utils.Utils;


public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (!DetectConnection.checkInternetConnection(this)) {
            // Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
            alertDialog();

        } else {


            new Handler().postDelayed(new Runnable() {


                @Override

                public void run() {


                    if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    if (!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {

                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }

                }

            }, 1000);
        }
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);

        builder.setMessage("Please Check Your Internet Connection")
                .setCancelable(false)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(SplashScreen.this, SplashScreen.class));
                        finish();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button

                        finish();


                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Connection Failed");
        alert.show();


    }
}