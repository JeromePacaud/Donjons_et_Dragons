package fr.campus.dungeoncrawler.character.enemy;

import fr.campus.dungeoncrawler.character.Character;

public class Dragon extends Enemy{
    public Dragon() {
        super("Dragon", "Dragon");
    }

    @Override
    public String getCharacterImage() {
        return "\uD83D\uDC32";
    }

    @Override
    public int getBaseAttackLevel() {
        return 4;
    }

    @Override
    public int getBaseLifeLevel() {
        return 15;
    }

    @Override
    public int getMaxLifeLevel() {
        return this.getLifeLevel();
    }

    @Override
    public boolean canAttack(Character character) { return true; }

    @Override
    public String toString() {
        return "\n=== Dragon === \n"
                + "PV : " + this.getLifeLevel() + "\n"
                + "PA : " + this.getAttackLevel() + "\n";
    }
}
