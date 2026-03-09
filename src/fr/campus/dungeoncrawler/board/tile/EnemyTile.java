package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.enemy.Enemy;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.menu.Menu;

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
        if (this.enemy == null) {
            // TODO : Utiliser EmptyTile.interact() ??
            //System.out.println("\n>>> Case vide, rien ne se passe.");
            return;
        }

        System.out.println("\n>>> Ennemi : \n" + this.enemy.toString());
        System.out.println(">>> ⚔️ Combat ! "  + character.getName() + " affronte " + this.enemy.getName());

        boolean combatOver = false;
        while (!combatOver) {
            this.menu.displayCombatMenu(character);
            int choice = this.menu.readInt();

            switch (choice) {
                case 1 -> {
                    character.attack(this.enemy);
                    if (this.enemy.getLifeLevel() <= 0) {
                        System.out.println("\uD83D\uDC80 " + enemy.getName() + " est vaincu !");
                        this.enemy = null;
                    } else {
                        this.enemy.attack(character);
                    }
                    combatOver = true;
                }
                case 2 -> {
                    if (character.getInventory().isEmpty()) {
                        System.out.println("⚠️ Aucune potion dans l'inventaire !");
                    } else {
                        character.getInventory().usePotion(character);
                        this.enemy.attack(character);
                        combatOver = true;
                    }
                }
                case 3 -> {
                    character.flee(this.enemy);
                    combatOver = true;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

   /*
    private void attack(Character character) {
        int characterDamage = character.getAttackLevel();
        this.enemy.setLifeLevel(enemy.getLifeLevel() - characterDamage);
        System.out.println(
            "\n>>> " + character.getName() + " inflige "
            + characterDamage + " points de dégâts à " + this.enemy.getName()
            + "(" + enemy.getName() + " PV : " + Math.max(enemy.getLifeLevel(), 0) + ")"
        );

        if (this.enemy.getLifeLevel() <= 0) {
            System.out.println("\uD83D\uDC80 " + enemy.getName() + " est vaincu !");
            this.enemy = null;
        } else {
            enemyAttack(character);
        }
    }

    private void enemyAttack(Character character) {
        int enemyDamage = Math.max(this.enemy.getAttackLevel() - character.getDefenseLevel(), 0);
        character.setLifeLevel(character.getLifeLevel() - enemyDamage);
        System.out.println("    " + this.enemy.getName() + " riposte et inflige " + enemyDamage + " dégâts. "
            + "(" + character.getName() + " PV : " + character.getLifeLevel() + ")");
        System.out.println("    \uD83D\uDCA8 " + this.enemy.getName() + " prend la fuite !");
    }*/

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
