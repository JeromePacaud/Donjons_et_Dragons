package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.stuff.offensivestuff.Weapon;

/**
 * Arme de mêlée, infligeant 3 points de dégâts.
 */
public class Mace extends Weapon {
    /**
     * Constructeur de la classe Mace, initialisant le nom et les points de dégâts.
     */
    public Mace() {
        super("Massue", 3);
    }

    /**
     * Retourne une représentation textuelle de la massue, incluant son nom et ses points de dégâts.
     * @return une chaîne de caractères représentant la massue
     */
    @Override
    public String toString() {
        return "=== " + this.getName() + " ===" + " \uD83D\uDD28 (Attaque : " + this.getStatBonus() + ")";
    }
}
