package fr.campus.dungeoncrawler.game;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.board.Board;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.dice.Dice;
import fr.campus.dungeoncrawler.menu.Menu;
import fr.campus.dungeoncrawler.stuff.OffensiveStuff;


public class Game {

    private Character character;
    private Board board;
    private Dice dice;
    private Menu menu;
    private boolean running;

    public Game() {
        this.board = new Board();
        this.dice = new Dice(6);
        this.menu = new Menu();
        this.running = true;
    }

    public void start() {
        while (running) {
            menu.displayMainMenu();
            int choice = menu.readInt();

            if (choice == 1) {
                createCharacter();
                characterMenu();
            } else if (choice == 2) {
                menu.displayMessage("Au revoir !");
                running = false;
            } else {
                menu.displayMessage("Choix invalide.");
            }
        }
    }

    private void createCharacter() {
        menu.displayTypeMenu();
        int typeChoice = menu.readInt();

        menu.displayMessage("Entrez le nom de votre personnage : ");
        String name = menu.readString();

        if (typeChoice == 1) {
            character = new Warrior(name);
            OffensiveStuff sword = new OffensiveStuff("Épée", "Offensive", 10);
            character.setOffensiveStuff(sword);
        } else {
            character = new Wizard(name);
        }

        menu.displayMessage("Personnage créé !");
    }

    private void characterMenu() {
        boolean inCharacterMenu = true;

        while (inCharacterMenu) {
            menu.displayCharacterMenu();
            int choice = menu.readInt();

            if (choice == 1) {
                menu.displayMessage(character.toString());
            } else if (choice == 2) {
                menu.displayMessage("Entrez le nouveau nom : ");
                String newName = menu.readString();
                character.setName(newName);
                menu.displayMessage("Nom modifié !");
            } else if (choice == 3) {
                playGame();
                inCharacterMenu = false;
            } else if (choice == 4) {
                menu.displayMessage("Au revoir !");
                running = false;
                inCharacterMenu = false;
            } else {
                menu.displayMessage("Choix invalide.");
            }
        }
    }

    private void playGame() {
        character.resetPosition();
        menu.displayMessage("\n=== Début de la partie ! ===");
        menu.displayMessage("Position de départ : case " + character.getPosition() + " / " + board.getTotalCases());

        while (!board.isFinished(character)) {
            menu.displayMessage("\nAppuyez sur Entrée pour lancer le dé...");
            menu.readString();

            int roll = dice.roll();
            menu.displayMessage("Vous avez obtenu : " + roll);

            character.move(roll, board.getTotalCases());
            menu.displayMessage("Position : case " + character.getPosition() + " / " + board.getTotalCases());
        }

        menu.displayMessage("\nVous avez atteint la fin du plateau !");
        endGameMenu();
    }

    private void endGameMenu() {
        menu.displayEndGameMenu();
        int choice = menu.readInt();

        if (choice == 1) {
            playGame();
        } else {
            menu.displayMessage("Au revoir !");
            running = false;
        }
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Partie en cours avec : " + (character.getName());
    }
}
