package com.example.kingismon.model;


import com.example.kingismon.R;

public class AirLUTemon extends LUTemon {

    private int windCapicity;

    public AirLUTemon(String name) {
        super(name, 0, 18, 7, 2, R.drawable.img_3);
        this.windCapicity = 14; // Default value for windCapicity
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
}
