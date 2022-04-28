package com.example.farm_a_zon.Seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farm_a_zon.Admin.CheckNewProductsActivity;
import com.example.farm_a_zon.Models.Products;
import com.example.farm_a_zon.R;
import com.example.farm_a_zon.Viewolder.itemViewHolder;
import com.example.farm_a_zon.Viewolder.viewProductHolder;
import com.example.farm_a_zon.buyer.MainActivity;
import com.example.farm_a_zon.databinding.ActivitySellerHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SellerHomeActivity extends AppCompatActivity {

    private ActivitySellerHomeBinding binding;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unVerifyProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySellerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        unVerifyProd = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = (RecyclerView) findViewById(R.id.seller_home_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logouts)
                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_seller_home);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.navigation_logouts) {
                    final FirebaseAuth oauth;
                    oauth = FirebaseAuth.getInstance();
                    oauth.signOut();

                    Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                    intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    return true;
                }
                if(i == R.id.navigation_add){

                    Intent intent = new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                    startActivity(intent);

                    return true;
                }

                if(i == R.id.navigation_home){

                    Intent intent = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                    startActivity(intent);

                    return true;
                }



                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unVerifyProd.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class).build();


        FirebaseRecyclerAdapter<Products, itemViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, itemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull itemViewHolder viewProductHolder, int i, @NonNull Products products) {
                        viewProductHolder.txtProductName.setText(products.getProdname());
                        viewProductHolder.txtProductDecsp.setText(products.getDescription());
                        viewProductHolder.txtProductStatus.setText("Status : " + products.getProductstatus());
                        viewProductHolder.txtProductPrice.setText("price " + products.getPrice() + " Rs");
                        Picasso.get().load(products.getImage()).into(viewProductHolder.productImage);

                        viewProductHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String productID = products.getPid();

                                CharSequence Options[] = new CharSequence[]{
                                        "Yes",
                                        "No"
                                };


                                AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Do you Want to Delete this Product");
                                builder.setItems(Options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            deleteProduct(productID);
                                        }
                                        else if (which == 1) {

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.seller_item_view, parent, false);

                        itemViewHolder holder = new itemViewHolder(view);

                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    private void deleteProduct(String productID) {
        unVerifyProd.child(productID)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SellerHomeActivity.this, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}