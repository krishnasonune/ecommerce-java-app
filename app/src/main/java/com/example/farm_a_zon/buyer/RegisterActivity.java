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
import android.widget.Toast;

import com.example.farm_a_zon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputPhnNo, InputPass, InputAdd;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.register_register_btn);
        InputName = (EditText) findViewById(R.id.register_username);
        InputPhnNo = (EditText) findViewById(R.id.register_phone_no);
        InputPass = (EditText) findViewById(R.id.register_pswd);
        InputAdd = (EditText) findViewById(R.id.register_add);
        loader = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                createAccount();
            }
        });
    }

    private void createAccount()
    {
        String name = InputName.getText().toString();
        String phone = InputPhnNo.getText().toString();
        String password = InputPass.getText().toString();
        String add = InputAdd.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Name cannot be empty...!!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Phone number cannot be empty...!!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password cannot be empty...!!!", Toast.LENGTH_SHORT).show();
        }
        else{
            loader.setTitle("Create Account");
            loader.setMessage("Please wait, while we are creating your account");
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            validatePhoneNo(name, phone,  password, add);
        }
    }

    private void validatePhoneNo(String name, String phone, String password, String add)
    {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("phone", phone);
                    userData.put("password", password);
                    userData.put("name", name);
                    userData.put("address", add);

                    Rootref.child("Users").child(phone).updateChildren(userData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                        loader.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, Login_Activity.class);
                                        startActivity(intent);
                                    }else {
                                        loader.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: please try again later", Toast.LENGTH_SHORT).show();

                                    }
                                }

                            });


                }
                else{
                    Toast.makeText(RegisterActivity.this, "This " + phone + " already exists", Toast.LENGTH_SHORT).show();
                    loader.dismiss();
                    Toast.makeText(RegisterActivity.this, "please try again with another number", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError databaseError) {

            }
        });


    }
}