package com.example.farm_a_zon.imageslide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farm_a_zon.R;
import com.rey.material.widget.ImageView;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {

    int[] banners;

    public SliderAdapter (int[] banners){

        this.banners = banners;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        viewHolder.imageView.setImageResource(banners[position]);
    }

    @Override
    public int getCount() {
        return banners.length;
    }

    public class Holder extends SliderViewAdapter.ViewHolder{





        ImageView imageView;

        public Holder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_view);
        }
    }
}
