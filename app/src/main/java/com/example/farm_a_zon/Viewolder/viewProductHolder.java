package com.example.farm_a_zon.Viewolder;

import android.view.View;


import androidx.recyclerview.widget.RecyclerView;

import com.example.farm_a_zon.Interfave.itemClickListener;
import com.example.farm_a_zon.R;
import android.widget.ImageView;
import android.widget.TextView;

public class viewProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductDecsp, txtProductPrice;
    public ImageView productImage;
    public itemClickListener listeners;

    public viewProductHolder(View itemView) {
        super(itemView);

        productImage = (ImageView) itemView.findViewById(R.id.product_item_image);
        txtProductDecsp = (TextView) itemView.findViewById(R.id.product_item_description);
        txtProductName = (TextView) itemView.findViewById(R.id.product_item_name);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_item_price);

    }

    public void setItemClickListener(itemClickListener listener){

        this.listeners = listener;
    }

    @Override
    public void onClick(View v) {
        listeners.onClick(v, getAdapterPosition(), false);
    }
}
