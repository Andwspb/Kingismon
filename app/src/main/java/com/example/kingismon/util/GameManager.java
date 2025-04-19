package com.example.kingismon.util;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.kingismon.model.AirLUTemon;
import com.example.kingismon.model.ElectricLUTemon;
import com.example.kingismon.model.FireLUTemon;
import com.example.kingismon.model.GrassLUTemon;
import com.example.kingismon.model.LUTemon;
import com.example.kingismon.model.WaterLUTemon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class GameManager {
    private static GameManager instance;
    private ArrayList<LUTemon> lutemons;
    private ArrayList<LUTemon> trainers;

    private GameManager() {
        lutemons = new ArrayList<>();
        trainers = new ArrayList<>();
        loadStateFromJson();
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
        saveStateToJson();
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
        saveStateToJson();
    }

    public void removeTrainer(LUTemon trainer) {
        trainers.remove(trainer);
        // Ensure it's still in the lutemons list
        if (!lutemons.contains(trainer)) {
            lutemons.add(trainer);
        }
        saveStateToJson();
    }

    public void saveStateToJson(){
        JSONObject stateJson = new JSONObject();
        try {
            JSONArray lutemonsArray = new JSONArray();
            for (LUTemon lutemon : lutemons) {
                lutemonsArray.put(lutemon.toJson());
            }

            JSONArray trainerArray = new JSONArray();
            for (LUTemon trainer : trainers) {
                trainerArray.put(trainer.toJson());
            }

            stateJson.put("lutemons", lutemonsArray);
            stateJson.put("trainers", trainerArray);

            try (FileOutputStream fos = LUTemonApp.getAppContext().openFileOutput("state.json", Context.MODE_PRIVATE)) {
                fos.write(stateJson.toString().getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadStateFromJson() {
        try {
            JSONObject stateJson = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                stateJson = new JSONObject(new String(LUTemonApp.getAppContext().openFileInput("state.json").readAllBytes()));
            }
            JSONArray lutemonsArray = stateJson.getJSONArray("lutemons");
            JSONArray trainerArray = stateJson.getJSONArray("trainers");
            for (int i = 0; i < lutemonsArray.length(); i++) {
                JSONObject lutemonJson = lutemonsArray.getJSONObject(i);
                switch (lutemonJson.getString("type")){
                    case "fire":
                        lutemons.add(new FireLUTemon(lutemonJson));
                        break;
                    case "water":
                        lutemons.add(new WaterLUTemon(lutemonJson));
                        break;
                    case "electric":
                        lutemons.add(new ElectricLUTemon(lutemonJson));
                        break;
                    case "grass":
                        lutemons.add(new GrassLUTemon(lutemonJson));
                        break;
                    case "air":
                        lutemons.add(new AirLUTemon(lutemonJson));
                        break;
                }
            }
            for (int i = 0; i < trainerArray.length(); i++) {
                JSONObject trainerJson = trainerArray.getJSONObject(i);
                switch (trainerJson.getString("type")){
                    case "fire":
                        lutemons.add(new FireLUTemon(trainerJson));
                        break;
                    case "water":
                        lutemons.add(new WaterLUTemon(trainerJson));
                        break;
                    case "electric":
                        lutemons.add(new ElectricLUTemon(trainerJson));
                        break;
                    case "grass":
                        lutemons.add(new GrassLUTemon(trainerJson));
                        break;
                    case "air":
                        lutemons.add(new AirLUTemon(trainerJson));
                        break;
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, "loadStateFromJson: ", e);
        }
    }

    public ArrayList<LUTemon> getLUTemons() {
        return lutemons;
    }

    public ArrayList<LUTemon> getTrainers() {
        return trainers;
    }

}
