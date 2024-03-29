package com.example.farm_a_zon.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farm_a_zon.Admin.AboutUsActivity;
import com.example.farm_a_zon.Admin.AdminMaintainProductsActivity;
import com.example.farm_a_zon.ChatBot.ChatbotActivity;
import com.example.farm_a_zon.Models.Products;
import com.example.farm_a_zon.Prevalent.prevalent;
import com.example.farm_a_zon.R;
import com.example.farm_a_zon.Viewolder.viewProductHolder;

import com.example.farm_a_zon.databinding.ActivityHomeBinding;
import com.example.farm_a_zon.imageslide.SliderAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    private DatabaseReference prodRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private android.widget.TextView name;
    private CircleImageView profileImageview;


    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            type = getIntent().getExtras().get("Admin").toString();
        }


        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, ChatbotActivity.class);
                startActivity(intent);



            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


        View hview = navigationView.getHeaderView(0);
        TextView navname = (TextView) hview.findViewById(R.id.nav_header_name);

        navname.setText(prevalent.name);


        Paper.init(this);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        prodRef = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NavigationView mdrawer = (NavigationView) findViewById(R.id.nav_view);
        mdrawer.setNavigationItemSelectedListener(this);



    }



    @Override
    protected void onStart() {
        super.onStart();




        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(prodRef.orderByChild("productstatus").equalTo("Approved"), Products.class).build();

        FirebaseRecyclerAdapter<Products, viewProductHolder> adapter =
                new FirebaseRecyclerAdapter<Products, viewProductHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@androidx.annotation.NonNull viewProductHolder holder, int position, @androidx.annotation.NonNull Products model) {
                        holder.txtProductName.setText(model.getProdname());
                        holder.txtProductDecsp.setText(model.getDescription());
                        holder.txtProductPrice.setText("price " + model.getPrice() + " Rs");

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (type.equals("Admin")) {

                                    Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                                }
                                else{
                                    Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });

                        Picasso.get().load(model.getImage()).into(holder.productImage);
                    }

                    @Override
                    public viewProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.product_items, parent, false);

                        viewProductHolder holder = new viewProductHolder(view);

                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.nav_logout){
            Paper.book().destroy();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }


        else if(id == R.id.nav_settings){
            if (type.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
                return true;
            }
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        else if(id == R.id.nav_about){
            Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        }

        else if(id == R.id.nav_order){
            if (!type.equals("Admin")){
                Intent intent = new Intent(HomeActivity.this, PlaceOrderedActivity.class);
                startActivity(intent);
                return true;
            }
        }

        else if(id == R.id.nav_cart){
            if (!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            }

        }

        return false;
    }
}