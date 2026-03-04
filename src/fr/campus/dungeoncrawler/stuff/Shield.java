package fr.campus.dungeoncrawler.stuff;

/**
 * Bouclier — équipement défensif concret.
 * Exemple : bouclier en bois, bouclier en acier.
 */
public class Shield extends DefensiveStuff {

    public Shield(String name, int defenseAmount) {
        super(name, "Bouclier", defenseAmount);
    }

    @Override
    public String toString() {
        return "Bouclier : " + getName() + " (Défense : +" + getStatBonus() + ")";
    }
}