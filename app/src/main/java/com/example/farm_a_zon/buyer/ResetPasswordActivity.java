package com.example.farm_a_zon.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.farm_a_zon.Prevalent.prevalent;
import com.example.farm_a_zon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText phnNum, ques1, ques2;
    private Button verifyBtn;
    private TextView pgTitle, titleQue;
    private String Check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Check = getIntent().getStringExtra("check");

        pgTitle = (TextView) findViewById(R.id.reset_pwd);
        titleQue = (TextView) findViewById(R.id.ques_text_view);
        phnNum = (EditText) findViewById(R.id.find_phn_num);
        ques1 = (EditText) findViewById(R.id.question_1);
        ques2 = (EditText) findViewById(R.id.question_2);
        verifyBtn = (Button) findViewById(R.id.verify_btn);


    }

    @Override
    protected void onStart() {
        super.onStart();

        phnNum.setVisibility(View.GONE);

        if (Check.equals("settings")) {
            pgTitle.setText("Set Question");
            titleQue.setText("Answer the following Question ?");

            displayPreviousAnswer();
            verifyBtn.setText("Set");


            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswer();

                }
            });


        }
        else if(Check.equals("login")) {
            pgTitle.setText("Reset Password");
            titleQue.setText("Answer the following Question ?");
            phnNum.setVisibility(View.VISIBLE);
            verifyBtn.setText("Verify");


            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });




        }



    }

    private void verifyUser() {

        String phn = phnNum.getText().toString();
        String anss1 = ques1.getText().toString().toLowerCase();
        String anss2 = ques2.getText().toString().toLowerCase();

        if (TextUtils.isEmpty(phn)) {
            Toast.makeText(this, "phone number cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(anss1) && TextUtils.isEmpty(anss2)) {
            Toast.makeText(this, "response cannot be empty", Toast.LENGTH_SHORT).show();
        }

        else{
            final DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(phn);

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String mPhone = snapshot.child("phone").getValue().toString();

                                if (phn.equals(mPhone)) {

                                    if (snapshot.hasChild("Security Questions")) {

                                        String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                                        String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString();


                                        if (!ans1.equals(anss1) ) {
                                            Toast.makeText(ResetPasswordActivity.this, "Answer of 1st question is Wrong", Toast.LENGTH_SHORT).show();
                                        }

                                        else if (!ans2.equals(anss2) ) {
                                            Toast.makeText(ResetPasswordActivity.this, "Answer of 2nd question is Wrong", Toast.LENGTH_SHORT).show();
                                        }
                                        else{

                                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                            builder.setTitle("New Password");

                                            final EditText newpass = new EditText(ResetPasswordActivity.this);
                                            newpass.setHint("Enter New Password");
                                            builder.setView(newpass);

                                            builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {


                                                    if (newpass.getText().toString().equals("")){
                                                        Toast.makeText(ResetPasswordActivity.this, "Password cannnot be empty", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {

                                                        ref.child("password")
                                                                .setValue(newpass.getText().toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(ResetPasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();

                                                                            Intent intent = new Intent(ResetPasswordActivity.this, Login_Activity.class);
                                                                            startActivity(intent);
                                                                        }
                                                                        else{
                                                                            Toast.makeText(ResetPasswordActivity.this, "Oops..!! some network error occured", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                });

                                                    }

                                                }
                                            });

                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            builder.show();

                                        }


                                    }

                                }
                                else{
                                    Toast.makeText(ResetPasswordActivity.this, "User Doesn't Exists", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }



    }






    private void setAnswer() {

        String ans1 = ques1.getText().toString().toLowerCase();
        String ans2 = ques2.getText().toString().toLowerCase();

        if (ques1.equals("") && ques2.equals("")) {
            Toast.makeText(ResetPasswordActivity.this, "Response Cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(prevalent.phone);

            HashMap<String, Object> userData = new HashMap<>();
            userData.put("answer1", ans1);
            userData.put("answer2", ans2);

            ref.child("Security Questions")
                    .updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "Security Questions Updated Successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }

                }
            });

        }


    }



    private void displayPreviousAnswer(){

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(prevalent.phone);

        ref.child("Security Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    ques1.setText(ans1);
                    ques2.setText(ans2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}