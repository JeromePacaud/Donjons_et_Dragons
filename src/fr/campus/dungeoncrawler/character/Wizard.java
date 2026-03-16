package fr.campus.dungeoncrawler.character;

import fr.campus.dungeoncrawler.stuff.Stuff;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.ProtectionSpell;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.Spell;

/**
 * Class representing the Wizard character in the Dungeon Crawler game.
 * This class extends the Character class and provides specific attributes and behaviors for the Wizard.
 */
public class Wizard extends Character {

    public Wizard(String name) {
        super("Wizard", name, 0, 0);
        this.reset();
    }

    @Override
    public String getSpecialStatLabel() { return "Mana"; }

    @Override
    public String getCharacterImage() { return "\uD83E\uDDD9\u200D♂\uFE0F"; }

    @Override
    public int getBaseAttackLevel() { return 8; }

    @Override
    public int getBaseLifeLevel() { return 6; }

    @Override
    public int getMaxLifeLevel() { return 12; }

    @Override
    public boolean canEquip(Stuff stuff) {
        return stuff instanceof Spell  // inclut Invisibility car Invisibility extends Spell
                || stuff instanceof ProtectionSpell
                || stuff instanceof Potion;
    }

    @Override
    public String toString() {
        return "\n=== Wizard (Mage) ===\n" + super.toString();
    }
}