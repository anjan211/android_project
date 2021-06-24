package com.example.moneybudget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class TodaySpendingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView totalAmountSpentOn;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ProgressDialog loader;

    private static final int RESULT_PICK_CONTACT = 1;
    private TextView viewContact;
    private Button selectContact;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expensesRef;

    private TodayItemsAdapter todayItemsAdapter;
    private List<Data> myDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_today_spending);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Today's Expenditures");
        totalAmountSpentOn = findViewById(R.id.totalAmountSpentOn);

        fab = findViewById(R.id.fab);
        loader = new ProgressDialog(this);



        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("Expenditure").child(onlineUserId);
        expensesRef.keepSynced(true);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        myDataList = new ArrayList<>();
        todayItemsAdapter = new TodayItemsAdapter(TodaySpendingActivity.this,myDataList);
        recyclerView.setAdapter(todayItemsAdapter);

        readItems();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemSpentOn();
            }
        });


    }


    private void readItems() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenditure").child(onlineUserId);
        Query query= reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDataList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);
                    myDataList.add(data);
                }

                todayItemsAdapter.notifyDataSetChanged();

                int totalAmount = 0;
                for(DataSnapshot ds: snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;

                    totalAmountSpentOn.setText("Total Day's Spending : " +totalAmount);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addItemSpentOn() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout_expense, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);
        ArrayList<String> list = new ArrayList<>();

        final Spinner itemSpinner = myView.findViewById(R.id.itemsspinner);
        final Spinner paymentSpinner = myView.findViewById(R.id.paymentspinner);
        final EditText amount = myView.findViewById(R.id.amount);
        final EditText note = myView.findViewById(R.id.note);
        final Button Addbut = myView.findViewById(R.id.Addbut);
        final Button cancel = myView.findViewById(R.id.cancel);
        final EditText add_bi = myView.findViewById(R.id.additem);
        final Button save = myView.findViewById(R.id.save);
        final Spinner currencySpinner = myView.findViewById(R.id.currencyspinner);

        Addbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.add(add_bi.getText().toString());
            }
        });

        list.add("Select Item");
        list.add("Transport");
        list.add("Entertainment");
        list.add("Charity");
        list.add("Food");
        list.add("House");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.notifyDataSetChanged();
        itemSpinner.setAdapter(adapter);

//      final SwitchCompat switchCompat=myView.findViewById(R.id.switchRepeatedTransaction);
        final Spinner selectRecurring = myView.findViewById(R.id.selectRecurring);

        viewContact = myView.findViewById(R.id.viewContact);
        selectContact = myView.findViewById(R.id.selectContact);

//        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    Toast.makeText(getBaseContext(), "Recurring transaction selected", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        List<String> recCategories = new ArrayList<>();
        recCategories.add(0, "Select period");
        recCategories.add("Weekly");
        recCategories.add("Monthly");
        recCategories.add("Yearly");

        ArrayAdapter<String> recAdapter;
        recAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, recCategories);
        recAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        selectRecurring.setAdapter(recAdapter);

        selectRecurring.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Select period")) {
                    // Do Nothing
                }

                else {

                    String recItem = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getBaseContext(), "Selected: "+ recItem, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        selectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, RESULT_PICK_CONTACT);
            }
        });


        note.setVisibility(View.VISIBLE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Amount = amount.getText().toString();
                String Item = itemSpinner.getSelectedItem().toString();
                String notes = note.getText().toString();
                String Payment = paymentSpinner.getSelectedItem().toString();
                String cur_pay = currencySpinner.getSelectedItem().toString();
                String phoneNo = viewContact.getText().toString();


                SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
                String cur = sp.getString("Currency","");

                switch (cur){
                    case "Euros":
                        switch (cur_pay){
                            case "Dollar":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 0.82));
                                break;

                            case "Pound":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 1.16));
                                break;

                            case "INR":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 0.011));
                                break;
                        }
                        break;

                    case "Pound":

                        switch (cur_pay){
                            case "Euros":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 0.86));
                                break;

                            case "Dollar":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 0.71));
                                break;

                            case "INR":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 0.0097));
                                break;
                        }
                        break;

                    case "INR":

                        switch (cur_pay){
                            case "Pound":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 102.75));
                                break;

                            case "Dollar":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 72.74));
                                break;

                            case "Euros":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 88.76));
                                break;
                        }
                        break;

                    case "Dollar":

                        switch (cur_pay){
                            case "Pound":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 1.41));
                                break;

                            case "INR":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 0.014));
                                break;

                            case "Euros":
                                Amount = String.valueOf((int) (Integer.parseInt(Amount) * 1.22));
                                break;
                        }
                        break;
                }


                if (TextUtils.isEmpty(Amount)){
                    amount.setError("Amount is required!");
                    return;
                }

                if (Item.equals("Select item")){
                    Toast.makeText(TodaySpendingActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                }

                if (Payment.equals("Select item")){
                    Toast.makeText(TodaySpendingActivity.this, "Select a valid mode of payment", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(notes)){
                    note.setError("Note is required");
                    return;
                }

                else {
                    loader.setMessage("Adding an expense item");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id  = expensesRef.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Weeks weeks = Weeks.weeksBetween(epoch, now);
                    Months months = Months.monthsBetween(epoch, now);

                    String itemNday = Item+date;
                    String itemNweek= Item+weeks.getWeeks();
                    String itemNmonth = Item+months.getMonths();

                    Data data = new Data(Item, date, id, notes,Payment,cur_pay,phoneNo,itemNday,itemNweek,itemNmonth,Integer.parseInt(Amount), months.getMonths(),weeks.getWeeks());
                    expensesRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(TodaySpendingActivity.this, "Expense item added successfuly", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(TodaySpendingActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            loader.dismiss();
                        }
                    });
                }
                dialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Toast.makeText(this, "Failed to pick contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            String phoneNo = null;
            Uri uri = data.getData ();
            cursor = getContentResolver ().query (uri, null, null,null,null);
            cursor.moveToFirst ();
            int phoneIndex = cursor.getColumnIndex (ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString (phoneIndex);

            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            viewContact.setText (contactName);


        } catch (Exception e) {
            e.printStackTrace ();
        }
    }




}