package com.example.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {
    private String[] categories;
    private String selectedCategory = "";
    private Context context;

    public AdapterCategory(Context context, String[] categories) {
        this.context = context;
        this.categories = categories;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategory.ViewHolder holder, int position) {
        String category = categories[position];
        holder.textCategory.setText(category);
        int imageResouce = context.getResources().getIdentifier("@drawable/" + category, null, context.getPackageName());
        holder.imageView.setImageResource(imageResouce);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).onCategoryClicked(category);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCategory;
        ImageView imageView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.textCategory);
            imageView = itemView.findViewById(R.id.imageCategory);
            cardView = itemView.findViewById(R.id.cardViewCategory);
        }
    }

}
