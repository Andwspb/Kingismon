package com.example.kingismon.util;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


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
    private Button nextAttackButton, superAttackButton, endBattleButton;
    private ProgressBar playerHealthBar, opponentHealthBar;

    private LUTemon playerLutemon;
    private LUTemon opponentLutemon;

    private int playerMaxHP;
    private int opponentMaxHP;

    private boolean playerTurn = true;
    private boolean usedSuperAttack = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        // Initialize UI components
        initializeUI();

        // Select player and opponent Lutemons
        selectLutemons();

        // Setup initial UI state
        setupInitialBattleState();

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
    }

    private void selectLutemons() {
        ArrayList<LUTemon> availableLutemons = GameManager.getInstance().getLUTemons();

        // If no Lutemons are available, create some for demonstration
        if (availableLutemons.isEmpty() || availableLutemons.size() < 2) {
            playerLutemon = new FireLUTemon("Pinky");

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
            // Choose a random player Lutemon from available ones
            int playerIndex = random.nextInt(availableLutemons.size());
            playerLutemon = availableLutemons.get(playerIndex);

            // Choose a different Lutemon for opponent
            int opponentIndex;
            do {
                opponentIndex = random.nextInt(availableLutemons.size());
            } while (opponentIndex == playerIndex && availableLutemons.size() > 1);

            opponentLutemon = availableLutemons.get(opponentIndex);
        }

        // Save initial max HP values
        playerMaxHP = playerLutemon.getHp();
        opponentMaxHP = opponentLutemon.getHp();
    }

    private void setupInitialBattleState() {
        // Set images
        playerImage.setImageResource(playerLutemon.getImageID());
        opponentImage.setImageResource(opponentLutemon.getImageID());

        // Set names
        playerNameText.setText(playerLutemon.getName());
        opponentNameText.setText(opponentLutemon.getName());

        // Update health displays
        updateHealthDisplays();

        // Set stats
        playerStatsText.setText("ATK: " + playerLutemon.getAttack() +
                " DEF: " + playerLutemon.getDefense() +
                " EXP: " + playerLutemon.getLevel());

        opponentStatsText.setText("ATK: " + opponentLutemon.getAttack() +
                " DEF: " + opponentLutemon.getDefense() +
                " EXP: " + opponentLutemon.getLevel());

        // Clear battle log
        battleLogText.setText("Battle begins! " + playerLutemon.getName() + " vs " + opponentLutemon.getName());

        // Configure health bars
        playerHealthBar.setMax(playerMaxHP);
        playerHealthBar.setProgress(playerLutemon.getHp());

        opponentHealthBar.setMax(opponentMaxHP);
        opponentHealthBar.setProgress(opponentLutemon.getHp());

        // Enable/disable buttons
        nextAttackButton.setEnabled(true);
        superAttackButton.setEnabled(true);
        endBattleButton.setEnabled(true);
    }

    private void setupButtonListeners() {
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
