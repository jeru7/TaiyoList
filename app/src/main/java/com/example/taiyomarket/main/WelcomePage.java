package com.example.taiyomarket.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taiyomarket.R;
import com.example.taiyomarket.adapters.PagerAdapter;
import com.example.taiyomarket.logins.Register;
import com.example.taiyomarket.logins.SignIn;

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

//      Variables for ViewPage and Dots
        xViewPager = (ViewPager) findViewById(R.id.slide_view_pager);
        xDotLayout = (LinearLayout) findViewById(R.id.bullet_indicator_layout);

        viewPagerAdapter = new PagerAdapter(this);

        xViewPager.setAdapter(viewPagerAdapter);

        displayIndicator(0);
        xViewPager.addOnPageChangeListener(viewListener);
//      Variables for buttons
        signIn = (Button) findViewById(R.id.sign_in_btn);
        register = (Button) findViewById(R.id.register_btn);

        attachBtnListener(signIn, register);
    }

    public void attachBtnListener(Button sign_in, Button register) {
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomePage.this, SignIn.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomePage.this, Register.class);
                startActivity(i);
            }
        });
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