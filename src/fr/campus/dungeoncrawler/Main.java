package fr.campus.dungeoncrawler;

import fr.campus.dungeoncrawler.exceptions.OutOfBoardException;
import  fr.campus.dungeoncrawler.game.Game;

/**
 * Point d'entrée du jeu. Contient la méthode main qui lance le jeu.
 */
public class Main {

    /**
     * Point d'entrée du jeu. Crée une instance de Game et lance le jeu.
     *
     * @param args les arguments de la ligne de commande (non utilisés)
     * @throws OutOfBoardException si une action tente de sortir du plateau de jeu
     */
    public static void main(String[] args) throws OutOfBoardException {
        Game game = new Game();
        game.start();
    }

}
