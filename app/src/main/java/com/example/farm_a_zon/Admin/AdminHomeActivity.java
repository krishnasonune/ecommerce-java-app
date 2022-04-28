package com.example.farm_a_zon.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.farm_a_zon.R;
import com.example.farm_a_zon.buyer.HomeActivity;
import com.example.farm_a_zon.buyer.MainActivity;
import com.rey.material.widget.Button;

public class AdminHomeActivity extends AppCompatActivity {

    private Button checknewordbtn, logoutbtn, maintainBtn, chkAprProvBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logoutbtn = (Button) findViewById(R.id.admin_logout_btn);
        checknewordbtn = (Button) findViewById(R.id.chk_ord_btn);
        maintainBtn = (Button) findViewById(R.id.maintain_btn);
        chkAprProvBtn = (Button) findViewById(R.id.admin_approve_btn);




        maintainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);

            }
        });



        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checknewordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });


        
        chkAprProvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, CheckNewProductsActivity.class);
                startActivity(intent);
            }
        });
        


    }
}