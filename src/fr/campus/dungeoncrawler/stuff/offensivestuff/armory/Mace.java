package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.stuff.offensivestuff.Weapon;

public class Mace extends Weapon {
    public Mace() {
        super("Massue", 3);
    }

    @Override
    public String toString() {
        return "=== " + this.getName() + " ===" + " \uD83D\uDD28 (Attaque : " + this.getStatBonus() + ")";
    }
}
