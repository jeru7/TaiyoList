package com.example.taiyomarket.main;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.taiyomarket.R;
import com.example.taiyomarket.adapters.ListAdapter;
import com.example.taiyomarket.classes.Item;
import com.example.taiyomarket.classes.ListItem;
import com.example.taiyomarket.classes.User;
import com.example.taiyomarket.database.DBHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.example.taiyomarket.fragments.AddListPage;
import com.example.taiyomarket.fragments.AddItemPage;

import java.util.ArrayList;
import java.util.List;

public class LandingPage extends AppCompatActivity implements AddListPage.OnAddListListener, AddItemPage.OnItemAddedListener, ListAdapter.OnItemLongClickListener {

    RecyclerView recyclerView;
    ListAdapter listAdapter;
    LinearLayout emptyLayout, listLayout, profileLayout;
    RelativeLayout mainPage, topBar;
    FrameLayout switchableContainer;
    ExtendedFloatingActionButton addNewListBtn;
    DBHelper db;
    User currentUser;
    String userEmail;
    AddListPage addListFragment;
    private List<Item> itemList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout_display);
        topBar = (RelativeLayout) findViewById(R.id.relative_1);
        mainPage = (RelativeLayout) findViewById(R.id.relative_2);
        switchableContainer = (FrameLayout) findViewById(R.id.switchable_view);

        addNewListBtn = (ExtendedFloatingActionButton) findViewById(R.id.add_new_list);
        listLayout = (LinearLayout) findViewById(R.id.list_layout);
        profileLayout = (LinearLayout) findViewById(R.id.profile_layout);

        itemList = new ArrayList<>();

        db = new DBHelper(this);
        Intent i = getIntent();
        userEmail = getIntent().getStringExtra("email");

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
        addNewListBtn.setOnClickListener(new View.OnClickListener() {
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
        addListFragment = new AddListPage();

        addListFragment.setAddListFragment(addListFragment);

        Bundle args = new Bundle();
        args.putString("email", userEmail);
        addListFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.switchable_view, addListFragment);
        transaction.commit();
    }

    public void displayList(String email) {
        List<ListItem> items = db.getLists(email);

        if(items != null && !items.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);

            listAdapter = new ListAdapter(items, this);
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
                    displayList(userEmail);
                }
            }
        });
    }

    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete List")
                .setMessage("Are you sure you want to delete this list?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteList(position);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteList(int position) {
        ListItem listItem = listAdapter.getItems().get(position);
        db.deleteList(listItem.getId());
        listAdapter.getItems().remove(position);
        listAdapter.notifyItemRemoved(position);

        if (listAdapter.getItemCount() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAddList() {
        displayList(userEmail);
    }

    @Override
    public void onItemAdded(Item item) {
        itemList.add(item);
    }

    @Override
    public void onItemLongClicked(int position) {
        showDeleteDialog(position);
    }

}