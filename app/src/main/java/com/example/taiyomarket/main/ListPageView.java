package com.example.taiyomarket.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taiyomarket.R;
import com.example.taiyomarket.adapters.ItemAdapter;
import com.example.taiyomarket.classes.Item;
import com.example.taiyomarket.database.DBHelper;

import java.util.List;

public class ListPageView extends AppCompatActivity  {

    List<Item> itemList;
    LinearLayout emptyLayoutDisplay, centerContainer;
    RelativeLayout infoContainer;
    ImageView backBtn;
    private RecyclerView recyclerView;
    private DBHelper db;
    private long listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page_view);

        recyclerView = (RecyclerView) findViewById(R.id.list_items_container);
        emptyLayoutDisplay = (LinearLayout) findViewById(R.id.empty_layout_display);
        centerContainer = (LinearLayout) findViewById(R.id.center_container);
        infoContainer = findViewById(R.id.info_container);
        backBtn = findViewById(R.id.back_btn);
        db = new DBHelper(this);

        listId = getIntent().getLongExtra("listId", -1);

        attachButtonEvents();
        displayItemList(listId);
    }

    public void attachButtonEvents() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void displayItemList(long listId) {
        itemList = db.getItemsOnList(listId);

        if (itemList.isEmpty()) {
            emptyLayoutDisplay.setVisibility(View.VISIBLE);
            infoContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            ScrollView.LayoutParams params = (ScrollView.LayoutParams) centerContainer.getLayoutParams();
            params.gravity = Gravity.CENTER;
            centerContainer.setLayoutParams(params);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    infoContainer.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            infoContainer.setVisibility(View.GONE);
                        }
                    }, 2000);
                }
            }, 2000);

            emptyLayoutDisplay.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            ScrollView.LayoutParams params = (ScrollView.LayoutParams) centerContainer.getLayoutParams();
            params.gravity = Gravity.TOP;
            centerContainer.setLayoutParams(params);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ItemAdapter itemAdapter = new ItemAdapter(itemList);
            recyclerView.setAdapter(itemAdapter);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        displayItemList(listId);
    }
}
