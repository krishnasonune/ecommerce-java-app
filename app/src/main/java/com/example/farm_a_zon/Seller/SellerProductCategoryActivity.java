package com.example.farm_a_zon.Seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.farm_a_zon.R;

public class SellerProductCategoryActivity extends AppCompatActivity {

    private ImageView grains, seeds, pesticides, fertilizers, vegetables,equipments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);

        grains = (ImageView) findViewById(R.id.grains);
        seeds = (ImageView) findViewById(R.id.seeds);
        pesticides = (ImageView) findViewById(R.id.pesticides);
        fertilizers = (ImageView) findViewById(R.id.fertilizers);
        equipments = (ImageView) findViewById(R.id.equipments);
        vegetables = (ImageView) findViewById(R.id.vegetables);





        grains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                inte.putExtra("category", "grains");
                startActivity(inte);
            }
        });

        seeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                inte.putExtra("category", "seeds");
                startActivity(inte);
            }
        });

        pesticides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                inte.putExtra("category", "pesticides");
                startActivity(inte);
            }
        });


        fertilizers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                inte.putExtra("category", "fertilizers");
                startActivity(inte);
            }
        });

        equipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                inte.putExtra("category", "equipments");
                startActivity(inte);
            }
        });

        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                inte.putExtra("category", "vegetables");
                startActivity(inte);
            }
        });
    }
}