package com.smbat.systemuihide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppUtils.hideSystemUI(this);
        AppUtils.setSystemUIListener(this);
        AppUtils.enableDrawOverApp(this);
    }
}
