package com.example.kingismon.model;


import com.example.kingismon.R;

public class WaterLUTemon extends LUTemon {


    private int waterFlow;

    public WaterLUTemon(String name) {
        super(name, 0, 20, 5, 4, R.drawable.img_2);
        this.waterFlow = 10;
    }

    public int getWaterFlow() {
        return waterFlow;
    }


    public void specialAttack(LUTemon opponent) {
        int damage = (this.waterFlow * 2) + (opponent.getHp() / 4) - opponent.getDefense();
        if (damage < 0) {
            damage = 0;
        }
        opponent.takeDamage(damage);
    }

}
