package fr.campus.dungeoncrawler.board;

import fr.campus.dungeoncrawler.board.tile.*;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.enemy.*;
import fr.campus.dungeoncrawler.exceptions.NoEmptyTileExceptions;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.ProtectionSpell;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.Thunderbolt;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.WoodShield;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.BigPotion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.StandardPotion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.*;

import java.util.Objects;
import java.util.Random;

/**
 * Représente le plateau de jeu du donjon.
 * Le plateau est constitué d'un tableau de tuiles, chacune pouvant être un départ, une arrivée, un ennemi, un coffre, etc.
 * Le plateau gère l'initialisation des tuiles, le déplacement des ennemis, l'affichage du plateau et la vérification de la fin du jeu.
 */
public class Board {

    private Tile[] tiles;
    private int size;
    private Random random;

    /**
     * Constructeur de la classe Board.
     * Initialise le plateau avec une taille donnée, crée un tableau de tuiles et remplit le plateau avec des tuiles aléatoires.
     *
     * @param size La taille du plateau (nombre de tuiles).
     */
    public Board(int size) {
        this.size = size;
        this.tiles = new Tile[this.size];
        this.random = new Random();
        initializeTiles();
    }

    public Tile getTile(int index) {
        if (index < 0 || index >= this.tiles.length) return new EmptyTile();
        return this.tiles[index];
    }

    public void setTile(int index, Tile tile) {
        if (index >= 0 && index < this.tiles.length) this.tiles[index] = tile;
    }

    public void clearTile(int index) {
        if (index >= 0 && index < this.tiles.length) this.tiles[index] = new EmptyTile();
    }

    public int getSize() { return this.tiles.length; }

    /**
     * Initialise les tuiles du plateau.
     * Remplit le plateau avec des tuiles de départ, d'arrivée, d'ennemis, de coffres, de marchands, d'auberges, etc.
     * Les tuiles sont placées de manière aléatoire sur le plateau, en s'assurant que les tuiles de départ et d'arrivée sont aux extrémités.
     * Si le plateau est trop petit pour contenir toutes les tuiles prévues, une exception est levée et un message d'avertissement est affiché.
     */
    private void initializeTiles() {
        for (int i = 0; i < this.tiles.length; i++) {
            this.tiles[i] = new EmptyTile();
        }

        this.tiles[0] = new StartTile();
        this.tiles[this.tiles.length - 1] = new EndTile();

        try {
            // Ennemis
            for (int i = 0; i < 4; i++)  placeRandomTiles(new EnemyTile(new Dragon()));
            for (int i = 0; i < 10; i++) placeRandomTiles(new EnemyTile(new Sorcerer()));
            for (int i = 0; i < 10; i++) placeRandomTiles(new EnemyTile(new Goblin()));
            for (int i = 0; i < 4; i++)  placeRandomTiles(new EnemyTile(new Orc()));
            for (int i = 0; i < 4; i++)  placeRandomTiles(new EnemyTile(new EvilSpirit()));

            // Armes Warrior
            for (int i = 0; i < 5; i++)  placeRandomTiles(new ChestTile(new Mace()));
            for (int i = 0; i < 4; i++)  placeRandomTiles(new ChestTile(new Sword()));
            for (int i = 0; i < 3; i++)  placeRandomTiles(new ChestTile(new Bow()));

            // Sorts Wizard
            for (int i = 0; i < 5; i++)  placeRandomTiles(new ChestTile(new Lightning()));
            for (int i = 0; i < 2; i++)  placeRandomTiles(new ChestTile(new Fireball()));
            for (int i = 0; i < 3; i++)  placeRandomTiles(new ChestTile(new Invisibility()));

            // Potions
            for (int i = 0; i < 6; i++)  placeRandomTiles(new ChestTile(new StandardPotion()));
            for (int i = 0; i < 2; i++)  placeRandomTiles(new ChestTile(new BigPotion()));
            for (int i = 0; i < 3; i++)  placeRandomTiles(new ChestTile(new Thunderbolt()));

            // Défensif
            for (int i = 0; i < 2; i++)  placeRandomTiles(new ChestTile(new WoodShield()));
            for (int i = 0; i < 2; i++)  placeRandomTiles(new ChestTile(new ProtectionSpell()));

            // Marchands et auberges
            for (int i = 0; i < 3; i++)  placeRandomTiles(new MerchantTile());
            for (int i = 0; i < 3; i++)  placeRandomTiles(new InnTile());

        } catch (NoEmptyTileExceptions e) {
            System.err.println("⚠️ " + e.getMessage());
            System.err.println("Le plateau sera joué avec moins d'éléments que prévu.");
        }
    }

