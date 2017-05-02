package com.lwh8762.simplememo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSION_REQUEST = 1746;

    private Switch powerSwh = null;
    private Intent serviceIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        demandPermission();

        PrefManager.setSharedPreferences(getSharedPreferences("shared_pref", Context.MODE_PRIVATE));

        serviceIntent = new Intent(this, MemoService.class);

        powerSwh = (Switch) findViewById(R.id.powerSwh);
        powerSwh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (powerSwh.isChecked()) {
                    startService(serviceIntent);
                }else {
                    stopService(serviceIntent);
                }
                PrefManager.setPower(powerSwh.isChecked());
            }
        });

        powerSwh.setChecked(PrefManager.powerEnabled());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST) {
            if(!(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void demandPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            if(!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECEIVE_BOOT_COMPLETED}, PERMISSION_REQUEST);
            }
        }
    }
}
