package com.example.taiyomarket.fragments;

// imports
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taiyomarket.R;

import com.example.taiyomarket.classes.Item;

import java.util.ArrayList;
import java.util.List;

public class AddItemPage extends Fragment{
//    variable declarations
    private Button cancelBtn, addItemBtn;
    private ImageView incrementBtn, decrementBtn;
    private EditText itemNameField;
    private TextView quantityValue;
    private List<Item> itemList;
    private LinearLayout recentlyAddedList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        initialization mosty ng ui elements para maging dynamic yung mga elements
        View view = inflater.inflate(R.layout.fragment_add_item_page, container, false);

        cancelBtn = (Button) view.findViewById(R.id.cancel_button);
        addItemBtn = (Button) view.findViewById(R.id.add_item_button);
        incrementBtn = (ImageView) view.findViewById(R.id.increment_button);
        decrementBtn = (ImageView) view.findViewById(R.id.decrement_button);
        itemNameField = (EditText) view.findViewById(R.id.item_name_input);
        quantityValue = (TextView) view.findViewById(R.id.quantity_value);
        recentlyAddedList = view.findViewById(R.id.recently_added_list);

        attachButtonEvents(); // attach lang ng listeners sa mga buttons or button like elements
        return view;
    }

//    method na ginagamit ni addlistpage, para mapasa niya yung temporary item list dito
    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    //    method na ginagamit sa addlistpage para ma-call yung itemlist (temporary list) dito sa current additempage *getter
    //    reason: nilagay ko siya sa array/list muna para pag magccreate na ng list dun lang siya idadagdag sabay sabay (refer to createBtn ng addlistpage)
    public List<Item> getItemList() {
        return itemList;
    }

    public void attachButtonEvents() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popBackStack lang para maalis tong current AddItemPage sa switchable_view kasi inadd siya using the addItemBtn sa addlistpage (refer to additempage addItemBtn onclick)
                getParentFragmentManager().popBackStack();
            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fetch lang ng value sa input field at textview
                String itemName = itemNameField.getText().toString();
//                parseInt since int yung quantity sa item class at pati sa database
                int itemQuantity = Integer.parseInt(quantityValue.getText().toString());

//                checker lang kung may laman ba yung item name field
                if(!itemName.isEmpty()) {
//                    pag meron create new item with the itemName at itemQuantity
                    Item item = new Item(itemName, itemQuantity);
                    addItemToList(item);
                } else {
//                    pag wala toast lang then do nothing
                    Toast.makeText(getContext(), "Please add an item name", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        increment lang yung value ng textview na quantity
        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curValue = Integer.parseInt(quantityValue.getText().toString());

                curValue++;

                quantityValue.setText(String.valueOf(curValue));
            }
        });

//        decrement lang ng value ng textview ng quantity
        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curValue = Integer.parseInt(quantityValue.getText().toString());

//                pag 1 na return na lang since bawal ang negative quantity
                if(curValue == 1) {
                    return;
                } else {
                    curValue--;
                }
                quantityValue.setText(String.valueOf(curValue));
            }
        });
    }

//    called when add item is clicked
    public void addItemToList(Item item) {
//        bali nirerefresh niya yung itemname field at quantity value
        itemNameField.setText("");
        quantityValue.setText("1");
//        then add si item sa itemList(temporary list lang to)
        itemList.add(item);

//        pasa lang yung name at quantity ni item para madisplay sa recently added
        displayRecentlyAdded(item.getItemName(), item.getQuantity());
    }

//    displays recently added item
    public void displayRecentlyAdded(String itemName, int quantity) {
//        bali ang nangyayare rito is gumagawa lang siya ng text view then inaaply yung mga need na attributes (text value, color, typeface = fontfamily)
        TextView textView = new TextView(requireContext());
//        height and width to wrap_content
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        itemname xquantity
        textView.setText(itemName + " x" + quantity);
//        color fetched on colors.xml
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.placeholder));

//      type face para lang makapag-fetch tayo ng gawa nating font family
        Typeface customTypeface = ResourcesCompat.getFont(requireContext(), R.font.bold);
        textView.setTypeface(customTypeface, Typeface.BOLD);

//        addView meaning ia-add na yung nagawang textview together with its attributes sa recently_added_list (refer to the xml file of additempage)
        recentlyAddedList.addView(textView);
    }

}