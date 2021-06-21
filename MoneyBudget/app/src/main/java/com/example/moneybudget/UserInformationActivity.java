package com.example.moneybudget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;

public class UserInformationActivity extends AppCompatActivity {

    private TextView userEmail,Name,Dob_user,Def_cur;
    Button back_main,export;

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
        export = findViewById(R.id.export);


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

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder data = new StringBuilder();
                data.append("Name" + "," + sp.getString("Name", ""));
                data.append("\n"+"DOB" + "," + sp.getString("DOB", "MM/DD/YYYY"));
                data.append("\n"+"Default Currency" + "," + sp.getString("Currency", ""));
                try {
                    //saving the file into the device
                    FileOutputStream out = openFileOutput("user_data.csv",Context.MODE_PRIVATE);
                    out.write((data.toString()).getBytes());
                    out.close();

                    //exporting
                    Context context = getApplicationContext();
                    File filelocation = new File(getFilesDir(),"user_data.csv");
                    Uri path = FileProvider.getUriForFile(context,"com.example.moneybudget.fileprovider",filelocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT,"Data");
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM,path);
                    startActivity(Intent.createChooser(fileIntent,"User Details"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}