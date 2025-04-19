package com.example.kingismon.model;


import com.example.kingismon.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AirLUTemon extends LUTemon {

    private int windCapicity;

    public AirLUTemon(String name) {
        super(name, 18, 7, 2, R.drawable.img_3, "air");
        this.windCapicity = getAttack() + 2*getLevel();; // Default value for windCapicity
    }

    public AirLUTemon(JSONObject lutemon) {
        super(lutemon);
        try {
            this.windCapicity = (int)lutemon.get("windCapicity");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSpecialAttack() {
        return "WindCapicity: " + windCapicity;
    }

    public void specialAttack(LUTemon opponent) {
        int damage = (windCapicity) - opponent.getDefense();
        if (damage < 0) {
            damage = 0;
        }
        opponent.takeDamage(damage);
    }

    @Override
    public JSONObject toJson() {
        JSONObject lutemonJson = super.toJson();
        try {
            lutemonJson.put("windCapicity", windCapicity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lutemonJson;
    }
}
