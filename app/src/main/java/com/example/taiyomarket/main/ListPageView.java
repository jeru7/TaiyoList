package com.example.taiyomarket.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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

public class ListPageView extends AppCompatActivity implements ItemAdapter.OnItemEditClickListener, ItemAdapter.OnItemDeleteClickListener {

    List<Item> itemList;
    LinearLayout emptyLayoutDisplay, centerContainer;
    TextView listNameDisplay;
    ImageView backBtn;
    private RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    private DBHelper db;
    private long listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page_view);

        listNameDisplay = findViewById(R.id.list_name);
        recyclerView = findViewById(R.id.list_items_container);
        emptyLayoutDisplay = findViewById(R.id.empty_layout_display);
        centerContainer = findViewById(R.id.center_container);
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
        String listName = db.getListName(listId);
        listNameDisplay.setText(listName);

        Toast.makeText(this, "Hold to edit or delete", Toast.LENGTH_SHORT).show();

        if (itemList.isEmpty()) {
            emptyLayoutDisplay.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            ScrollView.LayoutParams params = (ScrollView.LayoutParams) centerContainer.getLayoutParams();
            params.gravity = Gravity.CENTER;
            centerContainer.setLayoutParams(params);

        } else {

            emptyLayoutDisplay.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            ScrollView.LayoutParams params = (ScrollView.LayoutParams) centerContainer.getLayoutParams();
            params.gravity = Gravity.TOP;
            centerContainer.setLayoutParams(params);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            itemAdapter = new ItemAdapter(itemList);
            recyclerView.setAdapter(itemAdapter);

            // Set item click listeners
            itemAdapter.setOnItemEditClickListener(this);
            itemAdapter.setOnItemDeleteClickListener(this);
        }
    }

    private void showDeleteConfirmationDialog(final int position, final Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(position, item);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void checkEmptyState() {
        if (itemList.isEmpty()) {
            emptyLayoutDisplay.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            ScrollView.LayoutParams params = (ScrollView.LayoutParams) centerContainer.getLayoutParams();
            params.gravity = Gravity.CENTER;
            centerContainer.setLayoutParams(params);
        }
    }

    private void deleteItem(int position, Item item) {
        db.deleteItem(item.getId());
        itemList.remove(position);
        itemAdapter.notifyItemRemoved(position);
        checkEmptyState();
    }

    @Override
    public void onItemEditClick(int position, Item item, boolean isEdit) {
        Intent intent = new Intent(ListPageView.this, EditPage.class);
        intent.putExtra("itemId", item.getId());
        startActivity(intent);
    }

    @Override
    public void onItemDeleteClick(int position, Item item) {
        showDeleteConfirmationDialog(position, item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayItemList(listId);
    }
}
