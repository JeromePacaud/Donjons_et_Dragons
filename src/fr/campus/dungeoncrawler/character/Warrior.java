package fr.campus.dungeoncrawler.character;


public class Warrior extends Character {

    public Warrior(String name) {
        super("Warrior" , name, 100, 15);
    }

    @Override
    public String toString() {
        return "=== " + super.getType() + "(Guerrier) ===\n" + super.toString();
    }
}
