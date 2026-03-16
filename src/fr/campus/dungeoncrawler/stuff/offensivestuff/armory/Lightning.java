package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.stuff.offensivestuff.Spell;

/**
 * Sort d'éclair, infligeant 2 points de dégâts.
 */
public class Lightning extends Spell {
    /**
     * Constructeur de la classe Lightning, initialisant le nom et les points de dégâts.
     */
    public Lightning() {
        super("Éclaire", 2);
    }

    /**
     * Retourne une représentation textuelle de l'éclair, incluant son nom et ses points de dégâts.
     * @return une chaîne de caractères représentant l'éclair
     */
    @Override
    public  String toString() {
        return "=== " + this.getName() + " ===" + "⚡\uFE0F(Attaque : " + this.getStatBonus() + ")";
    }
}
