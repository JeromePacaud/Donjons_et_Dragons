package fr.campus.dungeoncrawler.stuff.defensivestuff.healing;

/**
 * Classe représentant une grande potion de soin dans le jeu.
 * Cette potion offre un bonus de soin plus important que les potions de base.
 */
public class BigPotion extends Potion {
    public BigPotion() {
        super("Grande potion", 5);
    }

    @Override
    public String toString() {
        return "=== Potion === ⚗️ : " + this.getName() + " (Soin : +" + this.getStatBonus() + " PV)";
    }
}
