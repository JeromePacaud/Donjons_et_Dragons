package fr.campus.dungeoncrawler.game;

import fr.campus.dungeoncrawler.board.Board;
import fr.campus.dungeoncrawler.board.Tile;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.dice.Dice;
import fr.campus.dungeoncrawler.exceptions.OutOfBoardException;
import fr.campus.dungeoncrawler.menu.Menu;
import fr.campus.dungeoncrawler.stuff.Shield;
import fr.campus.dungeoncrawler.stuff.Spell;
import fr.campus.dungeoncrawler.stuff.Weapon;

import java.util.Random;

public class Game {

    private Character character;
    private Board board;
    private Dice dice;
    private Menu menu;
    private boolean running;
    private Random random;

    public Game() {
        this.board   = new Board();
        this.dice    = new Dice(6);
        this.menu    = new Menu();
        this.running = true;
        this.random  = new Random();
    }

    public void start() {
        while (running) {
            menu.displayMainMenu();
            int choice = menu.readInt();

            switch (choice) {
                case 1:
                    createCharacter();
                    characterMenu();
                    break;
                case 2:
                    menu.displayMessage("Au revoir !");
                    running = false;
                    break;
                default:
                    menu.displayMessage("Choix invalide.");
            }
        }
    }

    // CRÉATION DU PERSONNAGE

    private void createCharacter() {
        menu.displayTypeMenu();
        int typeChoice = menu.readInt();

        menu.displayMessage("Entrez le nom de votre personnage : ");
        String name = menu.readString();
        if (name.isBlank()) {
            menu.displayMessage("Nom invalide.");
            createCharacter();
        }

        if (typeChoice == 1) {
            character = new Warrior(name);
            character.setOffensiveStuff(new Weapon("Épée en acier", 10));
            character.setDefensiveStuff(new Shield("Bouclier en bois", 5));
        } else {
            character = new Wizard(name);
            character.setOffensiveStuff(new Spell("Boule de feu", 15));
        }

        menu.displayMessage("Personnage créé : " + character.getName() + " !");
    }

    // MENU PERSONNAGE

    private void characterMenu() {
        boolean inCharacterMenu = true;

        while (inCharacterMenu) {
            menu.displayCharacterMenu();
            int choice = menu.readInt();

            switch (choice) {
                case 1:
                    menu.displayMessage(character.toString());
                    break;
                case 2:
                    menu.displayMessage("Entrez le nouveau nom : ");
                    String newName = menu.readString();
                    if (!newName.isBlank()) {
                        character.setName(newName);
                        menu.displayMessage("Nom modifié !");
                    }
                    break;
                case 3:
                    playGame();
                    inCharacterMenu = false;
                    break;
                case 4:
                    menu.displayMessage("Au revoir !");
                    running = false;
                    inCharacterMenu = false;
                    break;
                default:
                    menu.displayMessage("Choix invalide.");
            }
        }
    }

    // BOUCLE DE JEU

    private void playGame() {
        board = new Board();
        character.resetPosition();

        menu.displayMessage("\n=== Début de la partie ! ===");
        menu.displayMessage(board.toString());
        board.display(character);

        while (!board.isFinished(character)) {
            menu.displayMessage("\nAppuyez sur Entrée pour lancer le dé...");
            menu.eatEnter();

            int roll = dice.roll();
            menu.displayMessage("Vous avez obtenu : " + roll + " avec le " + dice);

            try {
                character.move(roll, board.getSize());
            } catch (OutOfBoardException e) {
                menu.displayMessage("[Warning] " + e.getMessage());
            }

            board.display(character);
            menu.displayMessage("Position : case " + (character.getPosition() + 1) + " / " + board.getSize());

            applyTileEffect(board.getTile(character.getPosition()));

            if (character.getLifeLevel() <= 0) {
                menu.displayMessage("\n*** " + character.getName() + " est mort ! Game Over ***");
                break;
            }
        }

        if (board.isFinished(character)) {
            menu.displayMessage("\n=== " + character.getName() + " a atteint la sortie du donjon ! Victoire ! ===");
        }

        endGameMenu();
    }

    // EFFETS DES CASES

    private void applyTileEffect(Tile tile) {
        switch (tile.getType()) {
            case TRAP:
                int dmg = 2 ;
                character.setLifeLevel(character.getLifeLevel() - dmg);
                menu.displayMessage(">>> PIÈGE ! Vous perdez " + dmg + " PV. (PV : " + character.getLifeLevel() + ")");
                break;

            case CHEST:
                int heal = 4 ;
                int newHp = Math.min(character.getLifeLevel() + heal, getMaxHp());
                character.setLifeLevel(newHp);
                menu.displayMessage(">>> COFFRE ! Vous récupérez " + heal + " PV. (PV : " + character.getLifeLevel() + ")");
                break;

            case ENEMY:
                int enemyDmg = 2 ;
                character.setLifeLevel(character.getLifeLevel() - enemyDmg);
                menu.displayMessage(">>> ENNEMI ! Combat ! Vous perdez " + enemyDmg + " PV. (PV : " + character.getLifeLevel() + ")");
                break;

            case END:
                break;

            default:
                menu.displayMessage(">>> Case vide, rien ne se passe.");
        }
    }

    /** PV maximum selon la classe */
    private int getMaxHp() {
        return character instanceof Warrior ? 15 : 12;
    }

    // FIN DE PARTIE
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

    // GETTERS / SETTERS

    public Character getCharacter() { return character; }
    public void setCharacter(Character character) { this.character = character; }

    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }

    public Dice getDice() { return dice; }
    public void setDice(Dice dice) { this.dice = dice; }

    public Menu getMenu() { return menu; }
    public void setMenu(Menu menu) { this.menu = menu; }

    @Override
    public String toString() {
        return "Partie en cours avec : " + character.getName();
    }
}
