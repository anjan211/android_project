package com.example.moneybudget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PreferenceActivity extends AppCompatActivity {

    private TextView userEmail;
    private Toolbar settingsToolbar;
    Spinner currency_pref;
    Button button;
    SharedPreferences sp;
    String CurStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_preference);

        settingsToolbar = findViewById(R.id.my_Feed_Toolbar);

        settingsToolbar.setTitle("My Account");
        button = findViewById(R.id.save_pref);
        currency_pref = findViewById(R.id.currency_pref);
        userEmail  = findViewById(R.id.userEmail);

        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurStr = currency_pref.getSelectedItem().toString();

                SharedPreferences.Editor editor = sp.edit();

                editor.putString("Currency",CurStr);
                editor.commit();
                Toast.makeText(PreferenceActivity.this,"Information Saved",Toast.LENGTH_SHORT).show();
            }
        });
    }
}