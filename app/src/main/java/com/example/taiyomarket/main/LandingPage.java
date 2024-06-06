package com.example.taiyomarket.main;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.taiyomarket.R;
import com.example.taiyomarket.adapters.ListAdapter;
import com.example.taiyomarket.classes.ListItem;
import com.example.taiyomarket.classes.User;
import com.example.taiyomarket.database.DBHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.example.taiyomarket.main.AddListPage;;

import java.util.List;

public class LandingPage extends AppCompatActivity implements AddListPage.OnAddListListener {

    RecyclerView recyclerView;
    ListAdapter listAdapter;
    LinearLayout emptyLayout, listLayout, profileLayout;
    RelativeLayout mainPage, topBar;
    FrameLayout switchableContainer;
    ExtendedFloatingActionButton addNewList;
    DBHelper db;
    User currentUser;
    AddListPage addListPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout_display);
        topBar = (RelativeLayout) findViewById(R.id.relative_1);
        mainPage = (RelativeLayout) findViewById(R.id.relative_2);
        switchableContainer = (FrameLayout) findViewById(R.id.switchable_view);

        addNewList = (ExtendedFloatingActionButton) findViewById(R.id.add_new_list);
        listLayout = (LinearLayout) findViewById(R.id.list_layout);
        profileLayout = (LinearLayout) findViewById(R.id.profile_layout);

        db = new DBHelper(this);
        Intent i = getIntent();
        String userEmail = getIntent().getStringExtra("email");

        if (userEmail != null) {
            currentUser = db.getUser(userEmail);
            if (currentUser != null) {
                displayList(userEmail);
            }
        }

        handleBackPress();
        attachButtonEvents();
    }

    public void attachButtonEvents() {
        addNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(LandingPage.this);
                View addListPageView = inflater.inflate(R.layout.fragment_add_list_page, null);

                switchableContainer.addView(addListPageView);

                topBar.setVisibility(View.GONE);
                mainPage.setVisibility(View.GONE);
                startAddList();
            }
        });
    }

    public void startAddList() {
        AddListPage addListFragment = new AddListPage();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.switchable_view, addListFragment);
        transaction.commit();
    }

    public void displayList(String email) {
        List<ListItem> items = db.getLists(email);

        if(items != null && !items.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);

            listAdapter = new ListAdapter(items);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(listAdapter);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void handleBackPress() {
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(switchableContainer.getChildCount() > 0) {
                    switchableContainer.removeAllViews();
                    topBar.setVisibility(View.VISIBLE);
                    mainPage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onAddList() {

    }
}