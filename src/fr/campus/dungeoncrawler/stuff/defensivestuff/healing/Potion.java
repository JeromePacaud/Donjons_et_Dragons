package fr.campus.dungeoncrawler.stuff.defensivestuff.healing;

import fr.campus.dungeoncrawler.stuff.defensivestuff.DefensiveStuff;

/**
 * Potion — équipement défensif concret à usage unique.
 * Restaure des points de vie (modélisé comme un bonus de défense/soin).
 * Exemple : potion de soin, élixir de vie.
 */
public class Potion extends DefensiveStuff {

    public Potion(String name, int healAmount) {
        super(name, "Potion", healAmount);
    }

    @Override
    public String getBonusLabel() {
        return "Soin";
    }

    @Override
    public String toString() {
        return "Potion";
    }
}