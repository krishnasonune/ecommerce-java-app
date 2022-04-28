package com.example.farm_a_zon.Seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.farm_a_zon.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddProductActivity extends AppCompatActivity {

    private String categoryName;
    private Button addPro;
    private EditText prodName, prodDesc, prodPrice;
    private ImageView prodImg;
    private final static int pick = 1;
    private Uri imgUri;
    private String cateGory, pname, price, pdes, saveCurrentDate, saveCurrentTime;
    private String Randomkey, downloadImgUrl;
    private StorageReference prodImgref;
    private DatabaseReference prodref , sellRef;
    private ProgressDialog loader;

    private String sName, sAddress, sEmail,
                    sPhone, sId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product);

        prodImg = (ImageView) findViewById(R.id.select_prod_image);
        prodName = (EditText) findViewById(R.id.product_name);
        prodDesc = (EditText) findViewById(R.id.product_desc);
        prodPrice = (EditText) findViewById(R.id.product_price);
        addPro = (Button) findViewById(R.id.add_prod_btn);
        loader = new ProgressDialog(this);

        categoryName = getIntent().getExtras().get("category").toString();
        prodImgref = FirebaseStorage.getInstance().getReference().child("productImage");
        prodref = FirebaseDatabase.getInstance().getReference().child("Products");
        sellRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

        prodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductDetail();
            }
        });

        sellRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            sName = snapshot.child("name").getValue().toString();
                            sEmail = snapshot.child("email").getValue().toString();
                            sAddress = snapshot.child("address").getValue().toString();
                            sPhone = snapshot.child("phone").getValue().toString();
                            sId = snapshot.child("sid").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void validateProductDetail() {
        pname = prodName.getText().toString();
        pdes = prodDesc.getText().toString();
        price = prodPrice.getText().toString();

        if (imgUri == null){
            Toast.makeText(SellerAddProductActivity.this, "Product image is mandtory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pname)){
            Toast.makeText(SellerAddProductActivity.this, "Product name is mandtory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pdes)){
            Toast.makeText(SellerAddProductActivity.this, "Product description is mandtory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(price)){
            Toast.makeText(SellerAddProductActivity.this, "Product price is mandtory", Toast.LENGTH_SHORT).show();
        }
        else{
            storeProductInfo();
        }




    }

    private void storeProductInfo() {

        loader.setTitle("Adding product");
        loader.setMessage("please wait, while we are adding the product in the system");
        loader.setCanceledOnTouchOutside(false);
        loader.show();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(cal.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(cal.getTime());

        Randomkey = saveCurrentDate + saveCurrentTime;
        StorageReference filepath = prodImgref.child(imgUri.getLastPathSegment() + Randomkey + ".jpg");
        final UploadTask uploadfile = filepath.putFile(imgUri);


        uploadfile.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SellerAddProductActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                loader.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddProductActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> task = uploadfile.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImgUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImgUrl = task.getResult().toString();
                            Toast.makeText(SellerAddProductActivity.this, "Product image uri from database", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDatabase() {
        HashMap<String, Object> prodData = new HashMap<>();
        prodData.put("pid", Randomkey);
        prodData.put("date", saveCurrentDate);
        prodData.put("time", saveCurrentTime);
        prodData.put("description", pdes);
        prodData.put("Image", downloadImgUrl);
        prodData.put("category", categoryName);
        prodData.put("prodname", pname);
        prodData.put("price", price);

        prodData.put("sellerName", sName);
        prodData.put("sellerAddress", sAddress);
        prodData.put("sellerPhone", sPhone);
        prodData.put("sellerEmail", sEmail);
        prodData.put("sid", sId);
        prodData.put("productstatus", "Not Approved");

        prodref.child(Randomkey).updateChildren(prodData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            Intent intent = new Intent(SellerAddProductActivity.this, SellerHomeActivity.class);
                            startActivity(intent);

                            Toast.makeText(SellerAddProductActivity.this, "product Added successfully", Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }else{
                            Toast.makeText(SellerAddProductActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }

                    }
                });
    }

    private void openGallery() {
        Intent gallintent = new Intent();
        gallintent.setAction(Intent.ACTION_GET_CONTENT);
        gallintent.setType("image/*");
        startActivityForResult(gallintent, pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pick && resultCode == RESULT_OK && data != null){
            imgUri = data.getData();
            prodImg.setImageURI(imgUri);
        }

    }



}