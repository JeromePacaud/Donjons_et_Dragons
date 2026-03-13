package fr.campus.dungeoncrawler.stuff.defensivestuff.healing;

/**
 * Potion Coup de Tonnerre.
 * Active un buff qui double la puissance d'attaque du personnage pour le prochain combat uniquement.
 */
public class ThunderBolt extends Potion {

    public ThunderBolt() {
        super("Coup de Tonnerre", 0);
    }

    @Override
    public String toString() {
        return "=== " + this.getName() + " === ⚡ (Double l'attaque pour le prochain combat)";
    }
}