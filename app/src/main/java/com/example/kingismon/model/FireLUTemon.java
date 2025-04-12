package com.example.kingismon.model;


import com.example.kingismon.R;

public class FireLUTemon extends LUTemon {

    private int firePower;

    public FireLUTemon(String name) {
        super(name, 0, 17, 8, 1, R.drawable.img_1);
        this.firePower = 16; // Default value for firePower
    }

    public int getFirePower() {
        return firePower;
    }

    public void specialAttack(LUTemon opponent) {
        int damage = (this.firePower * 2) + (opponent.getHp() / 4) - opponent.getDefense();
        if (damage < 0) {
            damage = 0;
        }
        opponent.takeDamage(damage);
    }

}
