package com.example.kingismon.util;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kingismon.R;


public class MainActivity extends AppCompatActivity {

    private Button homeButton, createNewLutemonButton, trainingAreaButton, battleArenaButton, statsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // Find all buttons
        homeButton = findViewById(R.id.homeButton);
        createNewLutemonButton = findViewById(R.id.createNewLutemonButton);
        trainingAreaButton = findViewById(R.id.trainingAreaButton);
        battleArenaButton = findViewById(R.id.battleArenaButton);


        // Set click listeners
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        createNewLutemonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        trainingAreaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrainerActivity.class);
                startActivity(intent);
            }
        });

        battleArenaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BattleActivity.class);
                startActivity(intent);
            }
        });

    }
}