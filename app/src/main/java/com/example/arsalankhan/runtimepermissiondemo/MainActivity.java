package com.example.arsalankhan.runtimepermissiondemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void SinglePermission(View view){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission Granted, GO Ahead", Toast.LENGTH_SHORT).show();
        }else{
            checkRunTimePermission(this,new String[]{Manifest.permission.READ_CONTACTS},CONTACT_REQUEST_CODE);
        }
    }

    public void MultiplePermission(View view){

        if(isAllPermissionGranted()){
            Toast.makeText(this, "All permissin Granted", Toast.LENGTH_SHORT).show();
        }else{
            checkRunTimePermission(this,multiplePermission,WRITE_REQUEST_CODE);
        }

    }

    public void GoSetting(View view){

        Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package",getPackageName(),""));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(permissions.length==1){
            if(requestCode==CONTACT_REQUEST_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission GrANTED", Toast.LENGTH_SHORT).show();
            }else{
                checkRunTimePermission(this,new String[]{Manifest.permission.READ_CONTACTS},CONTACT_REQUEST_CODE);
            }
        }

        else if(permissions.length>1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED&& requestCode==WRITE_REQUEST_CODE){
                if(isAllPermissionGranted()){
                    Toast.makeText(this, "Multiple Permission Granted", Toast.LENGTH_SHORT).show();
                }
            }
            else if(grantResults[0]==PackageManager.PERMISSION_DENIED && requestCode==WRITE_REQUEST_CODE){
                checkRunTimePermission(this,AllDeniedPermission(),WRITE_REQUEST_CODE);
            }
        }

    }
}
