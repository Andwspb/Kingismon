package com.example.kingismon.model;

public abstract class LUTemon {
    private String name;
    private int level;
    private int hp;
    private int attack;
    private int defense;

    private int imageID;

    // Constructor
    public LUTemon(String name, int level, int hp, int attack, int defense, int imageID) {
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.imageID = imageID;
    }


    //Getters
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getImageID() {
        return imageID;
    }

    // Used for timer implementation
    private long lastTrainedTime = 0;
    public long getLastTrainedTime() {
        return lastTrainedTime;
    }
    public void setLastTrainedTime(long time) {
        this.lastTrainedTime = time;
    }


    // Attack function
    public void attack(LUTemon opponent) {
        int damage = this.attack - opponent.getDefense();
        if (damage < 0) {
            damage = 0;
        }
        opponent.takeDamage(damage);
    }

    // takeDamage function
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    // Level up function, increases level, hp and attack if LUTemon wins
    public void LevelUp() {
        this.level ++;
        this.hp += 2;
        this.attack ++;
    }


    // LUTemon's stats are printed in a readable format
    public String prettyPrint() {
        return  "HP: " + hp + "\n" +
                "Attack: " + attack + "\n" +
                "Defense: " + defense + "\n" +
                "Level: " + level + "\n";
    }

}
