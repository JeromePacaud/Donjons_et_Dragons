package fr.campus.dungeoncrawler.board;

import fr.campus.dungeoncrawler.board.tile.*;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.enemy.*;
import fr.campus.dungeoncrawler.exceptions.NoEmptyTileExceptions;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.ProtectionSpell;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.WoodShield;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.BigPotion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.StandardPotion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.ThunderBolt;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.*;

import java.util.Objects;
import java.util.Random;

public class Board {

    private Tile[] tiles;
    private int size;
    private Random random;

    public Board(int size) {
        this.size = size;
        this.tiles = new Tile[this.size];
        this.random = new Random();
        initializeTiles();
    }

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
            for (int i = 0; i < 3; i++)  placeRandomTiles(new ChestTile(new ThunderBolt()));

            // Défensif
            for (int i = 0; i < 2; i++)  placeRandomTiles(new ChestTile(new WoodShield()));
            for (int i = 0; i < 2; i++)  placeRandomTiles(new ChestTile(new ProtectionSpell()));

        } catch (NoEmptyTileExceptions e) {
            System.err.println("⚠️ " + e.getMessage());
            System.err.println("Le plateau sera joué avec moins d'éléments que prévu.");
        }
    }

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

    private void displayLegend(Character character) {
        System.out.println(
                "Légende : [" + character.getCharacterImage() + "] Joueur [S] Départ  [E] Arrivée  [C] Coffre  [!] Ennemi  [ ] Vide"
        );
    }

    public boolean isFinished(Character character) {
        return character.getPosition() >= this.tiles.length - 1;
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

    @Override
    public String toString() { return "Plateau de " + this.getSize() + " cases"; }
}