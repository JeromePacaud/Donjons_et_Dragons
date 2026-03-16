package fr.campus.dungeoncrawler.stuff.defensivestuff.defense;

import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;

/**
 * Coup de Tonnerre — équipement défensif.
 * Double l'attaque du joueur pour le prochain combat.
 */
public class Thunderbolt extends Potion {

    public Thunderbolt() {
        super("Coup de Tonnerre", 0);
    }

    @Override
    public String toString() {
        return "=== " + this.getName() + " === ⚡ (Double l'attaque pour le prochain combat)";
    }
}