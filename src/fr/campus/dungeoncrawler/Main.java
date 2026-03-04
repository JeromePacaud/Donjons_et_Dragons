package fr.campus.dungeoncrawler;

import fr.campus.dungeoncrawler.exceptions.OutOfBoardException;
import  fr.campus.dungeoncrawler.game.Game;

public class Main {

    public static void main(String[] args) throws OutOfBoardException {
        Game game = new Game();
        game.start();
    }

}
