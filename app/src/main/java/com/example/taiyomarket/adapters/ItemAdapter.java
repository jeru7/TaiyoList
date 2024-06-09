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

    private OnItemLongClickListener itemLongClickListener;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    DBHelper db;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_adapter, parent, false);
        db = new DBHelper(view.getContext());
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemName.setText(item.getItemName());
        holder.itemQuantity.setText( "x" + item.getQuantity());
        holder.checkbox.setImageResource(item.isChecked() ? R.drawable.checked : R.drawable.uncheck);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setChecked(!item.isChecked());
                holder.checkbox.setImageResource(item.isChecked() ? R.drawable.uncheck : R.drawable.checked);
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
                                            if (itemLongClickListener != null) {
                                                itemLongClickListener.onItemLongClick(adapterPosition, item, true);
                                            }
                                            break;
                                        case 1:
                                            deleteItem(position, holder.itemView.getContext());
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

    public void deleteItem(int position, Context context) {
        Item itemToDelete = itemList.get(position);
        long deleteItemId = itemToDelete.getId();
        boolean isDeleted = db.deleteItem(deleteItemId);
        String itemNameDelete = itemToDelete.getItemName();

        if (isDeleted) {
            itemList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, itemNameDelete + " deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to delete " + itemNameDelete, Toast.LENGTH_SHORT).show();
        }
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
            itemName = (TextView) view.findViewById(R.id.item_name);
            itemQuantity = (TextView) view.findViewById(R.id.item_quantity);
            checkbox = (ImageView) view.findViewById(R.id.checkbox_btn);
        }
    }

    public void setItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, Item item, boolean isEdit);
    }

}
