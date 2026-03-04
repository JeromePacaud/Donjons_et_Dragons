package fr.campus.dungeoncrawler.board;

import fr.campus.dungeoncrawler.board.Tile.Type;
import fr.campus.dungeoncrawler.character.Character;

import java.util.Random;

public class Board {

    private Tile[] tiles;
    private Random random;

    public Board() {
        this(64);
    }

    public Board(int size) {
        this.tiles = new Tile[size];
        this.random = new Random();
        initializeTiles();
    }

    private void initializeTiles() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(Type.EMPTY);
        }

        tiles[0] = new Tile(Type.START);
        tiles[tiles.length - 1] = new Tile(Type.END);

        placeRandomTiles(Type.TRAP,  6);
        placeRandomTiles(Type.CHEST, 5);
        placeRandomTiles(Type.ENEMY, 5);
    }

    private void placeRandomTiles(Type type, int count) {
        int placed = 0;
        while (placed < count) {
            int index = 1 + random.nextInt(tiles.length - 2);
            if (tiles[index].getType() == Type.EMPTY) {
                tiles[index] = new Tile(type);
                placed++;
            }
        }
    }

    /**
     * Affiche le plateau dans le terminal.
     * La case occupée par le joueur affiche .
     */
    public void display(Character character) {
        System.out.println();
        for (int i = 0; i < tiles.length; i++) {
            if (i == character.getPosition()) {
                System.out.print(character.getCharacterImage());
            } else {
                System.out.print(tiles[i].toString());
            }
        }
        System.out.println();
        displayLegend(character);
    }

    private void displayLegend(Character character) {
        System.out.println("Légende : [" + character.getCharacterImage() + "] Joueur  [S] Départ  [E] Arrivée  [T] Piège  [C] Coffre  [!] Ennemi  [ ] Vide");
    }

    public boolean isFinished(Character character) {
        return character.getPosition() >= tiles.length - 1;
    }

    public Tile getTile(int index) {
        if (index < 0 || index >= tiles.length) return new Tile(Type.EMPTY);
        return tiles[index];
    }

    public int getSize() {
        return tiles.length;
    }

    public int getTotalCases() {
        return tiles.length;
    }

    @Override
    public String toString() {
        return "Plateau de " + tiles.length + " cases";
    }
}
