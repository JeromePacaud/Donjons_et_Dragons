package fr.campus.dungeoncrawler.stuff.defensivestuff.defense;

import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;

public class WoodShield extends Shield {
    public WoodShield() {
        super("Bouclier en bois", "Bouclier", 1);
    }

    @Override
    public String toString() {
        return this.getName() + " \uD83D\uDEE1\uFE0F : " + " (Résistance : -" + this.getStatBonus() + " dégâts)";
    }
}
