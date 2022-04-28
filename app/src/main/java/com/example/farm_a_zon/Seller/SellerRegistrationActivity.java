package com.example.farm_a_zon.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.farm_a_zon.R;
import com.example.farm_a_zon.buyer.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private FirebaseAuth oAuth;
    private ProgressDialog loader;
    private Button sellerAlreadyAccLoginBtn, sellerRegBtn;
    private EditText sellerNameEditText,sellerPhoneEditText, sellerPwdEditText,
                    sellerAddEditText,sellerEmailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        oAuth = FirebaseAuth.getInstance();

        sellerAlreadyAccLoginBtn = (Button) findViewById(R.id.seller_already_acc_btn);
        sellerRegBtn = (Button) findViewById(R.id.seller_reg_btn);
        sellerNameEditText = (EditText) findViewById(R.id.seller_name);
        sellerPhoneEditText = (EditText) findViewById(R.id.seller_phone_num);
        sellerPwdEditText = (EditText) findViewById(R.id.seller_pswd);
        sellerEmailEditText = (EditText) findViewById(R.id.seller_email);
        sellerAddEditText = (EditText) findViewById(R.id.seller_shop_add);
        loader = new ProgressDialog(this);



        sellerAlreadyAccLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });


        sellerRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerSeller();


            }
        });


    }

    private void registerSeller() {
        String name = sellerNameEditText.getText().toString();
        String phn = sellerPhoneEditText.getText().toString();
        String pass = sellerPwdEditText.getText().toString();
        String email = sellerEmailEditText.getText().toString();
        String add = sellerAddEditText.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(phn)) {
            Toast.makeText(this, "phone cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(add)) {
            Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else{

            loader.setTitle("Creating Seller Account");
            loader.setMessage("Please wait, while we are creating your account");
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            oAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                DatabaseReference rootRef;
                                rootRef = FirebaseDatabase.getInstance().getReference();

                                String sId = oAuth.getCurrentUser().getUid();

                                HashMap<String, Object> sellerMap = new HashMap<>();
                                sellerMap.put("sid", sId);
                                sellerMap.put("name", name);
                                sellerMap.put("email", email);
                                sellerMap.put("phone", phn);
                                sellerMap.put("password", pass);
                                sellerMap.put("address", add);

                                rootRef.child("Sellers").child(sId).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    loader.dismiss();
                                                    Toast.makeText(SellerRegistrationActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                                                    intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();


                                                }
                                            }
                                        });

                            }
                        }
                    });


        }
        
        
    }
}