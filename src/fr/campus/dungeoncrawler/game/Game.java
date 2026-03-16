package fr.campus.dungeoncrawler.game;

import fr.campus.dungeoncrawler.board.Board;
import fr.campus.dungeoncrawler.board.tile.*;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.db.BoardTable;
import fr.campus.dungeoncrawler.db.CharacterTable;
import fr.campus.dungeoncrawler.db.InventoryTable;
import fr.campus.dungeoncrawler.db.SQLDatabaseConnection;
import fr.campus.dungeoncrawler.dice.SixSidedDice;
import fr.campus.dungeoncrawler.exceptions.OutOfBoardException;
import fr.campus.dungeoncrawler.menu.Menu;

public class Game {

    private SQLDatabaseConnection db;
    private CharacterTable playerTable;
    private BoardTable boardTable;
    private InventoryTable inventoryTable;
    private Character character;
    private Board board;
    private SixSidedDice dice;
    private Menu menu;
    private boolean running;

    public Game() {
        this.board = new Board(90);
        this.dice = new SixSidedDice();
        this.menu = new Menu();
        this.running = true;
        this.db = new SQLDatabaseConnection();
        this.db.getConnection();
        this.db.createDatabase();
        this.db.useDatabase("dungeoncrawler");
        this.playerTable = new CharacterTable(db.getActiveConnection());
        this.boardTable = new BoardTable(db.getActiveConnection());
        this.inventoryTable = new InventoryTable(db.getActiveConnection());
        this.playerTable.createCharacterTable();
        this.playerTable.createEquipmentTable();
        this.boardTable.createBoardTable();
        this.boardTable.createBoardTilesTable();
        this.inventoryTable.createInventoryTable();
    }

    public void start() {
        while (running) {
            this.menu.displayMainMenu();
            int choice = this.menu.readInt();

            switch (choice) {
                case 1:
                    createCharacter();
                    characterMenu();
                    break;
                case 2:
                    if (this.playerTable.isEmpty()) {
                        this.menu.displayMessage("\nLa table est vide.");
                    } else {
                        this.playerTable.fetchAllCharacters();
                        this.menu.displayMessage("Entrez l'id du personnage à charger : ");
                        int loadId = this.menu.readInt();
                        Character loaded = this.playerTable.loadCharacter(loadId);
                        if (loaded != null) {
                            this.character = loaded;
                            this.inventoryTable.loadInventory(character);
                            this.menu.displayMessage("Personnage chargé : " + this.character.getName() + " !");
                            characterMenu();
                        } else {
                            this.menu.displayMessage("Aucun personnage trouvé avec l'id " + loadId + ".");
                        }
                    }
                    break;
                case 3:
                    this.playerTable.fetchAllCharacters();
                    this.menu.displayMessage("Entrez l'id du personnage à supprimer : ");
                    int deleteId = this.menu.readInt();
                    Character toDelete = this.playerTable.loadCharacter(deleteId);
                    if (toDelete != null) {
                        this.menu.displayMessage(
                                "Êtes-vous sûr de vouloir supprimer " + toDelete.getName() + " ? (1 = Oui / 2 = Non)"
                        );
                        int confirm = this.menu.readInt();
                        if (confirm == 1) this.playerTable.deleteCharacter(toDelete);
                    } else {
                        this.menu.displayMessage("Aucun personnage trouvé avec l'id " + deleteId + ".");
                    }
                    break;
                case 4:
                    this.menu.displayMessage("Au revoir !");
                    this.running = false;
                    this.db.closeConnection();
                    break;
                default:
                    this.menu.displayMessage("Choix invalide.");
            }
        }
    }

    private void createCharacter() {
        this.menu.displayTypeMenu();
        int typeChoice = this.menu.readInt();

        this.menu.displayMessage("Entrez le nom de votre personnage : ");
        String name = this.menu.readString();
        if (name.isBlank()) {
            this.menu.displayMessage("Nom invalide.");
            createCharacter();
        }

        this.character = (typeChoice == 1) ? new Warrior(name) : new Wizard(name);
        this.playerTable.insertCharacter(this.character);
        this.menu.displayMessage("Personnage créé : " + this.character.getName() + " !");
    }

