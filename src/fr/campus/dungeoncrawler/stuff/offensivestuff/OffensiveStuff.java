package fr.campus.dungeoncrawler.stuff.offensivestuff;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.enemy.Enemy;
import fr.campus.dungeoncrawler.stuff.Stuff;

/**
 * Classe abstraite représentant les objets offensifs (armes, sorts, etc.)
 * Ces objets augmentent la capacité d'attaque du personnage.
 */
public abstract class OffensiveStuff extends Stuff {

    private int damage;

    public OffensiveStuff(String name, String type, int damage) {
        super(name, type);
        this.damage = damage;
    }

    public int getAttackLevel()          { return damage; }
    public void setAttackLevel(int dmg)  { this.damage = dmg; }
    public int getDamageAgainst(Enemy enemy) {
        return this.getStatBonus();
    }

    @Override
    public int getStatBonus() { return damage; }

    @Override
    public String getBonusLabel() { return "Attaque"; }

    @Override
    public void equip(Character character) {
        character.setOffensiveStuff(this);
        character.setAttackLevel(character.getAttackLevel() + getStatBonus());
    }
}