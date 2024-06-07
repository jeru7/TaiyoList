package com.example.taiyomarket.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.taiyomarket.R;
import com.example.taiyomarket.classes.Item;
import com.example.taiyomarket.classes.ListItem;
import com.example.taiyomarket.database.DBHelper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.taiyomarket.database.DBHelper;

import java.util.ArrayList;
import java.util.List;
import com.example.taiyomarket.classes.User;

public class AddListPage extends Fragment implements FragmentManager.OnBackStackChangedListener {

    EditText listNameInput;
    ImageButton addItemBtn;
    RelativeLayout addListContainer;
    LinearLayout listItems;
    Button createBtn;
    TextView emptyText, itemText;
    String userEmail;
    int userId;
    DBHelper db;
    private List<Item> itemList = new ArrayList<>();
    private long listId;
    private AddItemPage addItemFragment;
    private AddListPage addListFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list_page, container, false);

        listNameInput = (EditText) view.findViewById(R.id.list_name_input);
        createBtn = (Button) view.findViewById(R.id.create_button);
        addItemBtn = (ImageButton) view.findViewById(R.id.add_item);
        addListContainer = (RelativeLayout) view.findViewById(R.id.add_list_container);
        listItems = (LinearLayout) view.findViewById(R.id.list_items);
        emptyText = (TextView) view.findViewById(R.id.empty_text);
        itemText = (TextView) view.findViewById(R.id.item_text);
        db = new DBHelper(requireContext());

        addItemFragment = new AddItemPage();
        itemList = addItemFragment.getItemList();

        Bundle args = getArguments();

        if (args != null) {
            userEmail = args.getString("email");
        }

        attachButtonEvents();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = db.getUser(userEmail);
        itemList = addItemFragment.getItemList();

        if (user != null) {
            userId = user.getId();
        }
        displayList(itemList);
        displaySuggestions(view);
    }

    public void displayList(List<Item> list) {
        listItems.removeAllViews();

        if(!list.isEmpty()) {
            itemText.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
            for(Item item : list) {
                TextView textView = new TextView(requireContext());
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText(item.getItemName() + " x" + item.getQuantity());
                textView.setTextSize(16);
                textView.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.medium));
                listItems.addView(textView);
            }
        } else {
            itemText.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    public void attachButtonEvents() {
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalListName = listNameInput.getText().toString();
                String dateCreated = getCurrentDateTime();
                String lastUpdate = getCurrentDateTime();

                DBHelper dbHelper = new DBHelper(requireContext());
                listId = dbHelper.addList(userId, finalListName, dateCreated, lastUpdate);
                setListId(listId);

                itemList = addItemFragment.getItemList();

                for (Item item : itemList) {
                    dbHelper.addItemToList(listId, item.getItemName(), item.getQuantity());
                }

                Toast.makeText(requireContext(), finalListName + " created successfully", Toast.LENGTH_SHORT).show();
                itemList.clear();
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemFragment.setListId(listId);

                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.switchable_view, addItemFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                addListContainer.setVisibility(View.GONE);
                createBtn.setVisibility(View.GONE);
            }
        });
    }

    private void displaySuggestions(View rootView) {
        String[] suggestionsArray = {"Fruits", "Vegetables", "Meal Plan", "Monday", "Tuesday", "Wednesday", "Thursday", "Saturday", "Sunday", "Tomorrow"};

        ViewGroup suggestionsList = rootView.findViewById(R.id.suggestions_list);
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (String suggestion : suggestionsArray) {
            TextView textView = (TextView) inflater.inflate(R.layout.suggestion_item, suggestionsList, false);
            textView.setText(suggestion);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedSuggestion = ((TextView) v).getText().toString();
                    listNameInput.setText(selectedSuggestion);
                }
            });

            suggestionsList.addView(textView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getChildFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getChildFragmentManager().removeOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {
        boolean addItemPageVisible = getChildFragmentManager().findFragmentById(R.id.switchable_view) instanceof AddItemPage;

        if (!addItemPageVisible) {
            displayList(itemList);
            addListContainer.setVisibility(View.VISIBLE);
            createBtn.setVisibility(View.VISIBLE);
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    public void addItemToList(Item item) {
        itemList.add(item);
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public void setAddListFragment(AddListPage addListFragment) {
        this.addListFragment = addListFragment;
    }

    public interface OnAddListListener {
        void onAddList();
    }
}