    private void characterMenu() {
        boolean inCharacterMenu = true;

        while (inCharacterMenu) {
            this.menu.displayCharacterMenu();
            int choice = this.menu.readInt();

            switch (choice) {
                case 1:
                    this.menu.displayMessage(this.character.toString());
                    break;
                case 2:
                    this.menu.displayMessage("Entrez le nouveau nom : ");
                    String newName = this.menu.readString();
                    if (!newName.isBlank()) {
                        this.character.setName(newName);
                        this.playerTable.updateCharacter(this.character);
                        this.menu.displayMessage("Nom modifié !");
                    }
                    break;
                case 3:
                    playGame();
                    inCharacterMenu = false;
                    break;
                case 4:
                    this.menu.displayMessage("Au revoir !");
                    this.running = false;
                    inCharacterMenu = false;
                    this.db.closeConnection();
                    break;
                default:
                    this.menu.displayMessage("Choix invalide.");
            }
        }
    }

    private void playGame() {
        Board savedBoard = this.boardTable.loadBoard(this.character);

        if (savedBoard != null) {
            this.menu.displayMessage("Plateau chargé pour " + this.character.getName() + " !");
            this.board = savedBoard;
        } else {
            this.board = new Board(90);
            this.character.reset();
        }

        this.menu.displayMessage("\n=== Début de la partie ! ===");
        this.menu.displayMessage(this.board.toString());
        this.board.display(this.character);

        while (!this.board.isFinished(this.character)) {
            this.menu.displayInGameMenu();
            int choice = this.menu.readInt();

            if (choice == 2) {
                saveAndQuit();
                return;
            }

            if (choice == 3) {
                this.menu.displayMessage("Êtes-vous sûr de vouloir regénérer le plateau ? (1 = Oui / 2 = Non)");
                if (this.menu.readInt() == 1) {
                    this.boardTable.deleteBoardByCharacter(this.character);
                    this.character.reset();
                    this.playGame();
                    return;
                }
            }

            int roll = this.dice.roll();
            this.menu.displayMessage("Vous avez obtenu : " + roll + " avec le " + this.dice);

            try {
                this.character.move(roll, this.board.getSize());
            } catch (OutOfBoardException e) {
                this.menu.displayMessage("[Warning] " + e.getMessage());
            }

            this.displayCharacterStats();
            this.applyTileEffect(this.board.getTile(this.character.getPosition()));
            this.board.display(this.character);
            this.menu.displayMessage("Position : case " + (this.character.getPosition() + 1) + " / " + this.board.getSize());

            if (this.character.isDead()) {
                this.menu.displayMessage("\n*** " + this.character.getName() + " est mort ! Game Over ***");
                this.boardTable.deleteBoardByCharacter(this.character);
                break;
            }
        }

        if (this.board.isFinished(this.character)) {
            this.boardTable.deleteBoardByCharacter(this.character);
        }

        endGameMenu();
    }

    private void saveAndQuit() {
        this.menu.displayMessage("Sauvegarde en cours...");
        this.playerTable.updateCharacter(character);
        this.playerTable.saveEquipments(character);
        this.inventoryTable.saveInventory(character);
        this.boardTable.saveBoard(board, character);
        this.menu.displayMessage("Partie sauvegardée ! À bientôt !");
        this.running = false;
        this.db.closeConnection();
    }

    private void applyTileEffect(Tile tile) {
        tile.interact(this.character);

        if (tile instanceof EnemyTile enemyTile && enemyTile.isDefeated()) {
            this.board.clearTile(this.character.getPosition());
        }

        if (tile instanceof ChestTile chestTile && chestTile.isOpened()) {
            this.board.clearTile(this.character.getPosition());
        }
    }

    private void displayCharacterStats() {
        System.out.println(this.character.toString());
    }

    /**
     * Menu de fin de partie.
     * Si le joueur recommence, le plateau est régénéré et les marchands/auberges
     * sont replacés aléatoirement — l'or est remis à 0 via character.reset().
     */
    private void endGameMenu() {
        menu.displayEndGameMenu();
        int choice = menu.readInt();

        if (choice == 1) {
            // Nouvelle partie — reset complet (or remis à 0)
            this.boardTable.deleteBoardByCharacter(this.character);
            this.character.reset();
            this.board = new Board(90); // marchands replacés aléatoirement
            playGame();
        } else {
            menu.displayMessage("Au revoir !");
            running = false;
            db.closeConnection();
        }
    }

    public Character getCharacter()             { return this.character; }
    public void setCharacter(Character c)       { this.character = c; }
    public Board getBoard()                     { return this.board; }
    public void setBoard(Board b)               { this.board = b; }
    public SixSidedDice getDice()               { return this.dice; }
    public void setDice(SixSidedDice d)         { this.dice = d; }
    public Menu getMenu()                       { return this.menu; }
    public void setMenu(Menu m)                 { this.menu = m; }

    @Override
    public String toString() {
        return "Partie en cours avec : " + this.character.getName();
    }
}