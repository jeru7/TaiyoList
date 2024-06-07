package com.example.taiyomarket.fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.taiyomarket.R;


public class AddItemPage extends Fragment{
    Button cancelBtn, addItemBtn;
    ImageView incrementBtn, decrementBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_page, container, false);

        cancelBtn = (Button) view.findViewById(R.id.cancel_button);
        addItemBtn = (Button) view.findViewById(R.id.add_item_button);
        incrementBtn = (ImageView) view.findViewById(R.id.increment_button);
        decrementBtn = (ImageView) view.findViewById(R.id.decrement_button);

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

            }
        });

        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}