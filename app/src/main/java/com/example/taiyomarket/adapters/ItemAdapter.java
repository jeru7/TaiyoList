package com.example.taiyomarket.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taiyomarket.R;
import com.example.taiyomarket.classes.Item;
import com.example.taiyomarket.database.DBHelper;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;
    private OnItemEditClickListener editClickListener;
    private OnItemDeleteClickListener deleteClickListener;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_adapter, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemName.setText(item.getItemName());
        holder.itemQuantity.setText("x" + item.getQuantity());
        holder.checkbox.setImageResource(item.isChecked() ? R.drawable.checked : R.drawable.uncheck);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setChecked(!item.isChecked());
                holder.checkbox.setImageResource(item.isChecked() ? R.drawable.uncheck : R.drawable.checked);
                DBHelper db = new DBHelper(v.getContext());
                db.updateIsChecked(item.getId(), item.isChecked());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("Please choose an option")
                            .setItems(new CharSequence[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            if (editClickListener != null) {
                                                editClickListener.onItemEditClick(adapterPosition, item, true);
                                            }
                                            break;
                                        case 1:
                                            if (deleteClickListener != null) {
                                                deleteClickListener.onItemDeleteClick(adapterPosition, item);
                                            }
                                            break;
                                    }
                                }
                            });
                    builder.create().show();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemQuantity;
        private ImageView checkbox;

        public ItemViewHolder(@NonNull View view) {
            super(view);
            itemName = view.findViewById(R.id.item_name);
            itemQuantity = view.findViewById(R.id.item_quantity);
            checkbox = view.findViewById(R.id.checkbox_btn);
        }
    }

    public void setOnItemEditClickListener(OnItemEditClickListener listener) {
        this.editClickListener = listener;
    }

    public void setOnItemDeleteClickListener(OnItemDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public interface OnItemEditClickListener {
        void onItemEditClick(int position, Item item, boolean isEdit);
    }

    public interface OnItemDeleteClickListener {
        void onItemDeleteClick(int position, Item item);
    }
}
