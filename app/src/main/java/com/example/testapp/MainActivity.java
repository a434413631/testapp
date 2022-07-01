package com.example.testapp;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        CustomView customView4 = findViewById(R.id.cus_view_of_four);
        CustomView cusViewOfSunday = findViewById(R.id.cus_view_of_sunday);
        customView4.setOnClickListener(v -> {
            cusViewOfSunday.reset(R.drawable.progressbar_ver,"#2D2F33",R.mipmap.ic_smile);
            customView4.onViewClick(R.drawable.progressbar_ver_on_selected,"#F36A1B",R.mipmap.ic_smile_orange_select);
        });
        cusViewOfSunday.setOnClickListener(v -> {
            customView4.reset(R.drawable.progressbar_ver_orange,"#2D2F33",R.mipmap.ic_orange_smile);
            cusViewOfSunday.onViewClick(R.drawable.progressbar_ver_on_selected2,"#52C873",R.mipmap.ic_smile_green);
        });
    }
}