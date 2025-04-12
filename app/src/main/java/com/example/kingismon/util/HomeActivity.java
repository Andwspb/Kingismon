package com.example.kingismon.util;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kingismon.R;
import com.example.kingismon.model.LUTemon;

import java.util.ArrayList;
import java.util.Iterator;


public class HomeActivity extends AppCompatActivity {
    // Define the RecyclerView and MovieAdapter fields
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private Button backButton;

    // Implement the onCreate method to set the content view, initialize the RecyclerView, and load movies
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the RecyclerView and set the layout manager
        recyclerView = findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backButton = findViewById(R.id.backButton);

        // Set click listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Get a copy of all LUTemons
        ArrayList<LUTemon> displayedLUT = new ArrayList<>(GameManager.getInstance().getLUTemons());

        // Filter out LUTemons that are in training
        Iterator<LUTemon> iterator = displayedLUT.iterator();
        while (iterator.hasNext()) {
            LUTemon lutemon = iterator.next();
            if (GameManager.getInstance().getTrainers().contains(lutemon)) {
                iterator.remove();
            }
        }

        // Use the filtered list for the adapter
        homeAdapter = new HomeAdapter(displayedLUT);
        recyclerView.setAdapter(homeAdapter);
    }
}
