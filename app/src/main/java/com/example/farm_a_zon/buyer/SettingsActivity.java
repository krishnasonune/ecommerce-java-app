package com.example.farm_a_zon.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farm_a_zon.Prevalent.prevalent;
import com.example.farm_a_zon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rey.material.widget.Button;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private TextView closetextBtn, updatetextBtn;
    public CircleImageView profileImageview;
    private EditText phoneEdittext, nameEdittext, addressEdittext;
    private TextView setName, setPhone;
    private Button setSecQuebtn;
    private String type ="";
    private StorageReference storageProfPicReference;
    private String checker = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            type = getIntent().getExtras().get("Admin").toString();
        }

        storageProfPicReference = FirebaseStorage.getInstance().getReference().child("Profile");


        closetextBtn = (TextView) findViewById(R.id.close_setting_btn);
        updatetextBtn = (TextView) findViewById(R.id.update_setting_btn);
        setSecQuebtn = (Button) findViewById(R.id.set_security_que);


        setPhone = (TextView) findViewById(R.id.setting_phn_view);
        setName = (TextView) findViewById(R.id.setting_view_name);



        phoneEdittext = (EditText) findViewById(R.id.setting_phone_num);
        nameEdittext = (EditText) findViewById(R.id.setting_full_name);
        addressEdittext = (EditText) findViewById(R.id.setting_address);

        userInfoDisplay(profileImageview, nameEdittext, phoneEdittext, addressEdittext);

        closetextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updatetextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    updateOnlyUserInfo();

            }
        });




        setSecQuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);

            }
        });



    }




    private void updateOnlyUserInfo() {
        String collect = "Admins";
        if (!type.equals("Admin")){
            collect = "Users";
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(collect);

        if (TextUtils.isEmpty(nameEdittext.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEdittext.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "phone cannot be empty", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(addressEdittext.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "address cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String , Object> userMapdata = new HashMap<>();
            userMapdata.put("name", nameEdittext.getText().toString());
            userMapdata.put("address", addressEdittext.getText().toString());
            userMapdata.put("password", phoneEdittext.getText().toString());

            ref.child(prevalent.phone).updateChildren(userMapdata);

            startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
            Toast.makeText(SettingsActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }


    }



    private void userInfoDisplay(CircleImageView profileImageview, EditText nameEdittext, EditText phoneEdittext, EditText addressEdittext) {
        String collect = "Admins";
        if (!type.equals("Admin")){
            collect = "Users";
        }


            DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child(collect).child(prevalent.phone);


            dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        {

                            String name = snapshot.child("name").getValue().toString();
                            String phn = snapshot.child("password").getValue().toString();
                            String add = snapshot.child("address").getValue().toString();
                            String pphn = snapshot.child("phone").getValue().toString();


                            nameEdittext.setText(name);
                            phoneEdittext.setText(phn);
                            addressEdittext.setText(add);
                            setName.setText(name);
                            setPhone.setText("+91 " + pphn);

                            if (type.equals("Admin")) {
                                setSecQuebtn.setVisibility(View.INVISIBLE);
                            }


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



    }
}