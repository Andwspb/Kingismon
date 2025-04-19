package com.example.kingismon.util;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kingismon.R;
import com.example.kingismon.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BattleActivity extends AppCompatActivity {

    // UI components
    private ImageView playerImage, opponentImage, battleArrow;
    private TextView playerNameText, opponentNameText;
    private TextView playerHealthText, opponentHealthText;
    private TextView playerStatsText, opponentStatsText;
    private TextView battleLogText;
    private Button nextAttackButton, superAttackButton, endBattleButton, startBattleButton;
    private ProgressBar playerHealthBar, opponentHealthBar;
    private Spinner playerLutemonSpinner, opponentLutemonSpinner;

    // Players initialization
    private LUTemon playerLutemon;
    private LUTemon opponentLutemon;
    private int playerMaxHP;
    private int opponentMaxHP;

    // Store original HP values
    private int originalPlayerHP;
    private int originalOpponentHP;

    // Game state variables
    private boolean playerTurn = true;
    private boolean playerUsedSuperAttack = false;
    private boolean opponentUsedSuperAttack = false;
    private boolean battleStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        // Initialize UI components
        initializeUI();

        // Setup lutemon selection spinners
        setupLutemonSpinners();

        // Set up button click listeners
        setupButtonListeners();
    }

    private void initializeUI() {
        playerImage = findViewById(R.id.playerImage);
        opponentImage = findViewById(R.id.opponentImage);
        playerNameText = findViewById(R.id.playerNameText);
        opponentNameText = findViewById(R.id.opponentNameText);
        playerHealthText = findViewById(R.id.playerHealthText);
        opponentHealthText = findViewById(R.id.opponentHealthText);
        playerStatsText = findViewById(R.id.playerStatsText);
        opponentStatsText = findViewById(R.id.opponentStatsText);
        battleLogText = findViewById(R.id.battleLogText);
        nextAttackButton = findViewById(R.id.nextAttackButton);
        superAttackButton = findViewById(R.id.superAttackButton);
        endBattleButton = findViewById(R.id.endBattleButton);
        playerHealthBar = findViewById(R.id.playerHealthBar);
        opponentHealthBar = findViewById(R.id.opponentHealthBar);
        battleArrow = findViewById(R.id.battleArrow);

        // Add buttons for starting battle
        startBattleButton = findViewById(R.id.startBattleButton);

        // Add spinners for lutemon selection
        playerLutemonSpinner = findViewById(R.id.playerLutemonSpinner);
        opponentLutemonSpinner = findViewById(R.id.opponentLutemonSpinner);

        // Disable battle buttons until battle starts
        nextAttackButton.setEnabled(false);
        superAttackButton.setEnabled(false);

        // Hide battle arrow initially
        battleArrow.setVisibility(View.INVISIBLE);

        // Clear battle log
        battleLogText.setText("Select Lutemons to begin the battle!");
    }

    // Setup spinners for selecting Lutemons
    private void setupLutemonSpinners() {
        ArrayList<LUTemon> availableLutemons = GameManager.getInstance().getAvailableLUTemons();

        if (availableLutemons.isEmpty()) {
            Toast.makeText(this, "No Lutemons found. Go to Create page", Toast.LENGTH_SHORT).show();
            // Return to main activity if no Lutemons available
            finish();
            return;
        }

        // Create a list of Lutemon names for the spinners
        ArrayList<String> lutemonNames = new ArrayList<>();
        for (LUTemon lutemon : availableLutemons) {
            lutemonNames.add(lutemon.getName() + " (" + lutemon.getClass().getSimpleName() + ")");
        }

        // Create the spinner adapters
        ArrayAdapter<String> playerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_white, lutemonNames);
        playerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerLutemonSpinner.setAdapter(playerAdapter);

        ArrayAdapter<String> opponentAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_white, lutemonNames);
        opponentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opponentLutemonSpinner.setAdapter(opponentAdapter);

        // Set listeners for selection
        playerLutemonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                playerLutemon = availableLutemons.get(position);
                updatePlayerPreview();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        opponentLutemonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                opponentLutemon = availableLutemons.get(position);
                updateOpponentPreview();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    // Update the player preview with selected Lutemon details
    private void updatePlayerPreview() {
        if (playerLutemon != null) {
            playerImage.setImageResource(playerLutemon.getImageID());
            playerNameText.setText(playerLutemon.getName());
            playerStatsText.setText("ATK: " + playerLutemon.getAttack() +
                    " DEF: " + playerLutemon.getDefense() +
                    " EXP: " + playerLutemon.getLevel());
        }
    }

    private void updateOpponentPreview() {
        if (opponentLutemon != null) {
            opponentImage.setImageResource(opponentLutemon.getImageID());
            opponentNameText.setText(opponentLutemon.getName());
            opponentStatsText.setText("ATK: " + opponentLutemon.getAttack() +
                    " DEF: " + opponentLutemon.getDefense() +
                    " EXP: " + opponentLutemon.getLevel());
        }
    }

    // Set up button listeners for battle actions
    private void setupButtonListeners() {
        startBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerLutemon != null && opponentLutemon != null) {
                    // Hide spinners and start button
                    playerLutemonSpinner.setVisibility(View.GONE);
                    opponentLutemonSpinner.setVisibility(View.GONE);
                    startBattleButton.setVisibility(View.GONE);

                    // Enable battle buttons
                    nextAttackButton.setEnabled(true);
                    superAttackButton.setEnabled(true);

                    // Start the battle
                    startBattle();
                } else {
                    Toast.makeText(BattleActivity.this, "Please select both Lutemons to start the battle!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextAttackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performNextAttack(false);
            }
        });

        superAttackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performNextAttack(true);
            }
        });

        endBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restore original HP values before ending the battle
                restoreOriginalHP();

                // Return to main activity
                Intent intent = new Intent(BattleActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startBattle() {
        // Make battle arrow visible and show current turn
        battleArrow.setVisibility(View.VISIBLE);
        updateBattleArrow();

        // Store original HP values
        originalPlayerHP = playerLutemon.getHp();
        originalOpponentHP = opponentLutemon.getHp();

        // Save initial max HP values
        playerMaxHP = playerLutemon.getHp();
        opponentMaxHP = opponentLutemon.getHp();

        // Configure health bars
        playerHealthBar.setMax(playerMaxHP);
        playerHealthBar.setProgress(playerLutemon.getHp());
        opponentHealthBar.setMax(opponentMaxHP);
        opponentHealthBar.setProgress(opponentLutemon.getHp());

        // Update health displays
        updateHealthDisplays();

        // Set battle started flag
        battleStarted = true;

        // Update battle log
        battleLogText.setText("Battle begins! " + playerLutemon.getName() + " vs " + opponentLutemon.getName() +
                "\n\nIt's " + (playerTurn ? playerLutemon.getName() : opponentLutemon.getName()) + "'s turn!");
    }

    private void performNextAttack(boolean isSuperAttack) {
        if (!battleStarted) {
            startBattle();
            return;
        }

        String attackLog = "";

        if (playerTurn) {
            // Player's turn
            int previousOpponentHp = opponentLutemon.getHp();

            if (isSuperAttack && !playerUsedSuperAttack) {
                // Perform super attack based on player Lutemon type
                if (playerLutemon instanceof FireLUTemon) {
                    ((FireLUTemon) playerLutemon).specialAttack(opponentLutemon);
                    attackLog = playerLutemon.getName() + " uses FIRE SPECIAL ATTACK on " + opponentLutemon.getName() + "!";
                } else if (playerLutemon instanceof WaterLUTemon) {
                    ((WaterLUTemon) playerLutemon).specialAttack(opponentLutemon);
                    attackLog = playerLutemon.getName() + " uses WATER SPECIAL ATTACK on " + opponentLutemon.getName() + "!";
                } else if (playerLutemon instanceof GrassLUTemon) {
                    ((GrassLUTemon) playerLutemon).specialAttack(opponentLutemon);
                    attackLog = playerLutemon.getName() + " uses GRASS SPECIAL ATTACK on " + opponentLutemon.getName() + "!";
                } else if (playerLutemon instanceof ElectricLUTemon) {
                    ((ElectricLUTemon) playerLutemon).specialAttack(opponentLutemon);
                    attackLog = playerLutemon.getName() + " uses ELECTRIC SPECIAL ATTACK on " + opponentLutemon.getName() + "!";
                } else if (playerLutemon instanceof AirLUTemon) {
                    ((AirLUTemon) playerLutemon).specialAttack(opponentLutemon);
                    attackLog = playerLutemon.getName() + " uses AIR SPECIAL ATTACK on " + opponentLutemon.getName() + "!";
                }
                playerUsedSuperAttack = true;
            } else {
                // Regular attack
                playerLutemon.attack(opponentLutemon);
                attackLog = playerLutemon.getName() + " attacks " + opponentLutemon.getName() + "!";
            }

            // Calculate damage
            int damage = previousOpponentHp - opponentLutemon.getHp();

            // Add damage info to log
            if (damage > 0) {
                attackLog += "\n" + opponentLutemon.getName() + " takes " + damage + " damage!";
            } else {
                attackLog += "\n" + opponentLutemon.getName() + " resists the attack!";
            }

            // Check if opponent fainted
            if (opponentLutemon.getHp() <= 0) {
                attackLog += "\n" + opponentLutemon.getName() + " has fainted! " + playerLutemon.getName() + " wins!";

                playerLutemon.increaseBattles();
                playerLutemon.increaseWins();
                opponentLutemon.increaseBattles();

                // Disable attack buttons
                nextAttackButton.setEnabled(false);
                superAttackButton.setEnabled(false);

                // Restore original HP values when battle ends
                restoreOriginalHP();
            } else {
                // Switch turn
                playerTurn = false;
                attackLog += "\nIt's now " + opponentLutemon.getName() + "'s turn!";
            }
        } else {
            // Opponent's turn
            int previousPlayerHp = playerLutemon.getHp();

            if (isSuperAttack && !opponentUsedSuperAttack) {
                // Perform super attack based on opponent Lutemon type
                if (opponentLutemon instanceof FireLUTemon) {
                    ((FireLUTemon) opponentLutemon).specialAttack(playerLutemon);
                    attackLog = opponentLutemon.getName() + " uses FIRE SPECIAL ATTACK on " + playerLutemon.getName() + "!";
                } else if (opponentLutemon instanceof WaterLUTemon) {
                    ((WaterLUTemon) opponentLutemon).specialAttack(playerLutemon);
                    attackLog = opponentLutemon.getName() + " uses WATER SPECIAL ATTACK on " + playerLutemon.getName() + "!";
                } else if (opponentLutemon instanceof GrassLUTemon) {
                    ((GrassLUTemon) opponentLutemon).specialAttack(playerLutemon);
                    attackLog = opponentLutemon.getName() + " uses GRASS SPECIAL ATTACK on " + playerLutemon.getName() + "!";
                } else if (opponentLutemon instanceof ElectricLUTemon) {
                    ((ElectricLUTemon) opponentLutemon).specialAttack(playerLutemon);
                    attackLog = opponentLutemon.getName() + " uses ELECTRIC SPECIAL ATTACK on " + playerLutemon.getName() + "!";
                } else if (opponentLutemon instanceof AirLUTemon) {
                    ((AirLUTemon) opponentLutemon).specialAttack(playerLutemon);
                    attackLog = opponentLutemon.getName() + " uses AIR SPECIAL ATTACK on " + playerLutemon.getName() + "!";
                }
                opponentUsedSuperAttack = true;
            } else {
                // Regular attack
                opponentLutemon.attack(playerLutemon);
                attackLog = opponentLutemon.getName() + " attacks " + playerLutemon.getName() + "!";
            }

            // Calculate damage
            int damage = previousPlayerHp - playerLutemon.getHp();

            // Add damage info to log
            if (damage > 0) {
                attackLog += "\n" + playerLutemon.getName() + " takes " + damage + " damage!";
            } else {
                attackLog += "\n" + playerLutemon.getName() + " resists the attack!";
            }

            // Check if player fainted
            if (playerLutemon.getHp() <= 0) {
                attackLog += "\n" + playerLutemon.getName() + " has fainted! " + opponentLutemon.getName() + " wins!";
                playerLutemon.increaseBattles();
                opponentLutemon.increaseBattles();
                opponentLutemon.increaseWins();

                // Disable attack buttons
                nextAttackButton.setEnabled(false);
                superAttackButton.setEnabled(false);

                // Restore original HP values when battle ends
                restoreOriginalHP();
            } else {
                // Switch turn back to player
                playerTurn = true;
                attackLog += "\nIt's now " + playerLutemon.getName() + "'s turn!";
            }
        }

        // Update battle log
        battleLogText.setText(attackLog);

        // Update health displays and other UI elements
        updateHealthDisplays();
        updateStatsDisplay();
        updateBattleArrow();
    }

    // Restore original HP values after battle
    private void restoreOriginalHP() {
        if (playerLutemon != null && opponentLutemon != null) {
            // Restore HP to original values for GameManager references
            for (LUTemon lutemon : GameManager.getInstance().getLUTemons()) {
                if (lutemon.getName().equals(playerLutemon.getName())) {
                    // Only preserve level gains, reset HP to original
                    lutemon.takeDamage(0); // Reset any temporary damage-related stats
                    while (lutemon.getHp() < originalPlayerHP) {
                        lutemon.getClass().cast(lutemon).takeDamage(-1); // Heal by increasing HP
                    }
                }
                if (lutemon.getName().equals(opponentLutemon.getName())) {
                    // Only preserve level gains, reset HP to original
                    lutemon.takeDamage(0); // Reset any temporary damage-related stats
                    while (lutemon.getHp() < originalOpponentHP) {
                        lutemon.getClass().cast(lutemon).takeDamage(-1); // Heal by increasing HP
                    }
                }
            }

            // Save the updated state
            GameManager.getInstance().saveStateToJson();
        }
    }

    private void updateHealthDisplays() {
        // Update text displays
        playerHealthText.setText("HP: " + playerLutemon.getHp() + "/" + playerMaxHP);
        opponentHealthText.setText("HP: " + opponentLutemon.getHp() + "/" + opponentMaxHP);

        // Update progress bars - ensure values are within bounds
        playerHealthBar.setProgress(Math.max(0, Math.min(playerLutemon.getHp(), playerMaxHP)));
        opponentHealthBar.setProgress(Math.max(0, Math.min(opponentLutemon.getHp(), opponentMaxHP)));
    }

    private void updateStatsDisplay() {
        playerStatsText.setText("ATK: " + playerLutemon.getAttack() +
                " DEF: " + playerLutemon.getDefense() +
                " EXP: " + playerLutemon.getLevel());

        opponentStatsText.setText("ATK: " + opponentLutemon.getAttack() +
                " DEF: " + opponentLutemon.getDefense() +
                " EXP: " + opponentLutemon.getLevel());
    }

    private void updateBattleArrow() {
        if (playerTurn) {
            // Arrow pointing from player to opponent (right direction)
            battleArrow.setRotation(0);
        } else {
            // Arrow pointing from opponent to player (left direction)
            battleArrow.setRotation(180);
        }
    }

    @Override
    public void onBackPressed() {
        // Restore original HP values when back button is pressed
        restoreOriginalHP();
        super.onBackPressed();
    }
}