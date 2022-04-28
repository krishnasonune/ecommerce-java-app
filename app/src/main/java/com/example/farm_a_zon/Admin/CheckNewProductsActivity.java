package com.example.farm_a_zon.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.farm_a_zon.Interfave.itemClickListener;
import com.example.farm_a_zon.Models.Products;
import com.example.farm_a_zon.R;
import com.example.farm_a_zon.Viewolder.viewProductHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CheckNewProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unVerifyProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_new_products);

        unVerifyProd = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = (RecyclerView) findViewById(R.id.admin_prod_chkList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unVerifyProd.orderByChild("productstatus").equalTo("Not Approved"), Products.class).build();


        FirebaseRecyclerAdapter<Products, viewProductHolder> adapter =
                new FirebaseRecyclerAdapter<Products, viewProductHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull viewProductHolder viewProductHolder, int i, @NonNull Products products) {
                        viewProductHolder.txtProductName.setText(products.getProdname());
                        viewProductHolder.txtProductDecsp.setText(products.getDescription());
                        viewProductHolder.txtProductPrice.setText("price " + products.getPrice() + " Rs");
                        Picasso.get().load(products.getImage()).into(viewProductHolder.productImage);

                        viewProductHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String productID = products.getPid();

                                CharSequence Options[] = new CharSequence[]{
                                        "Approve",
                                        "Decline"
                                };


                                AlertDialog.Builder builder = new AlertDialog.Builder(CheckNewProductsActivity.this);
                                builder.setTitle("Confirm Products");
                                builder.setItems(Options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            changeProductStatus(productID);
                                        }
                                        else if (which == 1) {
                                            chngProductStatus(productID);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public viewProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.product_items, parent, false);

                        viewProductHolder holder = new viewProductHolder(view);

                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void chngProductStatus(String productID) {

        unVerifyProd.child(productID)
                .child("productstatus")
                .setValue("Not Approved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CheckNewProductsActivity.this, "Product Declined Successfully, This product will not be Visible to end User", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void changeProductStatus(String productID) {

        unVerifyProd.child(productID)
                .child("productstatus")
                .setValue("Approved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CheckNewProductsActivity.this, "Product Approved Successfully, Now its Available for all Customer", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}