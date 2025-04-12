package com.example.kingismon.util;


import com.example.kingismon.model.LUTemon;

import java.util.ArrayList;

public class GameManager {
    private static GameManager instance;
    private ArrayList<LUTemon> lutemons;
    private ArrayList<LUTemon> trainers;

    private GameManager() {
        lutemons = new ArrayList<>();
        trainers = new ArrayList<>();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void addLUTemon(LUTemon lutemon) {
        // Add to lutemons list if not already there
        if (!lutemons.contains(lutemon)) {
            lutemons.add(lutemon);
        }

        // Remove from trainers if it was there
        if (trainers.contains(lutemon)) {
            trainers.remove(lutemon);
        }
    }

    public void addTrainer(LUTemon trainer) {
        // Make sure the LUTemon is in lutemons list
        if (!lutemons.contains(trainer)) {
            lutemons.add(trainer);
        }

        // Add to trainers if not already there
        if (!trainers.contains(trainer)) {
            trainers.add(trainer);
        }
    }

    public void removeTrainer(LUTemon trainer) {
        trainers.remove(trainer);
        // Ensure it's still in the lutemons list
        if (!lutemons.contains(trainer)) {
            lutemons.add(trainer);
        }
    }

    public ArrayList<LUTemon> getLUTemons() {
        return lutemons;
    }

    public ArrayList<LUTemon> getTrainers() {
        return trainers;
    }

}
