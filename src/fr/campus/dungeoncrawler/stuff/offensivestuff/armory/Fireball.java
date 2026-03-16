package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.stuff.offensivestuff.Spell;


/**
 * Classe représentant une arme de type boule de feu.
 * La boule de feu inflige des dégâts de base, avec un bonus supplémentaire contre les ennemis faibles au feu.
 */
public class Fireball extends Spell {
    public Fireball() {
        super("Boule de feu", 7);
    }

    @Override
    public  String toString() {
        return "=== " + this.getName() + " ===" + " \uD83D\uDD25 (Attaque : " + this.getStatBonus() + ")";
    }
}
