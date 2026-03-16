package fr.campus.dungeoncrawler.stuff.offensivestuff.armory;

import fr.campus.dungeoncrawler.character.enemy.Dragon;
import fr.campus.dungeoncrawler.character.enemy.Enemy;
import fr.campus.dungeoncrawler.stuff.offensivestuff.Weapon;

/*
 * Classe représentant une arme de type arc.
 * L'arc inflige des dégâts de base, avec un bonus supplémentaire contre les dragons.
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