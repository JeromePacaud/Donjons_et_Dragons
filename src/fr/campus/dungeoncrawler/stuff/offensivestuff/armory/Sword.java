package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.stuff.offensivestuff.Weapon;

/**
 * Arme de mêlée, infligeant 5 points de dégâts.
 */
public class Sword extends Weapon {
    public Sword() {
        super("Sword", 5);
    }

    @Override
    public String toString() {
        return "=== " + this.getName() + " ===" + " \uD83D\uDDE1\uFE0F (Attaque : " + this.getStatBonus() + ")";
    }
}
