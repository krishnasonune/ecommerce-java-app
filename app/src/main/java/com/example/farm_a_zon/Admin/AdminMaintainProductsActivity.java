package com.example.farm_a_zon.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.farm_a_zon.R;
import com.example.farm_a_zon.Seller.SellerProductCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private EditText mainProdName, mainProdDesc, mainProdPrice;
    private Button applyChnBtn, deleteProdBtn;
    private ImageView mainImageView;
    private String productID = "";
    private DatabaseReference prodRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("pid");
        prodRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        mainProdPrice = (EditText) findViewById(R.id.maint_product_item_price);
        mainProdName = (EditText) findViewById(R.id.maint_product_item_name);
        mainProdDesc = (EditText) findViewById(R.id.maint_product_item_description);
        mainImageView = (ImageView) findViewById(R.id.maint_product_item_image);
        applyChnBtn = (Button) findViewById(R.id.maint_product_btn);
        deleteProdBtn = (Button) findViewById(R.id.delete_product_btn);


        displaySpecificProdInfo();



        applyChnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });




        deleteProdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });




    }

    private void deleteThisProduct() {

        prodRef.removeValue().
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminMaintainProductsActivity.this, "Product Deleted SuccessFully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AdminMaintainProductsActivity.this, SellerProductCategoryActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                });

    }







    private void applyChanges() {

        String pName = mainProdName.getText().toString();
        String pPrice = mainProdPrice.getText().toString();
        String pDesc = mainProdDesc.getText().toString();

        if (TextUtils.isEmpty(pName)) {
            Toast.makeText(this, "Product name cannot be null", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pPrice)) {
            Toast.makeText(this, "Product price cannot be null", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pDesc)) {
            Toast.makeText(this, "Product description cannot be null", Toast.LENGTH_SHORT).show();
        }
        else{

            HashMap<String, Object> prodData = new HashMap<>();
            prodData.put("pid", productID);
            prodData.put("description", pDesc);
            prodData.put("prodname", pName);
            prodData.put("price", pPrice);

            prodRef.updateChildren(prodData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(AdminMaintainProductsActivity.this, "Changes Applied Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(AdminMaintainProductsActivity.this, SellerProductCategoryActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });

        }


    }






    private void displaySpecificProdInfo() {
        prodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("prodname").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();
                    String image = snapshot.child("Image").getValue().toString();

                    mainProdName.setText(name);
                    mainProdPrice.setText(price);
                    mainProdDesc.setText(description);
                    Picasso.get().load(image).into(mainImageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}