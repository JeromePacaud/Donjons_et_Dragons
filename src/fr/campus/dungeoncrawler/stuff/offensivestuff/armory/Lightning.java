package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.stuff.offensivestuff.Spell;

public class Lightning extends Spell {
    public Lightning() {
        super("Éclaire", 2);
    }

    @Override
    public  String toString() {
        return "=== " + this.getName() + " ===" + "⚡\uFE0F(Attaque : " + this.getStatBonus() + ")";
    }
}
