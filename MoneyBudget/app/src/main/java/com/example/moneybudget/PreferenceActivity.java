package com.example.moneybudget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
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
    EditText User_name,dob;
    private Toolbar settingsToolbar;

    private FirebaseAuth mAuth;
    Spinner currency_pref;
    Button button;
    SharedPreferences sp;
    String CurStr,Name2Str,Dob2Str;

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
        dob = findViewById(R.id.dob);
        User_name = findViewById(R.id.User_name);

        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurStr = currency_pref.getSelectedItem().toString();
                Name2Str = User_name.getText().toString();
                Dob2Str = dob.getText().toString();

                SharedPreferences.Editor editor = sp.edit();

                editor.putString("Currency",CurStr);
                editor.putString("Name",Name2Str);
                editor.putString("DOB",Dob2Str);
                editor.commit();
                Toast.makeText(PreferenceActivity.this,"Information Saved",Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(PreferenceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}