    /**
     * Place une tuile aléatoirement sur le plateau.
     * La méthode tente de placer la tuile sur une case vide du plateau. Si aucune case vide n'est trouvée après un nombre maximum de tentatives, une exception est levée.
     *
     * @param tile La tuile à placer sur le plateau.
     * @throws NoEmptyTileExceptions Si aucune case vide n'est disponible pour placer la tuile après un nombre maximum de tentatives.
     */
    private void placeRandomTiles(Tile tile) throws NoEmptyTileExceptions {
        boolean placed = false;
        int attempts = 0;
        int maxAttempts = this.tiles.length;

        while (!placed) {
            if (attempts == maxAttempts) {
                throw new NoEmptyTileExceptions(
                        "Impossible de placer la tuile " + tile.getType()
                                + " : aucune case vide disponible après " + attempts + " tentatives."
                );
            }
            int index = 1 + random.nextInt(this.tiles.length - 2);
            if (Objects.equals(this.tiles[index].getType(), "Empty")) {
                this.tiles[index] = tile;
                placed = true;
            }
            attempts++;
        }
    }

    /**
     * Tente de déplacer un ennemi d'une tuile donnée vers une tuile vide suivante.
     * Si l'ennemi est défait, la tuile est simplement vidée. Si l'ennemi n'est pas défait, il tente de se déplacer vers une tuile vide suivante. Si aucune tuile vide n'est disponible, l'ennemi s'échappe du plateau.
     *
     * @param tileIndex L'index de la tuile contenant l'ennemi à déplacer.
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
            } else {
                this.tiles[tileIndex] = new EmptyTile();
                System.out.println("💨 L'ennemi s'échappe du plateau !");
            }
        }
    }

    /**
     * Trouve l'index d'une tuile vide suivante à partir d'un index donné.
     * La méthode compte le nombre de tuiles vides disponibles après l'index donné, puis sélectionne aléatoirement l'une de ces tuiles vides. Si aucune tuile vide n'est disponible, la méthode retourne -1.
     *
     * @param index L'index à partir duquel chercher une tuile vide.
     * @return L'index d'une tuile vide suivante, ou -1 si aucune tuile vide n'est disponible.
     */
    private int findEmptyTile(int index) {
        int count = 0;
        for (int i = index + 1; i < this.tiles.length - 1; i++) {
            if (Objects.equals(this.tiles[i].getType(), "Empty")) count++;
        }
        if (count == 0) return -1;

        int target = random.nextInt(count);
        int found = 0;
        for (int i = index + 1; i < this.tiles.length - 1; i++) {
            if (Objects.equals(this.tiles[i].getType(), "Empty")) {
                if (found == target) return i;
                found++;
            }
        }
        return -1;
    }

    /**
     * Affiche le plateau de jeu dans la console, en indiquant la position du personnage et les différentes tuiles présentes sur le plateau.
     * La méthode affiche également une légende pour expliquer les symboles utilisés pour représenter les différentes tuiles et le personnage.
     *
     * @param character Le personnage dont la position doit être affichée sur le plateau.
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
     * Affiche la légende des symboles utilisés pour représenter les différentes tuiles et le personnage sur le plateau.
     * La légende indique les symboles associés à chaque type de tuile (départ, arrivée, coffre, ennemi, marchand, auberge, vide) ainsi que le symbole du personnage.
     *
     * @param character Le personnage dont le symbole doit être inclus dans la légende.
     */
    private void displayLegend(Character character) {
        System.out.println(
                "Légende : [" + character.getCharacterImage() + "] Joueur [S] Départ  [E] Arrivée  [C] Coffre  [!] Ennemi  [🧙] Marchand  [🏠] Auberge  [ ] Vide"
        );
    }

    /**
     * Vérifie si le personnage a atteint la fin du plateau, c'est-à-dire s'il est sur la tuile d'arrivée ou au-delà.
     *
     * @param character Le personnage dont la position doit être vérifiée.
     * @return true si le personnage a atteint ou dépassé la tuile d'arrivée, false sinon.
     */
    public boolean isFinished(Character character) {
        return character.getPosition() >= this.tiles.length - 1;
    }

    @Override
    public String toString() { return "Plateau de " + this.getSize() + " cases"; }
}