package fr.campus.dungeoncrawler.stuff.defensivestuff.healing;

/**
 * Classe représentant une potion de soin standard dans le jeu.
 * Cette potion offre un bonus de soin modéré, idéale pour les situations courantes.
 */
public class StandardPotion extends Potion {
    public StandardPotion() {
        super("Potion standard", 2);
    }

    @Override
    public String toString() {
        return "=== Potion === \uD83E\uDDEA : " + this.getName() + " (Soin : +" + this.getStatBonus() + " PV)";
    }
}
