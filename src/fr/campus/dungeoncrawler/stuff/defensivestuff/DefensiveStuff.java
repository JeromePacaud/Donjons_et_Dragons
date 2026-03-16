package fr.campus.dungeoncrawler.stuff.defensivestuff;

import fr.campus.dungeoncrawler.stuff.Stuff;
import fr.campus.dungeoncrawler.character.Character;

/**
 *  Classe abstraite représentant les objets défensifs dans le jeu.
 * Ces objets augmentent la défense du personnage lorsqu'ils sont équipés.
 */
public abstract class DefensiveStuff extends Stuff {

    private int defenseAmount;

    public DefensiveStuff(String name, String type, int defenseAmount) {
        super(name, type);
        this.defenseAmount = defenseAmount;
    }

    public int getDefenseLevel() { return defenseAmount; }

    public void setDefenseLevel(int defenseAmount) { this.defenseAmount = defenseAmount; }

    @Override
    public int getStatBonus() { return defenseAmount; }

    @Override
    public String getBonusLabel() { return "Défense"; }

    @Override
    public void equip(Character character) {
        character.setDefensiveStuff(this);
        character.setLifeLevel(character.getLifeLevel() + getStatBonus());
    }
}
