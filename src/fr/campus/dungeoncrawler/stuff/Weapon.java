package fr.campus.dungeoncrawler.stuff;

/**
 * Arme physique — équipement offensif concret pour le Guerrier.
 * Exemple : épée, hache, massue.
 */

public class Weapon extends OffensiveStuff {

    public Weapon(String name, int damage) {
        super(name, "Arme", damage);
    }

    @Override
    public String toString() {
        return "Arme : " + getName() + " (Attaque : +" + getStatBonus() + ")";
    }
}