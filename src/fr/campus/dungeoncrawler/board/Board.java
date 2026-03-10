package fr.campus.dungeoncrawler.board;

import fr.campus.dungeoncrawler.board.tile.*;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.enemy.Dragon;
import fr.campus.dungeoncrawler.character.enemy.Goblin;
import fr.campus.dungeoncrawler.character.enemy.Sorcerer;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.ProtectionSpell;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.WoodShield;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.BigPotion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.StandardPotion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Fireball;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Lightning;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Mace;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Sword;

import java.util.Objects;
import java.util.Random;

/**
 * La classe Board représente le plateau de jeu du Dungeon Crawler.
 * Elle est composée d'un tableau de tuiles (Tile) qui peuvent être de différents types :
 * départ, arrivée, ennemis, coffres, pièges ou vides.
 * Le plateau est initialisé avec une répartition aléatoire de ces tuiles, en veillant à ce que les cases de départ et d'arrivée soient fixes.
 */
public class Board {

    private Tile[] tiles;
    private int size;
    private Random random;

    /**
     * Constructeur de la classe Board.
     * Initialise le plateau avec une taille donnée et remplit les tuiles en appelant la méthode initializeTiles().
     * @param size La taille du plateau (nombre de cases).
     */
    public Board(int size) {
        this.size = size;
        this.tiles = new Tile[this.size];
        this.random = new Random();
        initializeTiles();
    }

    /**
     * Initialise le plateau en plaçant les tuiles de départ et d'arrivée,
     * puis en répartissant aléatoirement les autres types de tuiles (ennemis, coffres, pièges) sur le reste du plateau.
     */
    private void initializeTiles() {
        for (int i = 0; i < this.tiles.length; i++) {
            this.tiles[i] = new EmptyTile();
        }

        this.tiles[0] = new StartTile();
        this.tiles[this.tiles.length - 1] = new EndTile();

        for (int i = 0; i < 4;  i++) placeRandomTiles(new EnemyTile(new Dragon()));
        for (int i = 0; i < 10; i++) placeRandomTiles(new EnemyTile(new Sorcerer()));
        for (int i = 0; i < 10; i++) placeRandomTiles(new EnemyTile(new Goblin()));
        for (int i = 0; i < 5; i++) placeRandomTiles(new ChestTile(new Mace()));
        for (int i = 0; i < 4; i++) placeRandomTiles(new ChestTile(new Sword()));
        for (int i = 0; i < 5; i++) placeRandomTiles(new ChestTile(new Lightning()));
        for (int i = 0; i < 2; i++) placeRandomTiles(new ChestTile(new Fireball()));
        for (int i = 0; i < 6; i++) placeRandomTiles(new ChestTile(new StandardPotion()));
        for (int i = 0; i < 2; i++) placeRandomTiles(new ChestTile(new BigPotion()));
        for (int i = 0; i < 2; i++) placeRandomTiles(new ChestTile(new WoodShield()));
        for (int i = 0; i < 2; i++) placeRandomTiles(new ChestTile(new ProtectionSpell()));
    }

    /**
     * Place un certain nombre de tuiles aléatoirement sur le plateau, en évitant les cases déjà occupées.
     * @param tile
     */
    private void placeRandomTiles(Tile tile) {
        boolean placed = false;

        while (!placed) {
            int index = 1 + random.nextInt(this.tiles.length - 2);
            if (Objects.equals(this.tiles[index].getType(), "Empty")) {
                this.tiles[index] = tile;
                placed = true;
            }
        }
    }

