package com.example.kingismon.model;


import com.example.kingismon.R;

public class ElectricLUTemon extends LUTemon {
    private int electricStrike;

    public ElectricLUTemon(String name) {
        super(name, 0, 16, 9, 1, R.drawable.img_5);
        this.electricStrike = 18;
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

}
