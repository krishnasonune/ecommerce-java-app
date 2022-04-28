package com.example.farm_a_zon.Viewolder;

;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farm_a_zon.Interfave.itemClickListener;
import com.example.farm_a_zon.R;
import com.rey.material.widget.TextView;

public class cartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProdName, txtProdQuantity, txtProdPrice;
    private itemClickListener itemClickListener;

    public cartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProdName = itemView.findViewById(R.id.cart_prod_name);
        txtProdQuantity = itemView.findViewById(R.id.cart_prod_quantity);
        txtProdPrice = itemView.findViewById(R.id.cart_prod_price);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(itemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
