package fr.campus.dungeoncrawler.stuff.defensivestuff.healing;

public class StandardPotion extends Potion {
    public StandardPotion() {
        super("Potion standard", 2);
    }

    @Override
    public String toString() {
        return "=== Potion === \uD83E\uDDEA : " + this.getName() + " (Soin : +" + this.getStatBonus() + " PV)";
    }
}
