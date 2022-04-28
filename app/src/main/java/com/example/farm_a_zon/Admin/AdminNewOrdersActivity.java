package com.example.farm_a_zon.Admin;

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

import com.example.farm_a_zon.Models.AdminOrders;
import com.example.farm_a_zon.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordList;
    private DatabaseReference ordref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ordref = FirebaseDatabase.getInstance().getReference().child("Orders");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordList = (RecyclerView) findViewById (R.id.ord_list);
        ordList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options = new
                FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordref, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrderHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrderHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderHolder adminOrderHolder, final int i, @NonNull AdminOrders adminOrders) {
                        adminOrderHolder.userName.setText("Name: " + adminOrders.getName());
                        adminOrderHolder.userPhone.setText("Phone: " + adminOrders.getPhone());
                        adminOrderHolder.userAddress.setText("Address: " + adminOrders.getAddress() + ", " + adminOrders.getCity());
                        adminOrderHolder.userDateTime.setText("Order at: " + adminOrders.getDate() + "  " + adminOrders.getTime());
                        adminOrderHolder.totalPrice.setText("Total Price = " + adminOrders.getTotal_amount());

                        adminOrderHolder.orderDetailsbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String UID = getRef(i).getKey();

                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductActivity.class);
                                intent.putExtra("uid", UID);
                                startActivity(intent);
                            }
                        });

                    adminOrderHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence opt[] = new CharSequence[]{

                                    "yes",
                                    "No"

                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                            builder.setTitle("Is this order shipped ?");
                            builder.setItems(opt, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        String UID = getRef(i).getKey();
                                        
                                        RemoveOrder(UID);
                                    }
                                    else if (which == 1) {
                                        Toast.makeText(AdminNewOrdersActivity.this, "Your customer need this Product, get shipped soon", Toast.LENGTH_SHORT).show();
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

                    @NonNull
                    @Override
                    public AdminOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.order_layout, parent, false);
                        return new AdminOrderHolder(view);
                    }



                };
        ordList.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String uid) {
        ordref.child(uid).removeValue();
    }

    public static class AdminOrderHolder extends RecyclerView.ViewHolder{

        public TextView userName, userPhone, userAddress, userDateTime, totalPrice;
        public Button orderDetailsbtn;

        public AdminOrderHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.order_user_name);
            userPhone = (TextView) itemView.findViewById(R.id.ord_phn_num);
            userAddress = (TextView) itemView.findViewById(R.id.ord_add_city);
            userDateTime = (TextView) itemView.findViewById(R.id.ord_date_time);
            totalPrice = (TextView) itemView.findViewById(R.id.ord_totalprice);
            orderDetailsbtn = (Button) itemView.findViewById(R.id.ord_details_btn);

        }
    }

}