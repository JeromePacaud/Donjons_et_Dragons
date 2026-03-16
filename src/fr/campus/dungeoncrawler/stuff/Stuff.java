package fr.campus.dungeoncrawler.stuff;

import fr.campus.dungeoncrawler.character.Character;

public abstract class Stuff {

    private String name;
    private String type;

    public Stuff(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public abstract int getStatBonus();

    public abstract void equip(Character character);

    public abstract String getBonusLabel();

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return type + " : " + name + " (" + getBonusLabel() + " : +" + getStatBonus() + ")";
    }
}