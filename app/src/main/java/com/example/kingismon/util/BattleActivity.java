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
import java.util.Random;

public class BattleActivity extends AppCompatActivity {

    private ImageView playerImage, opponentImage;
    private TextView playerNameText, opponentNameText;
    private TextView playerHealthText, opponentHealthText;
    private TextView playerStatsText, opponentStatsText;
    private TextView battleLogText;
    private Button nextAttackButton, superAttackButton, endBattleButton, startBattleButton;
    private ProgressBar playerHealthBar, opponentHealthBar;
    private Spinner playerLutemonSpinner, opponentLutemonSpinner;

    private LUTemon playerLutemon;
    private LUTemon opponentLutemon;

    private int playerMaxHP;
    private int opponentMaxHP;

    private boolean playerTurn = true;
    private boolean usedSuperAttack = false;
    private boolean battleStarted = false;
    private Random random = new Random();

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

        // Add buttons for starting battle
        startBattleButton = findViewById(R.id.startBattleButton);

        // Add spinners for lutemon selection
        playerLutemonSpinner = findViewById(R.id.playerLutemonSpinner);
        opponentLutemonSpinner = findViewById(R.id.opponentLutemonSpinner);

        // Disable battle buttons until battle starts
        nextAttackButton.setEnabled(false);
        superAttackButton.setEnabled(false);

