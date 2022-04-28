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

import com.example.farm_a_zon.Models.Cart;
import com.example.farm_a_zon.Prevalent.prevalent;
import com.example.farm_a_zon.R;
import com.example.farm_a_zon.Viewolder.cartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

public class CartActivity extends AppCompatActivity {

    private TextView totalPrice, msg1;
    private Button next;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private int overtotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        totalPrice = (TextView) findViewById(R.id.total_price);
        msg1 = (TextView) findViewById(R.id.msg1);
        next = (Button) findViewById(R.id.next_process_button);
        recyclerView = (RecyclerView) findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPrice.setText("Total Price " +  String.valueOf(overtotalPrice));

                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overtotalPrice));
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        OrderState();

        final DatabaseReference cartList = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartList.child("User View").child(prevalent.phone).child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, cartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, cartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull cartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                cartViewHolder.txtProdPrice.setText(cart.getPrice());
                cartViewHolder.txtProdName.setText(cart.getProdname());
                cartViewHolder.txtProdQuantity.setText(cart.getQuantity());

                int onetypeOrder = ((Integer.valueOf(cart.getPrice()))) * Integer.valueOf(cart.getQuantity());
                overtotalPrice = overtotalPrice + onetypeOrder;

                totalPrice.setText("Total Price " +  String.valueOf(overtotalPrice));

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence opt[] = new CharSequence[] {
                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);

                        builder.setTitle("Cart Options");
                        builder.setItems(opt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", cart.getPid());
                                    startActivity(intent);
                                }
                                else if(which == 1) {
                                    cartList.child("User View").child(prevalent.phone)
                                            .child("Products").child(cart.getPid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Product removed from your cart", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                    else{
                                                        Toast.makeText(CartActivity.this, "NetWork Error, Try Again Later", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                cartViewHolder holder = new cartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        }

    private void OrderState(){
        final DatabaseReference orderstateref = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(prevalent.phone);

        orderstateref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shippingStatus = snapshot.child("status").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();

                    if (shippingStatus.equals("shipped")) {
                        totalPrice.setText("Order Status");
                        recyclerView.setVisibility(View.GONE);
                        msg1.setText("your final order has been shipped successfully, soon seller will verify it");
                        msg1.setVisibility(View.VISIBLE );
                        next.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "you can add more products once your previous purchase is delieverd", Toast.LENGTH_SHORT).show();
                    }
                    else if (shippingStatus.equals("not shipped")) {
                        totalPrice.setText("Order Status");
                        recyclerView.setVisibility(View.GONE);

                        msg1.setVisibility(View.VISIBLE);
                        next.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "your final order place successfully, it will reach soon", Toast.LENGTH_SHORT).show();
                    }
                    else {

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

