package com.EX.examcreator.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.EX.examcreator.Adapters.OnBoardingAdapter;
import com.EX.examcreator.MainActivity;
import com.EX.examcreator.Models.OnBoardingModel;
import com.EX.examcreator.OnBoardingSuppliers;
import com.EX.examcreator.R;

import java.util.List;

public class OnBoardingScreen extends AppCompatActivity {
    ViewPager viewPager;
    List<OnBoardingModel> onBoardingModels;
    PagerAdapter pagerAdapter;
    LinearLayout linearLayout;
    TextView[] dots;
    Button GetStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);


        viewPager =findViewById(R.id.OnBoardingPageView);
        onBoardingModels = OnBoardingSuppliers.getOnBoardingObjects();
        pagerAdapter =new OnBoardingAdapter(onBoardingModels,this);
        viewPager.setAdapter(pagerAdapter);

        linearLayout=findViewById(R.id.dots_Slider_circle);
        add_dotsSlider(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                add_dotsSlider( position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getStartedButton();
    }

    private void getStartedButton() {
        GetStarted=findViewById(R.id.Get_Started);
        final Intent intent=new Intent(this, MainActivity.class);
        GetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScreen();
                startActivity(intent);
            }
        });
    }

    private void add_dotsSlider(int position) {
        linearLayout.removeAllViews();
        dots=new TextView[onBoardingModels.size()];
        for (int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(getResources().getColor(R.color.Blue));
            linearLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.Black));

    }

    private void updateScreen() {
        SharedPreferences Shared=getSharedPreferences(DeciderActivity.MYPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=Shared.edit();
        editor.putBoolean("seen",true);
        editor.commit();
    }

}
