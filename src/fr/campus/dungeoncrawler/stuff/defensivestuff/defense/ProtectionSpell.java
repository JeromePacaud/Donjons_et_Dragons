package fr.campus.dungeoncrawler.stuff.defensivestuff.defense;

import fr.campus.dungeoncrawler.stuff.defensivestuff.DefensiveStuff;

/**
 * Sort de protection — équipement défensif pour le Magicien.
 * Réduit les dégâts reçus comme le bouclier pour le Guerrier.
 */
public class ProtectionSpell extends DefensiveStuff {

    public ProtectionSpell() {
        super("Sort de protection", "Sort", 1);
    }

    @Override
    public String toString() {
        return "=== " + this.getName() + " === \uD83E\uDEAC : " + " (Résistance : -" + this.getStatBonus() + " dégâts)";
    }
}