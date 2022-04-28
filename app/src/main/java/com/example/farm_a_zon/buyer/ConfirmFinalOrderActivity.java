package com.example.farm_a_zon.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.farm_a_zon.Prevalent.prevalent;
import com.example.farm_a_zon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEdtext, phnEdtext, addEdtext, cityEdtext;
    private Button finalordbtn;
    private String totalAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmt = getIntent().getStringExtra("Total Price");

        nameEdtext = (EditText) findViewById(R.id.cfo_name);
        phnEdtext = (EditText) findViewById(R.id.cfo_phn);
        addEdtext = (EditText) findViewById(R.id.cfo_add);
        cityEdtext = (EditText) findViewById(R.id.cfo_city);
        finalordbtn = (Button) findViewById(R.id.final_ordr_btn);

        finalordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate();
            }
        });


    }

    private void Validate() {
        if (TextUtils.isEmpty(nameEdtext.getText().toString())) {
            Toast.makeText(ConfirmFinalOrderActivity.this, "Name Is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phnEdtext.getText().toString())) {
            Toast.makeText(ConfirmFinalOrderActivity.this, "Phone number Is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addEdtext.getText().toString())) {
            Toast.makeText(ConfirmFinalOrderActivity.this, "Address Is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEdtext.getText().toString())) {
            Toast.makeText(ConfirmFinalOrderActivity.this, "City Name Is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else{
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {

        final String saveCurrentTime, saveCurrentDate;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentdate.format(cal.getTime());


        SimpleDateFormat currenttime = new SimpleDateFormat("HH:MM:ss a");
        saveCurrentTime = currentdate.format(cal.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(prevalent.phone);

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("total_amount", totalAmt);
        orderMap.put("name", nameEdtext.getText().toString());
        orderMap.put("phone", phnEdtext.getText().toString());
        orderMap.put("address", addEdtext.getText().toString());
        orderMap.put("city", cityEdtext.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("status", "Not shipped");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().
                            child("Cart List").child("User View")
                            .child(prevalent.phone).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Your Order Is Placed Successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, PlaceOrderedActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("total", String.valueOf(totalAmt));
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}