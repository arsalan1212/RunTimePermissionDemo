package com.example.arsalankhan.runtimepermissiondemo;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsalan khan on 7/26/2017.
 */

public class BaseActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    protected static int CONTACT_REQUEST_CODE=1;
    protected static int READPHONE_REQUEST_CODE=2;
    protected static int WRITE_REQUEST_CODE=3;

    protected String[] multiplePermission=new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE};


    protected void checkRunTimePermission(final Activity activity, final String[] permissions, final int requestCode){

        //single permission
        if(permissions.length==1){

            // showing permission dialog for second time
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permissions[0])){

                Snackbar.make(findViewById(android.R.id.content),"Grant Permission Need",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Enable", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(activity,permissions,requestCode);
                            }
                        }).show();
            }else{
                ActivityCompat.requestPermissions(activity,permissions,requestCode);
            }
        }
        //multiple permission
        else if(permissions.length>1){

            List<String> deniadPermissions=new ArrayList<>();
            for (String permission:permissions){

                if(ContextCompat.checkSelfPermission(activity,permission)== PackageManager.PERMISSION_DENIED){
                    deniadPermissions.add(permission);
                }
            }
            final String temp[]=deniadPermissions.toArray(new String[deniadPermissions.size()]);
            if(deniadPermissions.size()==1){

                if(ActivityCompat.shouldShowRequestPermissionRationale(activity,deniadPermissions.get(0))){

                    Snackbar.make(findViewById(android.R.id.content),"App need Mulitple Permission",Snackbar.LENGTH_INDEFINITE)
                            .setAction("Enable", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(activity,temp,requestCode);
                                }
                            }).show();
                }
            }

            else if(deniadPermissions.size()>1){

                if(isFirstTimeAskPermission()){
                    ActivityCompat.requestPermissions(activity,temp,requestCode);
                }else{

                    Snackbar.make(findViewById(android.R.id.content),"App need Mulitple Permission",Snackbar.LENGTH_INDEFINITE)
                            .setAction("Enable", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(activity,temp,requestCode);
                                }
                            }).show();
                }
            }
        }
    }



    private boolean isFirstTimeAskPermission(){
        SharedPreferences sharedPreferences=getSharedPreferences("askpermission",MODE_PRIVATE);
        boolean isFirstTime=sharedPreferences.getBoolean("FIRST_TIME_PERMISSION",true);

        if(isFirstTime){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("FIRST_TIME_PERMISSION",false);
            editor.commit();
        }
        return isFirstTime;
    }


    protected String[] AllDeniedPermission(){
        String[] deniedpemission;
        List<String> deniedArrayList=new ArrayList<>();
        for(String permission: multiplePermission){
            if(ContextCompat.checkSelfPermission(this,permission)==PackageManager.PERMISSION_DENIED){
                deniedArrayList.add(permission);
            }
        }

        deniedpemission=deniedArrayList.toArray(new String[deniedArrayList.size()]);
        return deniedpemission;
    }

    protected boolean isAllPermissionGranted(){
        for(String permission:multiplePermission){
            if(ContextCompat.checkSelfPermission(this,permission)==PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }
}
