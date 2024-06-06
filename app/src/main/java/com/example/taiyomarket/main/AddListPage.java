package com.example.taiyomarket.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.taiyomarket.R;
import androidx.fragment.app.Fragment;

public class AddListPage extends Fragment {

    EditText listNameInput;
    ImageButton addItemBtn;
    Button createBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list_page, container, false);
        listNameInput = view.findViewById(R.id.list_name_input);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displaySuggestions(view);
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

    public interface OnAddListListener {
        void onAddList();
    }

}