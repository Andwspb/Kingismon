package com.example.kingismon.util;

// Add the necessary import statements

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kingismon.model.LUTemon;
import com.example.kingismon.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private List<LUTemon> lutemonList;


    //Constructor
    public HomeAdapter(List<LUTemon> lutemonList) {
        this.lutemonList = lutemonList;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lutemon, parent, false);
        return new HomeViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        LUTemon lutemon = lutemonList.get(position);

        if (lutemon == null) {
            // Log or handle the case where the lutemon object is null
            Log.e("HomeAdapter", "LUTemon object is null at position: " + position);
            return;
        }

        if (holder.textViewLutemonName != null) {
            holder.textViewLutemonName.setText(lutemon.getName());
        } else {
            Log.e("HomeAdapter", "textViewLutemonName is null in ViewHolder");
        }

        if (holder.textViewLevel != null) {
            holder.textViewLevel.setText(String.valueOf(lutemon.getLevel())); // Assuming getLevel() returns an int
        } else {
            Log.e("HomeAdapter", "textViewLevel is null in ViewHolder");
        }

        if (holder.textViewLutemonInfo != null) {
            holder.textViewLutemonInfo.setText(lutemon.prettyPrint());
        } else {
            Log.e("HomeAdapter", "textViewLutemonInfo is null in ViewHolder");
        }

        if (holder.imagePoster != null) {
            holder.imagePoster.setImageResource(lutemon.getImageID()); // Assuming getImageResourceId() returns a drawable resource ID
        } else {
            Log.e("HomeAdapter", "imagePoster is null in ViewHolder");
        }
    }

    @Override
    public int getItemCount() {
        return lutemonList.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLutemonName, textViewLevel, textViewLutemonInfo;
        ImageView imagePoster;

        // Implement the constructor for the MovieViewHolder class
        public HomeViewHolder(View view) {
            super(view);
            imagePoster = view.findViewById(R.id.imageViewLUTemon);
            textViewLutemonName = view.findViewById(R.id.textViewLutemonNm);
            textViewLevel = view.findViewById(R.id.textViewLevel);
            textViewLutemonInfo = view.findViewById(R.id.textViewLutemonInfo);
        }
    }
}
