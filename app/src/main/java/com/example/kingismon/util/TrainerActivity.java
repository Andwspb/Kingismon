package com.example.kingismon.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.kingismon.R;
import com.example.kingismon.model.*;

import java.util.ArrayList;
import java.util.List;

public class TrainerActivity extends AppCompatActivity {

    private Spinner lutemonSpinner;
    private TextView lutemonStatsText;
    private Button trainButton, backButtonTrain, homeButton;

    private GameManager gameManager;
    private LUTemon selectedLutemon;
    private List<LUTemon> availableLutemons;

    // Set training cooldown period (in milliseconds)
    private static final long TRAINING_COOLDOWN = 60000; // 1 minute (for testing, should be 3600000 for 1 hour in production)

    // Handler for updating the cooldown timer
    private Handler cooldownHandler;
    private Runnable cooldownRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer);

        // Initialize GameManager instance
        gameManager = GameManager.getInstance();

        // Initialize the handler for updating cooldowns
        cooldownHandler = new Handler(Looper.getMainLooper());

        // Link UI components to variables
        lutemonSpinner = findViewById(R.id.lutemonSpinner);
        lutemonStatsText = findViewById(R.id.lutemonStatsText);
        trainButton = findViewById(R.id.trainButton);
        backButtonTrain = findViewById(R.id.backButtonTrain);
        homeButton = findViewById(R.id.homeButton);

        // Load LUTemons available for training
        loadAvailableLutemons();

        // Configure the spinner to display LUTemons
        setupLutemonSpinner();

        // Set up button click listeners
        setupButtonListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh LUTemon data and update the spinner when returning to this activity
        loadAvailableLutemons();
        updateSpinner();

        // Start the cooldown timer update
        startCooldownUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the cooldown timer when the activity is paused
        stopCooldownUpdates();
    }

    // Start periodic updates of cooldown timers
    private void startCooldownUpdates() {
        stopCooldownUpdates(); // Stop any existing updates

        cooldownRunnable = new Runnable() {
            @Override
            public void run() {
                // Update the UI to show current cooldown status
                updateButtonStates();
                // Schedule the next update in 1 second
                cooldownHandler.postDelayed(this, 1000);
            }
        };

        // Start immediate first update
        cooldownHandler.post(cooldownRunnable);
    }

    // Stop cooldown timer updates
    private void stopCooldownUpdates() {
        if (cooldownHandler != null && cooldownRunnable != null) {
            cooldownHandler.removeCallbacks(cooldownRunnable);
        }
    }

    // Load LUTemons from GameManager
    private void loadAvailableLutemons() {
        availableLutemons = new ArrayList<>(gameManager.getLUTemons());
        updateButtonStates();
    }

    // Configure the spinner to display LUTemon names
    private void setupLutemonSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, getLutemonNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lutemonSpinner.setAdapter(adapter);

        // Handle spinner item selection
        lutemonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (availableLutemons.size() > position) {
                    selectedLutemon = availableLutemons.get(position);
                    updateLutemonStats();
                    updateButtonStates();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLutemon = null;
                lutemonStatsText.setText("No LUTemon selected");
                updateButtonStates();
            }
        });
    }

    // Refresh the spinner with updated LUTemon data
    private void updateSpinner() {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) lutemonSpinner.getAdapter();
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(getLutemonNames());
            adapter.notifyDataSetChanged();
        }

        if (availableLutemons.isEmpty()) {
            lutemonStatsText.setText("No LUTemons available");
            selectedLutemon = null;
        } else if (lutemonSpinner.getSelectedItemPosition() >= 0 &&
                lutemonSpinner.getSelectedItemPosition() < availableLutemons.size()) {
            selectedLutemon = availableLutemons.get(lutemonSpinner.getSelectedItemPosition());
            updateLutemonStats();
        }

        updateButtonStates();
    }

    // Retrieve LUTemon names for the spinner
    private List<String> getLutemonNames() {
        ArrayList<String> names = new ArrayList<>();
        for (LUTemon lutemon : availableLutemons) {
            names.add(lutemon.getName());
        }
        return names;
    }

    // Display the selected LUTemon's stats
    private void updateLutemonStats() {
        if (selectedLutemon != null) {
            String type = getLutemonType(selectedLutemon);
            lutemonStatsText.setText(
                    "Name: " + selectedLutemon.getName() + "\n" +
                            "Type: " + type + "\n" +
                            "Level: " + selectedLutemon.getLevel() + "\n" +
                            "HP: " + selectedLutemon.getHp() + "\n" +
                            "Attack: " + selectedLutemon.getAttack() + "\n" +
                            "Defense: " + selectedLutemon.getDefense() + "\n" +
                            getTrainingStatusText(selectedLutemon)
            );
        } else {
            lutemonStatsText.setText("No LUTemon selected");
        }
    }

    // Get text describing training status
    private String getTrainingStatusText(LUTemon lutemon) {
        long currentTime = System.currentTimeMillis();
        long lastTrainedTime = lutemon.getLastTrainedTime();

        if (lastTrainedTime == 0) {
            return "Training Status: Ready";
        }

        long timeSinceTraining = currentTime - lastTrainedTime;
        if (timeSinceTraining >= TRAINING_COOLDOWN) {
            return "Training Status: Ready";
        } else {
            long remainingTimeMillis = TRAINING_COOLDOWN - timeSinceTraining;
            int remainingMinutes = (int) (remainingTimeMillis / 60000);
            int remainingSeconds = (int) ((remainingTimeMillis / 1000) % 60);
            return String.format("Training Status: Cooldown (%d:%02d remaining)",
                    remainingMinutes, remainingSeconds);
        }
    }

    // Determine the type of the selected LUTemon
    private String getLutemonType(LUTemon lutemon) {
        if (lutemon instanceof FireLUTemon) {
            return "Fire";
        } else if (lutemon instanceof WaterLUTemon) {
            return "Water";
        } else if (lutemon instanceof GrassLUTemon) {
            return "Grass";
        } else if (lutemon instanceof ElectricLUTemon) {
            return "Electric";
        } else if (lutemon instanceof AirLUTemon) {
            return "Air";
        }
        return "Unknown";
    }

    // Set up button click actions
    private void setupButtonListeners() {
        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainSelectedLutemon();
            }
        });

        backButtonTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSelectedLutemonHome();
            }
        });
    }

    // Train the selected LUTemon and handle cooldown logic
    private void trainSelectedLutemon() {
        if (selectedLutemon != null) {
            long currentTime = System.currentTimeMillis();
            long lastTrainedTime = selectedLutemon.getLastTrainedTime();

            if (lastTrainedTime == 0 || currentTime - lastTrainedTime >= TRAINING_COOLDOWN) {
                // Train the LUTemon
                selectedLutemon.LevelUp();
                selectedLutemon.setLastTrainedTime(currentTime);

                // Add to trainers list if not already there
                if (!gameManager.getTrainers().contains(selectedLutemon)) {
                    gameManager.addTrainer(selectedLutemon);
                }

                Toast.makeText(this, selectedLutemon.getName() + " trained! Level up to " +
                        selectedLutemon.getLevel(), Toast.LENGTH_SHORT).show();

                // Update UI
                updateLutemonStats();
                updateButtonStates();
            } else {
                // Calculate and display remaining cooldown time
                long remainingTimeMillis = TRAINING_COOLDOWN - (currentTime - lastTrainedTime);
                int remainingMinutes = (int) (remainingTimeMillis / 60000);
                int remainingSeconds = (int) ((remainingTimeMillis / 1000) % 60);

                Toast.makeText(this, selectedLutemon.getName() + " is still recovering from training! " +
                                "Available in " + remainingMinutes + " min " + remainingSeconds + " sec",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No LUTemon selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Send the selected LUTemon back to home
    private void sendSelectedLutemonHome() {
        if (selectedLutemon != null) {
            long currentTime = System.currentTimeMillis();
            long lastTrainedTime = selectedLutemon.getLastTrainedTime();

            if (lastTrainedTime == 0 || currentTime - lastTrainedTime >= TRAINING_COOLDOWN) {
                // Remove from trainers list
                gameManager.removeTrainer(selectedLutemon);

                Toast.makeText(this, selectedLutemon.getName() + " was sent home",
                        Toast.LENGTH_SHORT).show();

                // Refresh the list and UI
                loadAvailableLutemons();
                updateSpinner();
            } else {
                // Calculate and display remaining cooldown time
                long remainingTimeMillis = TRAINING_COOLDOWN - (currentTime - lastTrainedTime);
                int remainingMinutes = (int) (remainingTimeMillis / 60000);
                int remainingSeconds = (int) ((remainingTimeMillis / 1000) % 60);

                Toast.makeText(this, selectedLutemon.getName() + " is still recovering from training! " +
                                "Available in " + remainingMinutes + " min " + remainingSeconds + " sec",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No LUTemon selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Update all button states based on the current context
    private void updateButtonStates() {
        if (selectedLutemon == null) {
            // No LUTemon selected
            trainButton.setEnabled(false);
            trainButton.setText("Train");
            homeButton.setEnabled(false);
            return;
        }

        long currentTime = System.currentTimeMillis();
        long lastTrainedTime = selectedLutemon.getLastTrainedTime();
        boolean isOnCooldown = lastTrainedTime > 0 && currentTime - lastTrainedTime < TRAINING_COOLDOWN;

        // Update train button
        trainButton.setEnabled(!isOnCooldown);
        if (isOnCooldown) {
            long remainingTimeMillis = TRAINING_COOLDOWN - (currentTime - lastTrainedTime);
            int remainingMinutes = (int) (remainingTimeMillis / 60000);
            int remainingSeconds = (int) ((remainingTimeMillis / 1000) % 60);
            trainButton.setText(String.format("Train (Available in %d:%02d)",
                    remainingMinutes, remainingSeconds));
        } else {
            trainButton.setText("Train");
        }

        // Update home button
        homeButton.setEnabled(!isOnCooldown);

        // Update the LUTemon stats to reflect current status
        updateLutemonStats();
    }
}