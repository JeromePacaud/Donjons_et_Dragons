package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.enemy.*;
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

    private int gold_goblin = 3;
    private int gold_sorcerer = 5;
    private int gold_orc = 5;
    private int gold_evil_spirit = 6;
    private int gold_dragon = 10;

    /**
     * Constructeur de la classe EnemyTile.
     * @param enemy L'ennemi présent sur la tuile.
     */
    public EnemyTile(Enemy enemy) {
        super("Enemy");
        this.enemy = enemy;
        this.menu = new Menu();
    }

    public Enemy getEnemy() { return this.enemy; }
    public void setEnemy(Enemy enemy) { this.enemy = enemy; }
    public boolean isDefeated() { return this.enemy == null; }

    /**
     * Permet au personnage d'interagir avec l'ennemi présent sur la tuile.
     * Si l'ennemi ne peut pas attaquer le personnage, il disparaît.
     * Sinon, un combat est déclenché entre le personnage et l'ennemi.
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

    /**
     * Vérifie si le combat est terminé après une attaque.
     * Si l'ennemi est vaincu, le personnage reçoit une récompense en or.
     * Si le personnage est vaincu, le combat se termine également.
     */
    private boolean isCombatOver(Character character, boolean combatOver) {
        if (this.enemy.getLifeLevel() <= 0) {
            System.out.println(">>> 💀 " + this.enemy.getName() + " est vaincu !");
            character.addGold(getGoldReward(this.enemy));
            this.enemy = null;
            combatOver = true;
        } else {
            this.enemy.attack(character);
            if (character.isDead()) combatOver = true;
        }
        return combatOver;
    }

    /**
     * Retourne la récompense en or selon le type d'ennemi.
     */
    private int getGoldReward(Enemy enemy) {
        if (enemy instanceof Dragon) return gold_dragon;
        if (enemy instanceof Sorcerer) return gold_sorcerer;
        if (enemy instanceof Orc) return gold_orc;
        if (enemy instanceof EvilSpirit) return gold_evil_spirit;
        if (enemy instanceof Goblin) return gold_goblin;
        return 1;
    }

    /**
     * Permet au personnage de sélectionner une arme dans son inventaire et d'attaquer l'ennemi.
     * Si l'arme choisie n'est pas déjà équipée, elle est équipée avant l'attaque.
     */
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

    /**
     * Retourne l'image de la tuile. Si l'ennemi est vaincu, retourne l'image d'une tuile vide.
     */
    @Override
    public String getTileImage() {
        if (this.enemy == null) return new EmptyTile().getTileImage();
        return this.enemy.getCharacterImage();
    }
}