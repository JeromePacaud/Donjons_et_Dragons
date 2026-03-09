package fr.campus.dungeoncrawler.stuff.defensivestuff.defense;

import fr.campus.dungeoncrawler.stuff.defensivestuff.DefensiveStuff;

/**
 * Bouclier — équipement défensif concret.
 * Exemple : bouclier en bois, bouclier en acier.
 */
public class Shield extends DefensiveStuff {

    /** Constructeur de la classe Shield.
     *
     * @param name          Le nom du bouclier (ex : "Bouclier en bois").
     * @param type          Le type de l'équipement (ex : "Bouclier").
     * @param defenseAmount La quantité de défense que le bouclier offre.
     */
    public Shield(String name, String type, int defenseAmount) {
        super(name, type, defenseAmount);
    }

    /** Retourne une représentation textuelle du bouclier, incluant son nom et sa valeur de défense.
     *
     * @return Une chaîne de caractères décrivant le bouclier.
     */
    @Override
    public String toString() {
        return "=== Bouclier === \uD83D\uDEE1\uFE0F : " + this.getName() + " (Défense : +" + this.getStatBonus() + ")";
    }
}