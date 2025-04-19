package com.example.kingismon.model;


import com.example.kingismon.R;

import org.json.JSONObject;

public class WaterLUTemon extends LUTemon {


    private int waterFlow;

    public WaterLUTemon(String name) {
        super(name, 20, 5, 4, R.drawable.img_2, "water");
        this.waterFlow = getAttack() + 2*getLevel();;
    }

    public WaterLUTemon(JSONObject lutemon) {
        super(lutemon);
        try {
            this.waterFlow = (int) lutemon.get("waterFlow");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getSpecialAttack() {
        return "Water Flow: " + waterFlow;
    }


    public void specialAttack(LUTemon opponent) {
        int damage = (waterFlow) + opponent.getHp() - opponent.getDefense();
        if (damage < 0) {
            damage = 0;
        }
        opponent.takeDamage(damage);
    }

    @Override
    public JSONObject toJson() {
        JSONObject lutemonJson = super.toJson();
        try {
            lutemonJson.put("waterFlow", waterFlow);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lutemonJson;
    }
}
