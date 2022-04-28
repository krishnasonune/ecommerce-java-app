package com.example.farm_a_zon.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.farm_a_zon.R;
import com.example.farm_a_zon.imageslide.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class AboutUsActivity extends AppCompatActivity {

    SliderView sliderView;
    int[] banners = {
            R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.four,
            R.drawable.five
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        sliderView = (SliderView) findViewById(R.id.imageSlider);
        SliderAdapter sliderAdapter = new SliderAdapter(banners);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();


    }
}