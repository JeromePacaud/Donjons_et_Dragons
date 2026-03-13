package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.character.enemy.Dragon;
import fr.campus.dungeoncrawler.character.enemy.Enemy;
import fr.campus.dungeoncrawler.stuff.offensivestuff.Weapon;

/**
 * Arc, spécifique au Guerrier.
 * Inflige 10 points de dégâts contre les Dragons (4 + 6), 4 contre les autres ennemis.
 */
public class Bow extends Weapon {

    public Bow() {
        super("Bow", 4);
    }

    @Override
    public int getDamageAgainst(Enemy enemy) {
        return enemy instanceof Dragon ? this.getStatBonus() + 6 : this.getStatBonus();
    }

    @Override
    public String toString() {
        return "=== " + this.getName() + " === 🏹 (Attaque : " + this.getStatBonus()
                + " | vs Dragon : " + (this.getStatBonus() + 6) + ")";
    }
}
