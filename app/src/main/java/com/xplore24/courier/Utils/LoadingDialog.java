package com.xplore24.courier.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.xplore24.courier.R;

 public class LoadingDialog {
     Activity activity;
    AlertDialog dialog;

    public LoadingDialog(Activity activity) {

        this.activity = activity;
    }
   public void startLoadingdialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loadinglayout,null));
        builder.setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }

     public boolean loading(){
         AlertDialog.Builder builder=new AlertDialog.Builder(activity);
         LayoutInflater inflater=activity.getLayoutInflater();
         builder.setView(inflater.inflate(R.layout.loadinglayout,null));
         builder.setCancelable(false);
         dialog=builder.create();
         dialog.show();
         return true;
     }

    public void dialogDismiss(){
        dialog.dismiss();
    }


}
