package com.example.kingismon.model;


import com.example.kingismon.R;

import org.json.JSONObject;

public class GrassLUTemon extends LUTemon {

    private int natureEnergy;

    public GrassLUTemon(String name) {
        super(name, 19, 6, 3, R.drawable.img_4, "grass");
        this.natureEnergy = getAttack() + 2*getLevel(); // Default value for natureEnergy
    }

    public GrassLUTemon(JSONObject lutemon) {
        super(lutemon);
        try {
            this.natureEnergy = (int) lutemon.get("natureEnergy");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSpecialAttack() {
        return "Nature Energy: " + natureEnergy;
    }

    public void specialAttack(LUTemon opponent) {
        int damage = (natureEnergy) - opponent.getDefense();
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
