package fr.campus.dungeoncrawler.character.enemy;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;

public class Orc extends Enemy{

    public Orc() {
        super("Orc", "Orc");
    }

    @Override
    public int getBaseLifeLevel() {
        return 10;
    }

    @Override
    public String getCharacterImage() {
        return "\uD83E\uDDCC";
    }

    @Override
    public int getBaseAttackLevel() {
        return 6;
    }

    @Override
    public int getMaxLifeLevel() {
        return this.getLifeLevel();
    }

    @Override
    public boolean canAttack(Character character) { return character instanceof Warrior; }

    @Override
    public String toString() {
        return "\n=== Orc === \n"
            + "PV : " + this.getLifeLevel() + "\n"
            + "PA : " + this.getAttackLevel() + "\n";
    }
}
