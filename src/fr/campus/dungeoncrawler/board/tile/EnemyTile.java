package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.enemy.Enemy;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.menu.Menu;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;

/**
 * Représente une tuile contenant un ennemi.
 * Cette tuile permet au personnage de rencontrer un ennemi et de déclencher un combat.
 */
public class EnemyTile extends Tile {
    private Enemy enemy;
    private Menu menu;

    /**
     * Constructeur de la classe EnemyTile.
     * @param enemy L'ennemi présent sur cette tuile.
     */
    public EnemyTile(Enemy enemy) {
        super("Enemy");
        this.enemy = enemy;
        this.menu = new Menu();
    }

    /**
     * Getter pour l'ennemi présent sur cette tuile.
     * @return L'ennemi présent sur cette tuile.
     */
    public Enemy getEnemy() {
        return this.enemy;
    }

    /**
     * Setter pour l'ennemi présent sur cette tuile.
     * @param enemy L'ennemi à placer sur cette tuile.
     */
    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    /**
     * Permet au personnage d'interagir avec cette tuile, ce qui déclenche un combat avec l'ennemi présent sur cette tuile.
     * @param character Le personnage qui interagit avec cette tuile.
     */
    @Override
    public void interact(Character character) {
        if (this.enemy == null) return;

        if (!this.enemy.canAttack(character)) {
            System.out.println(">>> " + this.enemy.getName() + " vous ignore et disparaît...");
            this.enemy = null;
            return;
        }

        System.out.println("\n>>> Ennemi : \n" + this.enemy);
        System.out.println(">>> ⚔️ Combat ! " + character.getName() + " affronte " + this.enemy.getName());

        boolean combatOver = false;
        while (!combatOver) {
            this.menu.displayCombatMenu(character);
            int choice = this.menu.readInt();

            switch (choice) {
                case 1 -> {
                    character.attack(this.enemy);
                    combatOver = isCombatOver(character, combatOver);
                }
                case 2 -> {
                    if (character.getInventory().isWeaponsEmpty()) {
                        System.out.println("Inventaire d'armes vide");
                        continue;
                    } else {
                        selectWeaponAndAttack(character);
                    }
                    combatOver = isCombatOver(character, combatOver);
                }
                case 3 -> {
                    if (character.getInventory().isPotionsEmpty()) {
                        System.out.println("⚠️ Aucune potion dans l'inventaire !");
                    } else {
                        character.getInventory().usePotion(character);
                        this.enemy.attack(character);
                        if (character.isDead()) combatOver = true;
                    }
                }
                case 4 -> {
                    character.flee(this.enemy);
                    combatOver = true;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private boolean isCombatOver(Character character, boolean combatOver) {
        if (this.enemy.getLifeLevel() <= 0) {
            System.out.println("💀 " + this.enemy.getName() + " est vaincu !");
            this.enemy = null;
            combatOver = true;
        } else {
            this.enemy.attack(character);
            if (character.isDead()) combatOver = true;
        }
        return combatOver;
    }

    private void selectWeaponAndAttack(Character character) {

        this.menu.displayWeaponSelectMenu(character);

        int weaponChoice = this.menu.readInt();

        if (weaponChoice >= 1 && weaponChoice <= character.getInventory().getWeaponsSize()) {
            OffensiveStuff chosen = character.getInventory().getWeapon(weaponChoice - 1);

            if (!chosen.equals(character.getOffensiveStuff())) {
                character.equipFromInventory(chosen);
            }
        }

        character.attack(this.enemy);
    }

    public boolean isDefeated() {
        return this.enemy == null;
    }

    /**
     * Retourne l'image de la tuile, qui est l'image de l'ennemi présent sur cette tuile.
     * @return L'image de la tuile.
     */
    @Override
    public String getTileImage() {
        if (this.enemy == null) return new EmptyTile().getTileImage();
        return this.enemy.getCharacterImage();
    }
}
