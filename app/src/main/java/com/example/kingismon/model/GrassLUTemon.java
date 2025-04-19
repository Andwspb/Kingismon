package com.example.kingismon.model;


import com.example.kingismon.R;

import org.json.JSONObject;

public class GrassLUTemon extends LUTemon {

    private int natureEnergy;

    public GrassLUTemon(String name) {
        super(name, 0, 19, 6, 3, R.drawable.img_4, "grass");
        this.natureEnergy = 12; // Default value for natureEnergy
    }

    public GrassLUTemon(JSONObject lutemon) {
        super(lutemon);
        try {
            this.natureEnergy = (int) lutemon.get("natureEnergy");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getNatureEnergy() {
        return natureEnergy;
    }

    public void specialAttack(LUTemon opponent) {
        int damage = (this.natureEnergy * 2) + (opponent.getHp() / 4) - opponent.getDefense();
        if (damage < 0) {
            damage = 0;
        }
        opponent.takeDamage(damage);
    }

    @Override
    public JSONObject toJson() {
        JSONObject lutemonJson = super.toJson();
        try {
            lutemonJson.put("natureEnergy", natureEnergy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lutemonJson;
    }
}
