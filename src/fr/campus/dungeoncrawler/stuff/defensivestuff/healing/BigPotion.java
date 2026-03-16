package fr.campus.dungeoncrawler.stuff.defensivestuff.healing;

public class BigPotion extends Potion {
    public BigPotion() {
        super("Grande potion", 5);
    }

    @Override
    public String toString() {
        return "=== Potion === ⚗️ : " + this.getName() + " (Soin : +" + this.getStatBonus() + " PV)";
    }
}
