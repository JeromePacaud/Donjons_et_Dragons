package fr.campus.dungeoncrawler.character.enemy;

import fr.campus.dungeoncrawler.stuff.Stuff;

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
    public String toString() {
        return "\n=== Goblin === \n"
                + "PV : " + this.getLifeLevel() + "\n"
                + "PA : " + this.getAttackLevel() + "\n";
    }
}
