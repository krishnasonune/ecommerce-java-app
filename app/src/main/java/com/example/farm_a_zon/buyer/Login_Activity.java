package com.example.farm_a_zon.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farm_a_zon.Admin.AdminHomeActivity;
import com.example.farm_a_zon.Models.Admins;
import com.example.farm_a_zon.Seller.SellerHomeActivity;
import com.example.farm_a_zon.Seller.SellerProductCategoryActivity;
import com.example.farm_a_zon.Models.Users;
import com.example.farm_a_zon.Prevalent.prevalent;
import com.example.farm_a_zon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login_Activity extends AppCompatActivity {

    private EditText InputNumber, InputPassword;
    private Button loginbtn;
    private ProgressDialog loader;
    private String parentDb = "Users";
    private com.rey.material.widget.CheckBox Rememberme;
    private TextView adminLink, notAdminLink, forgetPswd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InputNumber = (EditText) findViewById(R.id.input_phone_no);
        InputPassword = (EditText) findViewById(R.id.login_pswd);
        loginbtn = (Button) findViewById(R.id.loginpg_login_btn);
        loader = new ProgressDialog(this);
        Rememberme = (com.rey.material.widget.CheckBox) findViewById(R.id.remember_me_chkbox);
        Paper.init(this);
        adminLink = (TextView) findViewById(R.id.admin_panel_link);
        notAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        forgetPswd = (TextView) findViewById(R.id.forget_pswd);



        forgetPswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });




        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();


            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbtn.setText("login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDb = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbtn.setText("LOGIN");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDb = "Users";
            }
        });
    }

    private void loginUser() {
        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(Login_Activity.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(Login_Activity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
        }else {
            loader.setTitle("login");
            loader.setMessage("please wait, while we are checking your credentials");
            loader.setCanceledOnTouchOutside(false);
            loader.show();
            
            AllowAccess(phone, password);
        }


    }

    private void AllowAccess(String phone, String password) {

        if (Rememberme.isChecked()){
            Paper.book().write(prevalent.userphonekey, phone);
            Paper.book().write(prevalent.userpasswdkey, password);

        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.child(parentDb).child(phone).exists() ){
                    Users userData = snapshot.child(parentDb).child(phone).getValue(Users.class);

                    if (userData.getPhone().equals(phone)){

                        if (userData.getPassword().equals(password)){
                           if (parentDb.equals("Admins")){
                               Toast.makeText(Login_Activity.this,"Log in successful", Toast.LENGTH_SHORT).show();
                               loader.dismiss();
                               Admins Data = snapshot.child(parentDb).child(phone).getValue(Admins.class);
                               prevalent.name= Data.getName();
                               prevalent.phone = Data.getPhone();

                               Intent intent = new Intent(Login_Activity.this, AdminHomeActivity.class);
                               startActivity(intent);
                           }
                           else if (parentDb.equals("Users")){
                               Toast.makeText(Login_Activity.this,"Log in successful", Toast.LENGTH_SHORT).show();
                               loader.dismiss();
                               prevalent.name= userData.getName();
                               prevalent.phone = userData.getPhone();

                               Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
                               startActivity(intent);
                           }
                        }
                        else{
                            Toast.makeText(Login_Activity.this,"Incorrect password", Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }
                    }

                }
                else{
                    Toast.makeText(Login_Activity.this, "User with this number " + phone + " does not exist", Toast.LENGTH_SHORT).show();
                    loader.dismiss();

                    Intent intent = new Intent(Login_Activity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}