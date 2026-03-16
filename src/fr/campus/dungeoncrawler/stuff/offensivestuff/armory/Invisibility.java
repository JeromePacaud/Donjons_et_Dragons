package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.character.enemy.Enemy;
import fr.campus.dungeoncrawler.character.enemy.EvilSpirit;
import fr.campus.dungeoncrawler.stuff.offensivestuff.Spell;

/**
 * Sort d'invisibilité, spécifique au Magicien.
 * Inflige 8 points de dégâts contre les Mauvais Esprits, 5 contre les autres ennemis.
 */
public class Invisibility extends Spell {

    /**
     *  Constructeur de la classe Invisibility, initialisant le nom et les points de dégâts.
     */
    public Invisibility() {
        super("Invisibility", 5);
    }

    /**
     * Retourne les points de dégâts infligés par le sort d'invisibilité, avec un bonus contre les Mauvais Esprits.
     * @param enemy l'ennemi contre lequel le sort est utilisé
     * @return les points de dégâts infligés
     */
    @Override
    public int getDamageAgainst(Enemy enemy) {
        return enemy instanceof EvilSpirit ? this.getStatBonus() + 3 : this.getStatBonus();
    }

    /**
     * Retourne une représentation textuelle du sort d'invisibilité, incluant son nom, ses points de dégâts et son bonus contre les Mauvais Esprits.
     * @return une chaîne de caractères représentant le sort d'invisibilité
     */
    @Override
    public String toString() {
        return "=== " + this.getName() + " === 👻 (Attaque : " + this.getStatBonus()
                + " | vs Mauvais Esprit : " + (this.getStatBonus() + 3) + ")";
    }
}