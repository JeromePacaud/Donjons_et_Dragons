package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.stuff.offensivestuff.Spell;

public class Fireball extends Spell {
    public Fireball() {
        super("Boule de feu", 7);
    }

    @Override
    public  String toString() {
        return "=== " + this.getName() + " ===" + " \uD83D\uDD25 (Attaque : " + this.getStatBonus() + ")";
    }
}
