package com.example.taiyomarket.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taiyomarket.R;
import com.example.taiyomarket.classes.Item;
import com.example.taiyomarket.database.DBHelper;

public class EditPage extends AppCompatActivity {
    Item item;
    EditText editName;
    ImageView decrementBtn, incrementBtn;
    TextView quantityValue;
    Button cancelBtn, saveBtn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_page);

        long itemId = getIntent().getLongExtra("itemId", -1);
        db = new DBHelper(this);
        item = db.getItemById(itemId);

        editName = findViewById(R.id.edit_input_field);
        decrementBtn = findViewById(R.id.decrement_btn_edit_page);
        incrementBtn = findViewById(R.id.increment_btn_edit_page);
        quantityValue = findViewById(R.id.quantity_value_edit_page);
        cancelBtn = findViewById(R.id.cancel_btn_edit_page);
        saveBtn = findViewById(R.id.save_btn_edit_page);

        quantityValue.setText(String.valueOf(item.getQuantity()));
        editName.setText(item.getItemName());
        attachButtonEvents();
    }

    public void attachButtonEvents() {
        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curValue = Integer.parseInt(quantityValue.getText().toString());

                if (curValue == 1) {
                    return;
                } else {
                    curValue--;
                }
                quantityValue.setText(String.valueOf(curValue));
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

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editName.getText().toString().isEmpty()) {
                    Toast.makeText(EditPage.this, "Item name should not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    item.setQuantity(Integer.parseInt(quantityValue.getText().toString()));
                    item.setItemName(editName.getText().toString());

                    boolean success = db.updateItem(item.getId(), item.getItemName(), item.getQuantity());

                    if (success) {
                        Toast.makeText(EditPage.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditPage.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
