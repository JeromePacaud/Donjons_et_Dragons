package fr.campus.dungeoncrawler.character.enemy;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.stuff.Stuff;

/**
 * Classe abstraite représentant un ennemi dans le jeu.
 * Les ennemis ont des caractéristiques de base et peuvent attaquer les personnages.
 * Ils ne peuvent pas équiper d'objets et ont une étiquette spéciale pour leur statistique d'attaque.
 */
public abstract class Enemy extends Character {

    public Enemy(String type, String name) {
        super(type, name, 0, 0);
        this.reset();
    }

    /**
     * Attaque un personnage en infligeant des dégâts basés sur la différence entre le niveau d'attaque de l'ennemi
     * et le niveau de défense du personnage. Les dégâts infligés ne peuvent pas être négatifs.
     * Affiche un message indiquant les dégâts infligés et les points de vie restants du personnage.
     *
     * @param character Le personnage à attaquer.
     */
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
