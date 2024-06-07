package com.example.taiyomarket.fragments;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.taiyomarket.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AddListPage extends Fragment implements FragmentManager
        .OnBackStackChangedListener{

    EditText listNameInput;
    ImageButton addItemBtn;

    RelativeLayout addListContainer;
    Button createBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list_page, container, false);

        listNameInput = (EditText) view.findViewById(R.id.list_name_input);
        createBtn = (Button) view.findViewById(R.id.create_button);
        addItemBtn = (ImageButton) view.findViewById(R.id.add_item);
        addListContainer = (RelativeLayout) view.findViewById(R.id.add_list_container);

        attachButtonEvents();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displaySuggestions(view);
    }

    public void attachButtonEvents() {
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addItemFragment = new AddItemPage();
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
            addListContainer.setVisibility(View.VISIBLE);
            createBtn.setVisibility(View.VISIBLE);
        }
    }


    public interface OnAddListListener {
        void onAddList();
    }

}