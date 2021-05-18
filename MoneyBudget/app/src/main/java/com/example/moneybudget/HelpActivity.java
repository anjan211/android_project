package com.example.moneybudget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

public class HelpActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setTitle("Help");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewFlipper = findViewById(R.id.view_flipper);
//        viewFlipper.setFlipInterval(5000);
//        viewFlipper.startFlipping();
    }

    public void nextView(View view) {
        viewFlipper.showNext();
    }

    public void previousView(View view) {
        viewFlipper.showPrevious();
    }
}
