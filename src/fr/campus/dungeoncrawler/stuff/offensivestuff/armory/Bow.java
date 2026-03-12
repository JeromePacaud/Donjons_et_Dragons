package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.stuff.offensivestuff.Weapon;

public class Bow extends Weapon {

    public Bow() {
        super("Bow", 4);
    }

    @Override
    public String toString() {
        return "=== " + this.getName() + " ===" + " \uD83C\uDFF9 (Attaque : " + this.getStatBonus() + ")";
    }
}
