package com.example.farm_a_zon.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.farm_a_zon.Models.Products;
import com.example.farm_a_zon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.ImageView;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;
import com.example.farm_a_zon.Prevalent.prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private FloatingActionButton addToCart;
    private ImageView productImage;
    private TextView prodName, prodDesc, prodPrice;
    private ElegantNumberButton noOfitemsbtn;

    private String productId = "", status = "normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("pid");

        addToCart = (FloatingActionButton) findViewById(R.id.add_prod_cart_btn);
        noOfitemsbtn = (ElegantNumberButton) findViewById(R.id.elegant_num_btn);
        productImage = (ImageView) findViewById(R.id.product_details_image);
        prodName = (TextView) findViewById(R.id.product_details_name);
        prodDesc = (TextView) findViewById(R.id.product_details_desc);
        prodPrice = (TextView) findViewById(R.id.product_details_price);

        getProductDetails(productId);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status.equals("Order shipped") || status.equals("Order Placed")) {
                    Toast.makeText(ProductDetailsActivity.this, "your can purchase more products once it is shipped or confirmed", Toast.LENGTH_LONG).show();
                }
                else{
                    AddingToCartList();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        OrderState();
    }

    private void AddingToCartList() {

        String saveCurrentTime, saveCurrentDate;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentdate.format(cal.getTime());


        SimpleDateFormat currenttime = new SimpleDateFormat("HH:MM:ss a");
        saveCurrentTime = currentdate.format(cal.getTime());

        final DatabaseReference cartList = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartprod = new HashMap<>();
        cartprod.put("pid", productId);
        cartprod.put("prodname", prodName.getText().toString());
        cartprod.put("price", prodPrice.getText().toString());
        cartprod.put("quantity", noOfitemsbtn.getNumber());
        cartprod.put("date", saveCurrentDate);
        cartprod.put("time", saveCurrentTime);
        cartprod.put("discount", "");

        cartList.child("User View").child(prevalent.phone).child("Products").
                child(productId).updateChildren(cartprod)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (task.isSuccessful()) {
                                cartList.child("Admin View").child(prevalent.phone).child("Products").
                                        child(productId).updateChildren(cartprod)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);

                                            }
                                        });
                            }
                        }
                    }
                });
    }

    private void getProductDetails(String productId) {
        DatabaseReference prodref = FirebaseDatabase.getInstance().getReference().child("Products");

        prodref.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);
                    prodName.setText(products.getProdname());
                    prodDesc.setText(products.getDescription());
                    prodPrice.setText(products.getPrice());

                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }

        );
    }

    private void OrderState(){
        final DatabaseReference orderstateref = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(prevalent.phone);

        orderstateref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shippingStatus = snapshot.child("status").getValue().toString();

                    if (shippingStatus.equals("shipped")) {
                        status = "Order shipped";
                    }
                    else if (shippingStatus.equals("not shipped")) {
                        status = "Order Placed";
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