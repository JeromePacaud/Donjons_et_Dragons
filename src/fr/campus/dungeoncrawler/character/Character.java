package fr.campus.dungeoncrawler.character;

import fr.campus.dungeoncrawler.stuff.DefensiveStuff;
import fr.campus.dungeoncrawler.stuff.OffensiveStuff;

public class Character {

    private String type;
    private String name;
    private int lifePoints;
    private int damage;
    private int position;
    private OffensiveStuff offensiveStuff;
    private DefensiveStuff defensiveStuff;

    public Character(String type, String name, int lifePoints, int damage) {
        this.type = type;
        this.name = name;
        this.lifePoints = lifePoints;
        this.damage = damage;
        this.position = 1;
        this.offensiveStuff = null;
        this.defensiveStuff = null;
    }

    public void move(int steps, int totalCases) {
        this.position += steps;
        if (this.position > totalCases) {
            this.position = totalCases;
        }
    }

    public void resetPosition() { this.position = 1; }

    public String getType() { return type; }

    public String setType(String type) { return this.type = type; }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public int getLifeLevel() {
        return lifePoints;
    }

    public void setLifeLevel(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public int getAttackLevel() {
        return damage;
    }

    public void setAttackLevel(int damage) {
        this.damage = damage;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public OffensiveStuff getOffensiveStuff() {
        return offensiveStuff;
    }

    public void setOffensiveStuff(OffensiveStuff offensiveStuff) {
        this.offensiveStuff = offensiveStuff;
    }

    public DefensiveStuff getDefensiveStuff() {
        return defensiveStuff;
    }

    public void setDefensiveStuff(DefensiveStuff defensiveStuff) {
        this.defensiveStuff = defensiveStuff;
    }

    @Override
    public String toString() {
        String powerType = this.type.equalsIgnoreCase("warrior") ? "PA" : "Mana";
        String offensive = (offensiveStuff != null) ? offensiveStuff.toString() : "Aucun";
        String defensive = (defensiveStuff != null) ? defensiveStuff.toString() : "Aucun";

        return "Nom : " + name + "\n"
                + "PV : " + lifePoints + "\n"
                + powerType + " :"  + damage + "\n"
                + "Position : " + position + "\n"
                + "Équipement offensif : " + offensive + "\n"
                + "Équipement défensif : " + defensive;
    }
}
