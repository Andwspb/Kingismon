package com.example.kingismon.model;


import com.example.kingismon.R;

public class GrassLUTemon extends LUTemon {

    private int natureEnergy;

    public GrassLUTemon(String name) {
        super(name, 0, 19, 6, 3, R.drawable.img_4);
        this.natureEnergy = 12; // Default value for natureEnergy
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
}
