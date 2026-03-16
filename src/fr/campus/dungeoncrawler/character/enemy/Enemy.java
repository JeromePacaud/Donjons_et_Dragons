package fr.campus.dungeoncrawler.character.enemy;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.stuff.Stuff;


public abstract class Enemy extends Character {

    public Enemy(String type, String name) {
        super(type, name, 0, 0);
        this.reset();
    }

    public void attack(Character character) {
        int enemyDamage = Math.max(this.getAttackLevel() - character.getDefenseLevel(), 0);
        character.setLifeLevel(character.getLifeLevel() - enemyDamage);
        System.out.println(">>> " + this.getName() + " riposte et inflige " + enemyDamage + " dégâts. "
            + "(" + character.getName() + " PV : " + character.getLifeLevel() + ")");
    }

    public abstract boolean canAttack(Character character);

    @Override
    public boolean canEquip(Stuff stuff) {
        return false;
    }

    @Override
    public String getSpecialStatLabel() {
        return "Attaque !";
    }
}
