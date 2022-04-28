package com.example.farm_a_zon.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.farm_a_zon.Admin.AdminNewOrdersActivity;
import com.example.farm_a_zon.Admin.AdminUserProductActivity;
import com.example.farm_a_zon.Models.AdminOrders;
import com.example.farm_a_zon.Models.Orders;
import com.example.farm_a_zon.Prevalent.prevalent;
import com.example.farm_a_zon.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.Button;
import com.rey.material.widget.RelativeLayout;
import com.rey.material.widget.TextView;

public class PlaceOrderedActivity extends AppCompatActivity {

    private DatabaseReference ordref;
    private TextView userName, userPhone, userAddress, userDateTime, userStatus, Backup, totalPrice;
    private Button orderDetailsbtn, canOrdBtn;
    private RelativeLayout card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_ordered);

        ordref = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(prevalent.phone);

        userName = (TextView) findViewById(R.id.order_user_name2);
        userPhone = (TextView) findViewById(R.id.ord_phn_num2);
        userAddress = (TextView) findViewById(R.id.ord_add_city2);
        userDateTime = (TextView) findViewById(R.id.ord_date_time2);
        userStatus = (TextView) findViewById(R.id.ord_status_2);
        Backup = (TextView) findViewById(R.id.backup);
        totalPrice = (TextView) findViewById(R.id.ord_totalprice2);
        card = (RelativeLayout) findViewById(R.id.plac_ord_card);
        orderDetailsbtn = (Button) findViewById(R.id.ord_details_btn2);
        canOrdBtn = (Button) findViewById(R.id.ord_cancel_btn2);

        orderDetailsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaceOrderedActivity.this, AdminUserProductActivity.class);
                intent.putExtra("uid", prevalent.phone);
                startActivity(intent);
            }

        });

        canOrdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence opt[] = new CharSequence[]{
                        "yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrderedActivity.this);
                builder.setTitle("Sure you want to Cancel Order?");
                builder.setItems(opt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            ordref.removeValue();
                            Toast.makeText(PlaceOrderedActivity.this, "Your order Cancel Successfully ", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(PlaceOrderedActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else if (which == 1) {

                        }
                        else{
                            finish();
                        }
                    }
                });

                builder.show();

            }


        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        OrderDisplay();
    }

    private void OrderDisplay(){



        ordref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    String phone = snapshot.child("phone").getValue().toString();
                    String address = snapshot.child("address").getValue().toString();
                    String date = snapshot.child("date").getValue().toString();
                    String status = snapshot.child("status").getValue().toString();
                    String total_price = snapshot.child("total_amount").getValue().toString();
//                            getIntent().getStringExtra("total").toString();


                    userName.setText("Name: " + name);
                    userPhone.setText(phone);
                    userAddress.setText("Address: " + address);
                    userDateTime.setText("Date: " + date);
                    totalPrice.setText("Price: " + total_price);
                    userStatus.setText("status: " + status);

                }
                else {
                    card.setVisibility(View.INVISIBLE);
                    Backup.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }





}