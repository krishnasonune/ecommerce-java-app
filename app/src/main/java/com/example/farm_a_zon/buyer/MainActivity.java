package com.example.farm_a_zon.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.farm_a_zon.Models.Users;
import com.example.farm_a_zon.Prevalent.prevalent;
import com.example.farm_a_zon.R;
import com.example.farm_a_zon.Seller.SellerHomeActivity;
import com.example.farm_a_zon.Seller.SellerRegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rey.material.widget.TextView;

import java.security.Permission;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton , logInButton;
    private ProgressDialog loader;
    private TextView sellerBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = (Button) findViewById(R.id.join_now_btn);
        logInButton = (Button) findViewById(R.id.login_btn);
        loader = new ProgressDialog(this);
        sellerBegin = (TextView) findViewById(R.id.seller_begin);

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();


        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });



        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                startActivity(intent);
            }
        });

        Paper.init(this);

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String userPhone = Paper.book().read(prevalent.userphonekey);
        String userPassword = Paper.book().read(prevalent.userpasswdkey);
        if (userPhone != "" && userPassword != ""){
            if (!TextUtils.isEmpty(userPhone) && !TextUtils.isEmpty(userPassword)){
                AllowAccess(userPhone, userPassword);

                loader.setTitle("Already Logged in");
                loader.setMessage("taking you to the home page, please wait");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {

            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

    }

    private void AllowAccess(String userPhone, String userPassword) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.child("Users").child(userPhone).exists() ){
                    Users userData = snapshot.child("Users").child(userPhone).getValue(Users.class);

                    if (userData.getPhone().equals(userPhone)){

                        if (userData.getPassword().equals(userPassword)){
                            Toast.makeText(MainActivity.this,"Log in successful", Toast.LENGTH_SHORT).show();
                            loader.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Incorrect password", Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "User with this number " + userPhone + " does not exist", Toast.LENGTH_SHORT).show();
                    loader.dismiss();

                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}