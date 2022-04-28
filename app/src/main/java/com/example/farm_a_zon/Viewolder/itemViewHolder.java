package com.example.farm_a_zon.Viewolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


import androidx.recyclerview.widget.RecyclerView;

import com.example.farm_a_zon.Interfave.itemClickListener;
import com.example.farm_a_zon.R;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.farm_a_zon.Interfave.itemClickListener;
import com.example.farm_a_zon.R;

public class itemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
   public TextView txtProductName, txtProductDecsp, txtProductPrice, txtProductStatus;
    public ImageView productImage;
    public itemClickListener listeners;

    public itemViewHolder(View itemView) {
        super(itemView);

        productImage = (ImageView) itemView.findViewById(R.id.seller_item_image);
        txtProductDecsp = (TextView) itemView.findViewById(R.id.seller_item_description);
        txtProductName = (TextView) itemView.findViewById(R.id.seller_item_name);
        txtProductPrice = (TextView) itemView.findViewById(R.id.seller_item_price);
        txtProductStatus = (TextView) itemView.findViewById(R.id.seller_prod_status);

    }

    public void setItemClickListener(itemClickListener listener){

        this.listeners = listener;
    }

    @Override
    public void onClick(View v) {
        listeners.onClick(v, getAdapterPosition(), false);
    }
}
