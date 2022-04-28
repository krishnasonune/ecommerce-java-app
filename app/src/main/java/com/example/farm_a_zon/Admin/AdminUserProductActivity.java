package com.example.farm_a_zon.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farm_a_zon.R;
import com.example.farm_a_zon.Viewolder.cartViewHolder;
import com.example.farm_a_zon.Models.Cart;
import com.example.farm_a_zon.Prevalent.prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProductActivity extends AppCompatActivity {

    private RecyclerView detOrderlist;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference detOrderRef;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_product);
        userId = getIntent().getStringExtra("uid");

        detOrderlist = (RecyclerView) findViewById(R.id.det_ord_list);
        detOrderlist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        detOrderlist.setLayoutManager(layoutManager);


        detOrderRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View").child(userId).child("Products");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new
                FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(detOrderRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, cartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, cartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull cartViewHolder cartViewHolder, int i, @NonNull Cart cart) {

                        cartViewHolder.txtProdPrice.setText("Price = " + cart.getPrice());
                        cartViewHolder.txtProdName.setText("Product " + cart.getProdname());
                        cartViewHolder.txtProdQuantity.setText("Quantity " + cart.getQuantity());

                    }

                    @NonNull
                    @Override
                    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.cart_item_layout, parent, false);
                        cartViewHolder holder = new cartViewHolder(view);
                        return holder;
                    }
                };

        detOrderlist.setAdapter(adapter);
        adapter.startListening();
    }
}