    /**
     * Appelée par Game après interact() sur une EnemyTile.
     * Si l'ennemi est mort, la case devient EmptyTile.
     * Sinon, l'ennemi fuit sur une case vide aléatoire plus loin sur le plateau.
     * @param tileIndex L'index de la case où se trouve l'ennemi.
     */
    public void moveEnemy(int tileIndex) {
        if (!(this.tiles[tileIndex] instanceof EnemyTile enemyTile)) return;

        if (enemyTile.isDefeated()) {
            this.tiles[tileIndex] = new EmptyTile();
        } else {
            int newIndex = findEmptyTile(tileIndex);
            if (newIndex != -1) {
                this.tiles[newIndex] = enemyTile;
                this.tiles[tileIndex] = new EmptyTile();
                // System.out.println("💨 " + enemyTile.getEnemy().getName() + " fuit à la case " + (newIndex + 1) + " !");
            } else {
                this.tiles[tileIndex] = new EmptyTile();
                System.out.println("💨 L'ennemi s'échappe du plateau !");
            }
        }
    }

    /**
     * Trouve une case vide aléatoire après l'index donné.
     * @param index L'index à partir duquel chercher.
     * @return L'index d'une case vide aléatoire, ou -1 si aucune case vide n'est trouvée.
     */
    private int findEmptyTile(int index) {
        int count = 0;
        for (int i = index + 1; i < this.tiles.length - 1; i++) {
            if (Objects.equals(this.tiles[i].getType(), "Empty")) {
                count++;
            }
        }

        if (count == 0) return -1;

        int target = random.nextInt(count);
        int found  = 0;
        for (int i = index + 1; i < this.tiles.length - 1; i++) {
            if (Objects.equals(this.tiles[i].getType(), "Empty")) {
                if (found == target) return i;
                found++;
            }
        }

        return -1;
    }

    /**
     * Affiche le plateau dans le terminal.
     * La case occupée par le joueur affiche son image.
     */
    public void display(Character character) {
        System.out.println();
        for (int i = 0; i < this.tiles.length; i++) {
            if (i == character.getPosition()) {
                System.out.print(character.getCharacterImage());
            } else {
                System.out.print(this.tiles[i].toString());
            }
        }
        System.out.println();
        displayLegend(character);
    }

    /**
     * Affiche la légende des symboles utilisés pour représenter les différentes tuiles et le personnage.
     * @param character Le personnage dont on veut afficher l'image dans la légende.
     */
    private void displayLegend(Character character) {
        System.out.println(
                "Légende : [" + character.getCharacterImage() + "] Joueur" + "[S] Départ  [E] Arrivée  [T] Piège  [C] Coffre  [!] Ennemi  [ ] Vide"
        );
    }

    /**
     * Vérifie si le personnage a atteint la fin du plateau (case d'arrivée).
     * @param character Le personnage dont on veut vérifier la position.
     * @return true si le personnage est sur ou au-delà de la dernière case du plateau, false sinon.
     */
    public boolean isFinished(Character character) {
        return character.getPosition() >= this.tiles.length - 1;
    }

    /**
     * Retourne la tuile à une position donnée sur le plateau.
     * Si l'index est en dehors des limites du plateau, retourne une tuile vide.
     * @param index L'index de la tuile à récupérer.
     * @return La tuile à l'index spécifié, ou une tuile vide si l'index est invalide.
     */
    public Tile getTile(int index) {
        if (index < 0 || index >= this.tiles.length) return new EmptyTile();
        return this.tiles[index];
    }

    /**
     * Remplace une case à un index donné — utilisé par BoardDAO lors du chargement.
     */
    public void setTile(int index, Tile tile) {
        if (index >= 0 && index < this.tiles.length) {
            this.tiles[index] = tile;
        }
    }

    /**
     * Vide une case (remplace par EmptyTile) — utilisé après ouverture d'un coffre.
     */
    public void clearTile(int index) {
        if (index >= 0 && index < this.tiles.length) {
            this.tiles[index] = new EmptyTile();
        }
    }

    /**
     * Retourne la taille du plateau, c'est-à-dire le nombre de cases qu'il contient.
     * @return La taille du plateau.
     */
    public int getSize() {
        return this.tiles.length;
    }

    /**
     * Redéfinit la méthode toString pour retourner une description du plateau, indiquant le nombre de cases qu'il contient.
     * @return Une description du plateau.
     */
    @Override
    public String toString() {
        return "Plateau de " + this.getSize() + " cases";
    }
}