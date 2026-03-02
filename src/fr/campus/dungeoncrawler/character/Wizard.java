package fr.campus.dungeoncrawler.character;


public class Wizard extends Character {

    public Wizard(String name) {
        super("Wizard", name, 70, 20);
    }

    @Override
    public String toString() {
        return "=== " + super.getType() + "(Mage) ===\n" + super.toString();
    }
}
