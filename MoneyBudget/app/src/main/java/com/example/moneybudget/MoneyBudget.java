package com.example.moneybudget;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MoneyBudget extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
