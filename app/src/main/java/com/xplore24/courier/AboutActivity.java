package com.xplore24.courier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xplore24.courier.Utils.Utils;

public class AboutActivity extends AppCompatActivity {
    TextView privicypolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Utils.blackIconStatusbar(AboutActivity.this,R.color.backgroundlight);
      privicypolicy=findViewById(R.id.privacypolicy);
      privicypolicy.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent page = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fastfly.com.bd/privacy"));
              startActivity(page);
          }
      });
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(AboutActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}