package fr.campus.dungeoncrawler.character;

public class Wizard extends Character {

    public Wizard(String name) {
        super("Wizard", name, 6, 8);
    }

    @Override
    public String getSpecialStatLabel() {
        return "Mana";
    }

    @Override
    public String getCharacterImage() {
        return "\uD83E\uDDD9\u200D♂\uFE0F";
    }

    @Override
    public String toString() {
        return "=== Wizard (Mage) ===\n" + super.toString();
    }
}