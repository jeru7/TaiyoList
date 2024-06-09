package com.example.taiyomarket.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taiyomarket.R;
import com.example.taiyomarket.classes.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;

    private OnItemLongClickListener itemLongClickListener;

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
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    if (itemLongClickListener != null) {
                        itemLongClickListener.onItemLongClick(adapterPosition, item);
                        return true;
                    }
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
            itemName = (TextView) view.findViewById(R.id.item_name);
            itemQuantity = (TextView) view.findViewById(R.id.item_quantity);
            checkbox = (ImageView) view.findViewById(R.id.checkbox_btn);
        }
    }

    public void setItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, Item item);
    }

}
