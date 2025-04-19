package com.example.kingismon.model;


import com.example.kingismon.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AirLUTemon extends LUTemon {

    private int windCapicity;

    public AirLUTemon(String name) {
        super(name, 0, 18, 7, 2, R.drawable.img_3, "air");
        this.windCapicity = 14; // Default value for windCapicity
    }

    public AirLUTemon(JSONObject lutemon) {
        super(lutemon);
        try {
            this.windCapicity = (int)lutemon.get("windCapicity");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getWindCapicity() {
        return windCapicity;
    }

    public void specialAttack(LUTemon opponent) {
        int damage = (this.windCapicity * 2) + (opponent.getHp() / 4) - opponent.getDefense();
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
