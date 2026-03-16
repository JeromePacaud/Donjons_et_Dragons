package fr.campus.dungeoncrawler.character;

import fr.campus.dungeoncrawler.stuff.Stuff;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.Shield;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.Weapon;

/**
 * Class representing the Warrior character in the Dungeon Crawler game.
 * This class extends the Character class and provides specific attributes and behaviors for the Warrior.
 */
public class Warrior extends Character {

    public Warrior(String name) {
        super("Warrior", name, 0, 0);
        this.reset();
    }

    @Override
    public String getSpecialStatLabel() { return "PA"; }

    @Override
    public String getCharacterImage() { return "\uD83E\uDD34\uD83C\uDFFB"; }

    @Override
    public int getBaseAttackLevel() { return 5; }

    @Override
    public int getBaseLifeLevel() { return 10; }

    @Override
    public int getMaxLifeLevel() { return 15; }

    @Override
    public boolean canEquip(Stuff stuff) {
        return stuff instanceof Weapon  // inclut Bow et Sword et Mace car Bow extends Weapon
                || stuff instanceof Potion
                || stuff instanceof Shield;
    }

    @Override
    public String toString() {
        return "\n=== Warrior (Guerrier) ===\n" + super.toString();
    }
}