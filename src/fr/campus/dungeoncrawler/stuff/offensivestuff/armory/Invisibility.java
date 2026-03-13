package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.character.enemy.Enemy;
import fr.campus.dungeoncrawler.character.enemy.EvilSpirit;
import fr.campus.dungeoncrawler.stuff.offensivestuff.Spell;

/**
 * Sort d'invisibilité, spécifique au Magicien.
 * Inflige 8 points de dégâts contre les Mauvais Esprits, 5 contre les autres ennemis.
 */
public class Invisibility extends Spell {

    public Invisibility() {
        super("Invisibility", 5);
    }

    @Override
    public int getDamageAgainst(Enemy enemy) {
        return enemy instanceof EvilSpirit ? this.getStatBonus() + 3 : this.getStatBonus();
    }

    @Override
    public String toString() {
        return "=== " + this.getName() + " === 👻 (Attaque : " + this.getStatBonus()
                + " | vs Mauvais Esprit : " + (this.getStatBonus() + 3) + ")";
    }
}