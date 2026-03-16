package fr.campus.dungeoncrawler.stuff.defensivestuff.defense;

/**
 * Bouclier en bois — équipement défensif de base pour le Guerrier.
 * Offre une résistance de 1 point contre les attaques ennemies.
 */
public class WoodShield extends Shield {
    public WoodShield() {
        super("Bouclier en bois", "Bouclier", 1);
    }

    @Override
    public String toString() {
        return this.getName() + " \uD83D\uDEE1\uFE0F : " + " (Résistance : -" + this.getStatBonus() + " dégâts)";
    }
}
