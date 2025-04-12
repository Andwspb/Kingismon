package com.example.kingismon.util;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kingismon.R;
import com.example.kingismon.model.*;


public class CreateActivity extends AppCompatActivity {

    private EditText nameEditText;
    private RadioGroup typeRadioGroup;
    private Button createButton;
    private Button backButtonCreate;

    private RadioButton selectedRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);

        backButtonCreate = findViewById(R.id.backButtonCreate);
        typeRadioGroup = findViewById(R.id.typeRadioGroup);
        nameEditText = findViewById(R.id.nameEditText);
        createButton = findViewById(R.id.createButton);

        // Set click listeners
        backButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the name from the EditText
                String name = nameEditText.getText().toString().trim();

                // Validate name
                if (name.isEmpty()) {
                    Toast.makeText(CreateActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get the selected type
                int selectedTypeId = typeRadioGroup.getCheckedRadioButtonId();
                selectedRadioButton = findViewById(selectedTypeId);

                // Validate type selection
                if (selectedTypeId == -1) {
                    Toast.makeText(CreateActivity.this, "Please select a type", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create the LUTemon based on the selected type and add it to the GameManager
                if (selectedRadioButton.getId() == R.id.fireRadioButton) {
                    GameManager.getInstance().addLUTemon(new FireLUTemon(name));
                    Toast.makeText(CreateActivity.this, "LUTemon created", Toast.LENGTH_SHORT).show();
                } else if (selectedRadioButton.getId() == R.id.waterRadioButton) {
                    GameManager.getInstance().addLUTemon(new WaterLUTemon(name));
                    Toast.makeText(CreateActivity.this, "LUTemon created", Toast.LENGTH_SHORT).show();
                } else if (selectedRadioButton.getId() == R.id.grassRadioButton) {
                    GameManager.getInstance().addLUTemon(new GrassLUTemon(name));
                    Toast.makeText(CreateActivity.this, "LUTemon created", Toast.LENGTH_SHORT).show();
                } else if (selectedRadioButton.getId() == R.id.airRadioButton) {
                    GameManager.getInstance().addLUTemon(new AirLUTemon(name));
                    Toast.makeText(CreateActivity.this, "LUTemon created", Toast.LENGTH_SHORT).show();
                } else if (selectedRadioButton.getId() == R.id.electricRadioButton) {
                    GameManager.getInstance().addLUTemon(new ElectricLUTemon(name));
                    Toast.makeText(CreateActivity.this, "LUTemon created", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}