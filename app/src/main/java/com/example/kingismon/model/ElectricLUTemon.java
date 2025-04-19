package com.example.kingismon.model;


import com.example.kingismon.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ElectricLUTemon extends LUTemon {
    private int electricStrike;

    public ElectricLUTemon(String name) {
        super(name, 0, 16, 9, 1, R.drawable.img_5, "electric");
        this.electricStrike = 18;
    }

    public ElectricLUTemon(JSONObject lutemon) {
        super(lutemon);
        try {
            this.electricStrike = (int)lutemon.get("electricStrike");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getElectricStrike() {
        return electricStrike;
    }

    public void specialAttack(LUTemon opponent) {
        int damage = (this.electricStrike * 2) + (opponent.getHp() / 4) - opponent.getDefense();
        if (damage < 0) {
            damage = 0;
        }
        opponent.takeDamage(damage);
    }

    @Override
    public JSONObject toJson() {
        JSONObject lutemonJson = super.toJson();
        try {
            lutemonJson.put("electricStrike", electricStrike);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lutemonJson;
    }
}
