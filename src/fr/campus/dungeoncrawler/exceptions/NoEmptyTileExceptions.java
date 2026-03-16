package fr.campus.dungeoncrawler.exceptions;

/**
 * Exception levée lorsqu'il n'y a pas de tile vide disponible pour placer un objet ou un personnage dans le jeu.
 */
public class NoEmptyTileExceptions extends Exception {

    public NoEmptyTileExceptions(String message) {
        super(message);
    }
}
