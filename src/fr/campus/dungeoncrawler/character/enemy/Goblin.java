package fr.campus.dungeoncrawler.character.enemy;

import fr.campus.dungeoncrawler.character.Character;

/**
 * Class representing the Goblin enemy in the Dungeon Crawler game.
 * This class extends the Enemy class and provides specific attributes and behaviors for the Goblin.
 */
public class Goblin extends Enemy{
    public Goblin() {
        super("Goblin", "Goblin");
    }

    @Override
    public String getCharacterImage() {
        return "\uD83E\uDDDF";
    }

    @Override
    public int getBaseAttackLevel() {
        return 1;
    }

    @Override
    public int getBaseLifeLevel() {
        return 6;
    }

    @Override
    public int getMaxLifeLevel() {
        return this.getLifeLevel();
    }

    @Override
    public boolean canAttack(Character character) { return true; }

    @Override
    public String toString() {
        return "\n=== Goblin === \n"
            + "PV : " + this.getLifeLevel() + "\n"
            + "PA : " + this.getAttackLevel() + "\n";
    }
}
