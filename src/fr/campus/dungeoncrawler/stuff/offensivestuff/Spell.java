package fr.campus.dungeoncrawler.stuff.offensivestuff;

/**
 * Sort magique — équipement offensif concret pour le Mage.
 * Exemple : boule de feu, éclair, sort de glace.
 */
public class Spell extends OffensiveStuff {

    public Spell(String name, int damage) {
        super(name, "Sort", damage);
    }

    @Override
    public String toString() {
        return "Sort : " + this.getName() + " (Attaque magique : +" + this.getStatBonus() + ")";
    }
}