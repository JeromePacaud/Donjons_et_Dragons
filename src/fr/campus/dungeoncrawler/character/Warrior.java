package fr.campus.dungeoncrawler.character;

public class Warrior extends Character {

    public Warrior(String name) {
        super("Warrior", name, 10, 5);
    }

    @Override
    public String getSpecialStatLabel() {
        return "PA";
    }

    @Override
    public String getCharacterImage() {
        return "\uD83E\uDD34\uD83C\uDFFB";
    }

    @Override
    public String toString() {
        return "=== Warrior (Guerrier) ===\n" + super.toString();
    }
}