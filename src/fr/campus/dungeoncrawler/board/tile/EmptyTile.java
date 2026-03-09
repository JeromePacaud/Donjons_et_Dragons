package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.Character;

/**
 * Représente une case vide sur le plateau de jeu.
 * Cette classe hérite de Tile et implémente les méthodes spécifiques à une case vide.
 */
public class EmptyTile extends Tile {

    /**
     * Constructeur de la classe EmptyTile.
     * Initialise le nom de la case à "Empty".
     */
    public EmptyTile() {
        super("Empty");
    }

    /**
     * Retourne l'image représentant une case vide.
     *
     * @return une chaîne de caractères contenant l'emoji d'une case vide.
     */
    @Override
    public String getTileImage() {
        return "\uD83D\uDFEB";
    }

    /**
     * Interagit avec un personnage qui se trouve sur cette case.
     * Pour une case vide, aucune interaction n'a lieu, et un message est affiché.
     *
     * @param character le personnage qui interagit avec la case.
     */
    @Override
    public void interact(Character character) {
        System.out.println("\n>>> Case vide, rien ne se passe.\n");
    }
}
