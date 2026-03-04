package fr.campus.dungeoncrawler.character;

import fr.campus.dungeoncrawler.exceptions.OutOfBoardException;
import fr.campus.dungeoncrawler.stuff.DefensiveStuff;
import fr.campus.dungeoncrawler.stuff.OffensiveStuff;

public abstract class Character {

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
        this.position = 0;
        this.offensiveStuff = null;
        this.defensiveStuff = null;
    }

    /** Label de la stat spéciale : "PA" pour Warrior, "Mana" pour Wizard */
    public abstract String getSpecialStatLabel();

    public abstract String getCharacterImage();

    public void move(int steps, int totalCases) throws OutOfBoardException {
        this.position += steps;
        if (this.position >= totalCases) {
            this.position = totalCases - 1;
            throw new OutOfBoardException(
                    name + " dépasse la dernière case ! Position plafonnée à " + totalCases + "."
            );
        }
    }

    public void resetPosition() {
        this.position = 0;
    }

    // Getters / Setters

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLifeLevel() { return lifePoints; }
    public void setLifeLevel(int lifePoints) { this.lifePoints = lifePoints; }

    public int getAttackLevel() { return damage; }
    public void setAttackLevel(int damage) { this.damage = damage; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public OffensiveStuff getOffensiveStuff() { return offensiveStuff; }
    public void setOffensiveStuff(OffensiveStuff offensiveStuff) { this.offensiveStuff = offensiveStuff; }

    public DefensiveStuff getDefensiveStuff() { return defensiveStuff; }
    public void setDefensiveStuff(DefensiveStuff defensiveStuff) { this.defensiveStuff = defensiveStuff; }

    @Override
    public String toString() {
        String offensive = (offensiveStuff != null) ? offensiveStuff.toString() : "Aucun";
        String defensive = (defensiveStuff != null) ? defensiveStuff.toString() : "Aucun";
        return "Nom : " + name + "\n"
                + "PV  : " + lifePoints + "\n"
                + getSpecialStatLabel() + "  : " + damage + "\n"
                + "Position : " + (position + 1) + "\n"
                + "Équipement offensif : " + offensive + "\n"
                + "Équipement défensif : " + defensive;
    }
}