        // Clear battle log
        battleLogText.setText("Select Lutemons to begin the battle!");
    }

    private void setupLutemonSpinners() {
        ArrayList<LUTemon> availableLutemons = GameManager.getInstance().getLUTemons();

        // If no Lutemons available, create default ones
        if (availableLutemons.isEmpty()) {
            createDefaultLutemons();
            availableLutemons = GameManager.getInstance().getLUTemons();
            Toast.makeText(this, "No Lutemons found. Default Lutemons were created.", Toast.LENGTH_SHORT).show();
        }

        // Create a list of Lutemon names for the spinners
        ArrayList<String> lutemonNames = new ArrayList<>();
        for (LUTemon lutemon : availableLutemons) {
            lutemonNames.add(lutemon.getName() + " (" + lutemon.getClass().getSimpleName() + ")");
        }

        // Add random opponent option
        lutemonNames.add("Random Opponent");

        // Create the spinner adapters
        ArrayAdapter<String> playerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lutemonNames);
        playerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerLutemonSpinner.setAdapter(playerAdapter);

        ArrayAdapter<String> opponentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lutemonNames);
        opponentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opponentLutemonSpinner.setAdapter(opponentAdapter);

        // Default to Random Opponent
        opponentLutemonSpinner.setSelection(lutemonNames.size() - 1);

        // Set listeners for selection
        ArrayList<LUTemon> finalAvailableLutemons = availableLutemons;
        playerLutemonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < finalAvailableLutemons.size()) {
                    playerLutemon = finalAvailableLutemons.get(position);
                    updatePlayerPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        opponentLutemonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < finalAvailableLutemons.size()) {
                    opponentLutemon = finalAvailableLutemons.get(position);
                    updateOpponentPreview();
                } else {
                    // "Random Opponent" selected
                    opponentLutemon = null;
                    opponentNameText.setText("Random Opponent");
                    opponentStatsText.setText("Stats will be revealed in battle");
                    opponentImage.setImageResource(R.drawable.ic_launcher_foreground); // Default image
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void createDefaultLutemons() {
        GameManager.getInstance().addLUTemon(new FireLUTemon("Pinky"));
        GameManager.getInstance().addLUTemon(new WaterLUTemon("Blue"));
        GameManager.getInstance().addLUTemon(new GrassLUTemon("Leafy"));
        GameManager.getInstance().addLUTemon(new ElectricLUTemon("Sparky"));
        GameManager.getInstance().addLUTemon(new AirLUTemon("Windy"));
    }

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

    private void selectRandomOpponent() {
        ArrayList<LUTemon> availableLutemons = GameManager.getInstance().getLUTemons();

        // If only one Lutemon available or player selected the only one, create a random opponent
        if (availableLutemons.size() <= 1 ||
                (availableLutemons.size() == 2 && availableLutemons.contains(playerLutemon))) {
            // Choose a random opponent type
            int opponentType = random.nextInt(5);
            switch (opponentType) {
                case 0:
                    opponentLutemon = new WaterLUTemon("Black Knight");
                    break;
                case 1:
                    opponentLutemon = new GrassLUTemon("Black Knight");
                    break;
                case 2:
                    opponentLutemon = new FireLUTemon("Black Knight");
                    break;
                case 3:
                    opponentLutemon = new ElectricLUTemon("Black Knight");
                    break;
                default:
                    opponentLutemon = new AirLUTemon("Black Knight");
                    break;
            }
        } else {
            // Choose a different Lutemon from available ones (not the player's)
            int opponentIndex;
            do {
                opponentIndex = random.nextInt(availableLutemons.size());
            } while (availableLutemons.get(opponentIndex) == playerLutemon);

            opponentLutemon = availableLutemons.get(opponentIndex);
        }
    }

    private void setupButtonListeners() {
        startBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBattle();
                // Hide spinners and start button
                playerLutemonSpinner.setVisibility(View.GONE);
                opponentLutemonSpinner.setVisibility(View.GONE);
                startBattleButton.setVisibility(View.GONE);
                // Enable battle buttons
                nextAttackButton.setEnabled(true);
                superAttackButton.setEnabled(true);
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
                // Disable super attack button after use
                superAttackButton.setEnabled(false);
                usedSuperAttack = true;
            }
        });

        endBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to main activity
                Intent intent = new Intent(BattleActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startBattle() {
        // Check if we need to select a random opponent
        if (opponentLutemon == null) {
            selectRandomOpponent();
        }

        // Update opponent UI
        opponentImage.setImageResource(opponentLutemon.getImageID());
        opponentNameText.setText(opponentLutemon.getName());
        opponentStatsText.setText("ATK: " + opponentLutemon.getAttack() +
                " DEF: " + opponentLutemon.getDefense() +
                " EXP: " + opponentLutemon.getLevel());

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
        battleLogText.setText("Battle begins! " + playerLutemon.getName() + " vs " + opponentLutemon.getName());
    }

    private void performNextAttack(boolean isSuperAttack) {
        String attackLog = "";

        if (playerTurn) {
            // Player's turn
            int previousOpponentHp = opponentLutemon.getHp();

            if (isSuperAttack) {
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
                playerLutemon.LevelUp();

                // Update UI with level up info
                attackLog += "\n" + playerLutemon.getName() + " gained experience and leveled up!";

                // Disable attack buttons
                nextAttackButton.setEnabled(false);
                superAttackButton.setEnabled(false);
            } else {
                attackLog += "\n" + opponentLutemon.getName() + " health: " + opponentLutemon.getHp() + "/" + opponentMaxHP;
                attackLog += "\n" + playerLutemon.getName() + " health: " + playerLutemon.getHp() + "/" + playerMaxHP;

                // Switch turn
                playerTurn = false;
            }
        } else {
            // Opponent's turn
            int previousPlayerHp = playerLutemon.getHp();

            // Random chance for opponent to use special attack if available
            boolean opponentUseSpecial = !usedSuperAttack && random.nextInt(5) == 0; // 20% chance

            if (opponentUseSpecial) {
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
                opponentLutemon.LevelUp();

                // Disable attack buttons
                nextAttackButton.setEnabled(false);
                superAttackButton.setEnabled(false);
            } else {
                attackLog += "\n" + opponentLutemon.getName() + " health: " + opponentLutemon.getHp() + "/" + opponentMaxHP;
                attackLog += "\n" + playerLutemon.getName() + " health: " + playerLutemon.getHp() + "/" + playerMaxHP;

                // Switch turn back to player
                playerTurn = true;
            }
        }

        // Update battle log
        battleLogText.setText(attackLog);

        // Update health displays and other UI elements
        updateHealthDisplays();
        updateStatsDisplay();
    }

    private void updateHealthDisplays() {
        playerHealthText.setText("HP: " + playerLutemon.getHp() + "/" + playerMaxHP);
        opponentHealthText.setText("HP: " + opponentLutemon.getHp() + "/" + opponentMaxHP);

        // Update progress bars
        playerHealthBar.setProgress(playerLutemon.getHp());
        opponentHealthBar.setProgress(opponentLutemon.getHp());
    }

    private void updateStatsDisplay() {
        playerStatsText.setText("ATK: " + playerLutemon.getAttack() +
                " DEF: " + playerLutemon.getDefense() +
                " EXP: " + playerLutemon.getLevel());

        opponentStatsText.setText("ATK: " + opponentLutemon.getAttack() +
                " DEF: " + opponentLutemon.getDefense() +
                " EXP: " + opponentLutemon.getLevel());
    }
}