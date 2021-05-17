package com.example.moneybudget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TodayItemsAdapter  extends  RecyclerView.Adapter<TodayItemsAdapter.ViewHolder>{

    private Context myContext;
    private List<Data> myDataList;
    private String post_key = "";
    private String item = "";
    private int amount = 0;
    private String note = "";
    private String payment = "";


    public TodayItemsAdapter(Context myContext, List<Data> myDataList) {
        this.myContext = myContext;
        this.myDataList = myDataList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.retrieve_layout_expense,parent,false);

        return new TodayItemsAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Data data = myDataList.get(position);

        holder.item.setText("Item:" + data.getItem());
        holder.amount.setText("Amount:" + data.getAmount());
        holder.payment.setText("Payment:" + data.getPayment());
        holder.date.setText("On:"+data.getDate());
        holder.notes.setText("Note :"+data.getNotes());


        switch(data.getItem()){
            case "Transport":
                holder.imageView.setImageResource(R.drawable.ic_transport);
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.ic_food);
                break;
            case "House":
                holder.imageView.setImageResource(R.drawable.ic_house);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.ic_entertainment);
                break;
            case "Education":
                holder.imageView.setImageResource(R.drawable.ic_education);
                break;
            case "Charity":
                holder.imageView.setImageResource(R.drawable.ic_consultancy);
                break;
            case "Apparel":
                holder.imageView.setImageResource(R.drawable.ic_shirt);
                break;
            case "Health":
                holder.imageView.setImageResource(R.drawable.ic_health);
                break;
            case "Personal":
                holder.imageView.setImageResource(R.drawable.ic_personalcare);
                break;
                default:
                holder.imageView.setImageResource(R.drawable.ic_other);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_key = data.getId();
                item = data.getItem();
                amount = data.getAmount();
                payment = data.getPayment();
                note = data.getNotes();
                updateData();
            }
        });


    }

    private void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(myContext);
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View myView = inflater.inflate(R.layout.update_layout_expense,null);

        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();

        final TextView mItem = myView.findViewById(R.id.itemName);
        final EditText mAmount = myView.findViewById(R.id.amount);
        final EditText mNotes = myView.findViewById(R.id.note);
        final Spinner Paymentspinner = myView.findViewById(R.id.paymentspinner);

        mNotes.setText(note);

        mItem.setText(item);

        mAmount.setText(String.valueOf(amount));

        Button delBut = myView.findViewById(R.id.delete);
        Button UpdBut = myView.findViewById(R.id.update);

        UpdBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amount = Integer.parseInt(mAmount.getText().toString());
                note = mNotes.getText().toString();
                payment = Paymentspinner.getSelectedItem().toString();

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Weeks weeks = Weeks.weeksBetween(epoch, now);
                Months months = Months.monthsBetween(epoch, now);

                String itemNday = item+date;
                String itemNweek= item+weeks.getWeeks();
                String itemNmonth = item+months.getMonths();

                Data data = new Data(item,date,post_key,note,payment,"Euros",itemNday,itemNweek,itemNmonth,amount,months.getMonths(),weeks.getWeeks());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenditure").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(myContext, "Updated successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(myContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        delBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenditure").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(myContext, "Deleted successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(myContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();

            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item,amount,date,payment,notes;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            payment = itemView.findViewById(R.id.payment);
            notes = itemView.findViewById(R.id.note);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
