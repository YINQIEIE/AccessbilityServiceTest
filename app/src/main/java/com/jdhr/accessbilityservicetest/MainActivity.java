package com.jdhr.accessbilityservicetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvStartAccessibilityService;

    private void assignViews() {
        tvStartAccessibilityService = (TextView) findViewById(R.id.tv_startAccessibilityService);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
