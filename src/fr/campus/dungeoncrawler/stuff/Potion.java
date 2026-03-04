package fr.campus.dungeoncrawler.stuff;

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
        return "Potion : " + getName() + " (Soin : +" + getStatBonus() + " PV)";
    }
}