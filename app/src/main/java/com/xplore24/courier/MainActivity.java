package com.xplore24.courier;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentSender;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import android.net.Uri;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.xplore24.courier.Utils.SharedPrefManager;
import com.xplore24.courier.Utils.Utils;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    static final float END_SCALE = 0.7f;
    private Toast backToast;
    private long backpressedTime;
    private Fragment fragment=null;

    private BottomAppBar chipNavigationBar;

    LinearLayout contentView;
    ImageView menuIcon;
    private int REQ_CODE=6;


    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_home);
        drawerLayout = findViewById(R.id.drawer_layout);
        contentView = findViewById(R.id.content);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        chipNavigationBar = findViewById(R.id.bottomAppBar);
        chipNavigationBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
                animateNavigationDrawer();
            }
        });
        FloatingActionButton fab=findViewById(R.id.floatingbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new OrderFragment()).commit();

            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        chipNavigationBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                        break;
                    case R.id.nav_refresh:
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        break;

                }
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

                }
                return false;
            }
        });



        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());


        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();


        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            REQ_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.nav_productsstatus:

                startActivity(new Intent(this,ViewOrderActivity.class));
                finish();

                return true;

            case R.id.nav_profile_view:
                  startActivity(new Intent(this,ViewProfile.class));
                 finish();


                return true;
            case R.id.nav_add_order:
                startActivity(new Intent(this,OrderActivity.class));
                finish();


                return true;
            case R.id.nav_website:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fastfly.com.bd/"));
                startActivity(browserIntent);

                return true;

            case R.id.nav_page:
                Intent page = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/fastdeliverybd"));
                startActivity(page);
                return true;

            case R.id.nav_about:
                startActivity(new Intent(this,AboutActivity.class));
                finish();
                return true;
            case R.id.nav_exit:
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    logoutDialog();

                    return true;


                }




        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
    @Override
    public void onBackPressed(){

        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);



        }else {

            if (backpressedTime+2000> System.currentTimeMillis() ){

                backToast.cancel();
                super.onBackPressed();
                return;

            }else {


                backToast  =  Toast.makeText(this,"Press Back Again To Exit",Toast.LENGTH_SHORT);
                backToast.show();
            }
            backpressedTime=System.currentTimeMillis();

        }



    }

//    @Override
//    protected void onStart() {
//
//
//        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
//            finish();
//            Intent intent=new Intent(this, Login.class);
//            startActivity(intent);
//            return;
//        }
//
//        super.onStart();
//    }



    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("Do you want to logout?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                        Intent intent =new Intent(MainActivity.this,Login.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button

                        dialog.cancel();


                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Logout");
        alert.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Toasty.success(this,"Start Updating",Toasty.LENGTH_SHORT,true).show();


            }
        }
    }

}