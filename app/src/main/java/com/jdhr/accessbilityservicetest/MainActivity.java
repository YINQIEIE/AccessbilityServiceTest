package com.jdhr.accessbilityservicetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private TextView tvStartAccessibilityService;
    private TextView tvInstall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        tvStartAccessibilityService.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        tvInstall.setOnClickListener(v -> autoInstall());
    }

    private void assignViews() {
        tvStartAccessibilityService = (TextView) findViewById(R.id.tv_startAccessibilityService);
        tvInstall = findViewById(R.id.tv_install);
    }

    public void autoInstall() {
        //sd 卡路径下放入名为 test 的安装包
        String apkPath = Environment.getExternalStorageDirectory() + "/test.apk";
        Uri uri = Uri.fromFile(new File(apkPath));
        Intent localIntent = new Intent(Intent.ACTION_VIEW);
        localIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(localIntent);
    }
}
