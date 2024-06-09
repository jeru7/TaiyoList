package com.example.taiyomarket.adapters;

import androidx.recyclerview.widget.RecyclerView;
import com.example.taiyomarket.R;
import com.example.taiyomarket.classes.ListItem;
import com.example.taiyomarket.main.ListPageView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<ListItem> items;
    private OnItemLongClickListener longClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClicked(int position);
    }

    public ListAdapter(List<ListItem> items, OnItemLongClickListener longClickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListItem item = items.get(position);
        holder.listName.setText(item.getListName());
        holder.lastUpdate.setText("Last updated since " + item.getLastUpdate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int currentPosition = holder.getAdapterPosition();
               if (currentPosition != RecyclerView.NO_POSITION) {
                   ListItem clickedItem = items.get(currentPosition);
                   long listId = clickedItem.getId();
                   Intent intent = new Intent(holder.itemView.getContext(), ListPageView.class);
                   intent.putExtra("listId", listId);
                   holder.itemView.getContext().startActivity(intent);

               }
           }
       });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    longClickListener.onItemLongClicked(currentPosition);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView listName;
        TextView lastUpdate;

        ListViewHolder(View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name);
            lastUpdate = itemView.findViewById(R.id.last_update);
        }
    }

    public List<ListItem> getItems() {
        return items;
    }
}
