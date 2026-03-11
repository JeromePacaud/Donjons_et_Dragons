package fr.campus.dungeoncrawler.character.enemy;

import fr.campus.dungeoncrawler.character.Character;

public class Sorcerer extends Enemy{
    public Sorcerer() {
        super("Sorcier", "Sorcier");
    }

    @Override
    public String getCharacterImage() {
        return "\uD83E\uDDD9\uD83C\uDFFF\u200D♂\uFE0F";
    }

    @Override
    public int getBaseAttackLevel() {
        return 2;
    }

    @Override
    public int getBaseLifeLevel() {
        return 9;
    }

    @Override
    public int getMaxLifeLevel() {
        return this.getLifeLevel();
    }

    @Override
    public boolean canAttack(Character character) { return true; }

    @Override
    public String toString() {
        return "\n=== Sorcier === \n"
                + "PV : " + this.getLifeLevel() + "\n"
                + "PA : " + this.getAttackLevel() + "\n";
    }
}
