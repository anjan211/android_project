package com.example.moneybudget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserInformationActivity extends AppCompatActivity {

    private TextView userEmail,Name,Dob_user,Def_cur;
    Button back_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_user_information);


        userEmail  = findViewById(R.id.userEmail);
        Name = findViewById(R.id.Name);
        Dob_user = findViewById(R.id.Dob_user);
        Def_cur = findViewById(R.id.Def_cur);
        back_main = findViewById(R.id.back_main);


        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        Def_cur.setText(sp.getString("Currency",""));
        Name.setText(sp.getString("Name",""));
        Dob_user.setText(sp.getString("DOB","MM/DD/YYYY"));

        back_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInformationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}