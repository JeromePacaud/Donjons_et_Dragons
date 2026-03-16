package fr.campus.dungeoncrawler.character.enemy;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Wizard;

/**
 * Class representing the EvilSpirit enemy in the Dungeon Crawler game.
 * This class extends the Enemy class and provides specific attributes and behaviors for the EvilSpirit.
 */
public class EvilSpirit extends Enemy{

    public EvilSpirit() {
        super("EvilSpirit", "Mauvais Esprit");
    }

    @Override
    public int getBaseLifeLevel() {
        return 15;
    }

    @Override
    public String getCharacterImage() {
        return "\uD83D\uDC7B";
    }

    @Override
    public int getBaseAttackLevel() {
        return 4;
    }

    @Override
    public int getMaxLifeLevel() {
        return this.getLifeLevel();
    }

    @Override
    public boolean canAttack(Character character) { return character instanceof Wizard; }

    @Override
    public String toString() {
        return "\n=== " + this.getName() + " === \n"
            + "PV : " + this.getLifeLevel() + "\n"
            + "PA : " + this.getAttackLevel() + "\n";
    }
}
