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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

public class SellerLoginActivity extends AppCompatActivity {

    private EditText nameInput, pwdInput;
    private Button sellerLoginBtn;
    private ProgressDialog loader;
    private FirebaseAuth oAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        nameInput = (EditText) findViewById(R.id.seller_email_login);
        pwdInput = (EditText) findViewById(R.id.seller_login_pwd);
        sellerLoginBtn = (Button) findViewById(R.id.seller_login_btn);
        oAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);
        
        sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();
            }
        });

    }

    private void validateCredentials() {

        String pass = pwdInput.getText().toString();
        String email = nameInput.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            loader.setTitle("Logging In");
            loader.setMessage("Please wait, while we are Checking your credentials");
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            oAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loader.dismiss();
                                Toast.makeText(SellerLoginActivity.this, "Login In SuccessFully as a Seller", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }
                            else{
                                loader.dismiss();
                                Toast.makeText(SellerLoginActivity.this, "Network Error, try again later", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }
}