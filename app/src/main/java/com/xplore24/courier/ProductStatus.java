package com.xplore24.courier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import com.baoyachi.stepview.VerticalStepView;

import java.util.ArrayList;
import java.util.List;


public class ProductStatus extends AppCompatActivity {
    VerticalStepView verticalStepView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_status);

     verticalStepView=findViewById(R.id.verticalstepview);


        List<String> list0 = new ArrayList<>();
        list0.add("Pick Up");
        list0.add("Hub");
        list0.add("Bap Re Bap");
        list0.add("On the way");
        list0.add("Delivered");
        list0.add("Good Night");
        verticalStepView.setStepsViewIndicatorComplectingPosition(list0.size() - 2)
                .reverseDraw(false)//default is true
                .setStepViewTexts(list0)
                .setLinePaddingProportion(0.85f)
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white))
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getApplicationContext(), R.color.uncompleted_text_color))
                .setStepViewComplectedTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white))
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getApplicationContext(), R.color.uncompleted_text_color))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.complted))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_icon))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.attention));


    }
    @Override
    public void onBackPressed() {
        Intent intent =new Intent(ProductStatus.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}