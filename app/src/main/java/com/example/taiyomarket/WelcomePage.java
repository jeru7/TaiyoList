package com.example.taiyomarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {

    ViewPager xViewPager;
    LinearLayout xDotLayout;
    Button signIn, register;

    TextView[] dots;
    PagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        xViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        xDotLayout = (LinearLayout) findViewById(R.id.bulletIndicatorLayout);

        viewPagerAdapter = new PagerAdapter(this);

        xViewPager.setAdapter(viewPagerAdapter);

        displayIndicator(0);
        xViewPager.addOnPageChangeListener(viewListener);
    }

    public void displayIndicator(int pos) {
        dots = new TextView[3];

        xDotLayout.removeAllViews();

        for(int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.placeholder, getApplication().getTheme()));
            xDotLayout.addView(dots[i]);
        }

        dots[pos].setTextColor(getResources().getColor(R.color.extra_color3, getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            displayIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}