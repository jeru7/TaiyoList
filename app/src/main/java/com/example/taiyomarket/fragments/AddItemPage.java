package com.example.taiyomarket.fragments;

import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taiyomarket.R;

import com.example.taiyomarket.classes.Item;
import com.example.taiyomarket.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class AddItemPage extends Fragment{
    private Button cancelBtn, addItemBtn;
    private ImageView incrementBtn, decrementBtn;
    private EditText itemNameField;
    private TextView quantityValue;
    private List<Item> itemList = new ArrayList<>();
    private long listId;
    private LinearLayout recentlyAddedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_page, container, false);

        cancelBtn = (Button) view.findViewById(R.id.cancel_button);
        addItemBtn = (Button) view.findViewById(R.id.add_item_button);
        incrementBtn = (ImageView) view.findViewById(R.id.increment_button);
        decrementBtn = (ImageView) view.findViewById(R.id.decrement_button);
        itemNameField = (EditText) view.findViewById(R.id.item_name_input);
        quantityValue = (TextView) view.findViewById(R.id.quantity_value);
        recentlyAddedList = view.findViewById(R.id.recently_added_list);

        attachButtonEvents();
        return view;
    }

    public void attachButtonEvents() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = itemNameField.getText().toString();
                int itemQuantity = Integer.parseInt(quantityValue.getText().toString());

                Item item = new Item(itemName, itemQuantity);
                addItemToList(item);
            }
        });

        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curValue = Integer.parseInt(quantityValue.getText().toString());

                curValue++;

                quantityValue.setText(String.valueOf(curValue));
            }
        });

        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curValue = Integer.parseInt(quantityValue.getText().toString());

                if(curValue == 1) {
                    return;
                } else {
                    curValue--;
                }
                quantityValue.setText(String.valueOf(curValue));
            }
        });
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public void addItemToList(Item item) {
        itemNameField.setText("");
        quantityValue.setText("1");
        itemList.add(item);

        displayRecentlyAdded(item.getItemName(), item.getQuantity());
    }

    public void displayRecentlyAdded(String itemName, int quantity) {
        TextView textView = new TextView(requireContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(itemName + " x" + quantity);
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.placeholder));


        Typeface customTypeface = ResourcesCompat.getFont(requireContext(), R.font.bold);
        textView.setTypeface(customTypeface, Typeface.BOLD);

        recentlyAddedList.addView(textView);
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public interface OnItemAddedListener {
        void onItemAdded(Item item);
    }

}