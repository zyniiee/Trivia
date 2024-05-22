package com.example.a2_p2.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_p2.Model.CategoryModel;
import com.example.a2_p2.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<CategoryModel> categoryModelList;
    private Context context;

    public CategoryAdapter(List<CategoryModel> categoryModelList, Context context) {
        this.categoryModelList = categoryModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_rv_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryModel categoryItem = categoryModelList.get(position);
        Log.d("Adapter", "Binding category: " + categoryItem.getCategoryName()); // Log each item binding
        holder.categoryNameView.setText(categoryItem.getCategoryName());
        holder.categoryIdView.setText(String.valueOf(categoryItem.getId()));
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryNameView;
        public TextView categoryIdView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameView = itemView.findViewById(R.id.categoryRvTv);
            categoryIdView = itemView.findViewById(R.id.categoryIdRvTv);
        }
    }
}
