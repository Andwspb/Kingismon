package com.example.kingismon.model;


import com.example.kingismon.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FireLUTemon extends LUTemon {

    private int firePower;

    public FireLUTemon(String name) {
        super(name, 17, 8, 1, R.drawable.img_1, "fire");
        this.firePower = getAttack() + 2*getLevel();; // Default value for firePower
    }

    public FireLUTemon(JSONObject lutemon) {
        super(lutemon);
        try {
            this.firePower = (int) lutemon.get("firePower");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public String getSpecialAttack() {
        return "Fire Power: " + firePower;
    }

    public void specialAttack(LUTemon opponent) {
        int damage = (firePower) - opponent.getDefense();
        if (damage < 0) {
            damage = 0;
        }
        opponent.takeDamage(damage);
    }

    @Override
    public JSONObject toJson() {
        JSONObject lutemonJson = super.toJson();
        try {
            lutemonJson.put("firePower", firePower);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lutemonJson;
    }
}
