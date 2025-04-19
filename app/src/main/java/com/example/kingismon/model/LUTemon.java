package com.example.kingismon.model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class LUTemon {
    private String name;
    private int level;
    private int battles;
    private int wins;
    private int hp;
    private int attack;
    private int defense;

    private int imageID;

    private String type;


    // Constructor
    public LUTemon(String name, int hp, int attack, int defense, int imageID, String type) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.imageID = imageID;
        this.type = type;
        this.level = 0;
        this.battles = 0;
        this.wins = 0;
    }

    public LUTemon(JSONObject lutemon) {
        try {
            this.name = (String)lutemon.get("name");
            this.level = (int)lutemon.get("level");
            this.hp = (int)lutemon.get("hp");
            this.attack = (int)lutemon.get("attack");
            this.defense = (int)lutemon.get("defense");
            this.imageID = (int)lutemon.get("imageID");
            this.lastTrainedTime = lutemon.get("lastTrainedTime").getClass() == Integer.class ? (int)lutemon.get("lastTrainedTime") : (long)lutemon.get("lastTrainedTime");
            this.type = (String)lutemon.get("type");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    //Getters
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHp() {
        return hp;
    }


    public int getAttack() {
        return attack;
    }

    public String getSpecialAttack() {
        return "No special attack";
    }

    public int getDefense() {
        return defense;
    }

    public int getBattles() {
        return battles;
    }
    public int getWins() {
        return wins;
    }

    public int getImageID() {
        return imageID;
    }


    // Used for timer implementation
    private long lastTrainedTime = 0;
    public long getLastTrainedTime() {
        return lastTrainedTime;
    }
    public void setLastTrainedTime(long time) {
        this.lastTrainedTime = time;
    }


    // Attack function
    public void attack(LUTemon opponent) {
        int damage = this.attack - opponent.getDefense();
        if (damage < 0) {
            damage = 0;
        }
        opponent.takeDamage(damage);
    }

    // Increase the number of battles
    public void increaseBattles() {
        this.battles++;
    }
    // Increase the number of wins
    public void increaseWins() {
        this.wins++;
    }
    // takeDamage function
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    // Level up function, increases level, hp and attack if LUTemon wins
    public void LevelUp() {
        this.level ++;
        this.hp += 2;
        this.attack ++;
    }


    // LUTemon's stats are printed in a readable format
    public String prettyPrint() {
        return  "HP: " + hp + "\n" +
                "Attack: " + attack + "\n" +
                "Defense: " + defense + "\n" +
                "Level: " + level + "\n";
    }



    @NonNull
    public JSONObject toJson() {
        JSONObject lutemonJson = new JSONObject();
        try {
            lutemonJson.put("name", name);
            lutemonJson.put("level", level);
            lutemonJson.put("hp", hp);
            lutemonJson.put("attack", attack);
            lutemonJson.put("defense", defense);
            lutemonJson.put("lastTrainedTime", lastTrainedTime);
            lutemonJson.put("imageID", imageID);
            lutemonJson.put("type", type);
            return lutemonJson;
        } catch (Exception e) {
            Log.e(TAG, "Error converting LUTemon to JSON");
        }
        return new JSONObject();
    }



}
