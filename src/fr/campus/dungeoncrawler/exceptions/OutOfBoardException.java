package fr.campus.dungeoncrawler.exceptions;

/**
 * Exception levée lorsque la position d'un personnage dépasse la case finale du plateau.
 */
public class OutOfBoardException extends Exception {

    public OutOfBoardException(String message) {
        super(message);
    }
}