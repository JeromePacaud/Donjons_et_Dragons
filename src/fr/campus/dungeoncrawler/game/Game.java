package fr.campus.dungeoncrawler.game;

import fr.campus.dungeoncrawler.board.Board;
import fr.campus.dungeoncrawler.board.tile.ChestTile;
import fr.campus.dungeoncrawler.board.tile.EnemyTile;
import fr.campus.dungeoncrawler.board.tile.Tile;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.db.BoardTable;
import fr.campus.dungeoncrawler.db.CharacterTable;
import fr.campus.dungeoncrawler.db.InventoryTable;
import fr.campus.dungeoncrawler.db.SQLDatabaseConnection;
import fr.campus.dungeoncrawler.dice.SixSidedDice;
import fr.campus.dungeoncrawler.dice.TwentySidedDice;
import fr.campus.dungeoncrawler.exceptions.OutOfBoardException;
import fr.campus.dungeoncrawler.menu.Menu;

import java.util.Random;

/**
 * La classe Game est la classe centrale du projet Dungeon Crawler. Elle gère le déroulement du jeu,
 * les interactions entre les différentes classes (Character, Board, Dice, Menu) et la connexion à la base de données.
 * Elle contient la boucle principale du jeu et les méthodes pour créer un personnage,
 * jouer une partie, appliquer les effets des cases et gérer la fin de partie.
 */
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

    /**
     * Constructeur de la classe Game. Initialise les composants du jeu : le plateau, le dé, le menu,
     * la connexion à la base de données et les variables de contrôle.
     */
    public Game() {
        this.board = new Board(74);
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

    /**
     * Démarre le jeu en affichant le menu principal et en gérant les choix de l'utilisateur.
     * Permet de créer un personnage, charger un personnage existant, supprimer un personnage ou quitter le jeu.
     * En fonction du choix de l'utilisateur, appelle les méthodes appropriées pour gérer la suite du jeu.
     */
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
                        if (confirm == 1) {
                            this.playerTable.deleteCharacter(toDelete);
                        }
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

    /**
     * Permet à l'utilisateur de créer un personnage en choisissant une classe (guerrier ou magicien) et en entrant un nom.
     * Le personnage est ensuite enregistré dans la base de données et un message de confirmation est affiché.
     * Si le nom entré est invalide (vide ou composé uniquement d'espaces), un message d'erreur est affiché et
     * la méthode est rappelée pour permettre à l'utilisateur de réessayer.
     */
    private void createCharacter() {
        this.menu.displayTypeMenu();
        int typeChoice = this.menu.readInt();

        this.menu.displayMessage("Entrez le nom de votre personnage : ");
        String name = this.menu.readString();
        if (name.isBlank()) {
            this.menu.displayMessage("Nom invalide.");
            createCharacter();
        }

        if (typeChoice == 1) {
            this.character = new Warrior(name);
        } else {
            this.character = new Wizard(name);
        }

        this.playerTable.insertCharacter(this.character);
        this.menu.displayMessage("Personnage créé : " + this.character.getName() + " !");
    }

    /**
     * Affiche le menu de gestion du personnage, permettant à l'utilisateur de voir les statistiques du personnage,
     * modifier son nom, commencer une partie ou revenir au menu principal. En fonction du choix de l'utilisateur,
     * appelle les méthodes appropriées pour gérer la suite du jeu.
     */
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

    /**
     * Démarre une partie en initialisant le plateau de jeu, en réinitialisant la position du personnage et en affichant le plateau.
     * La boucle principale du jeu continue tant que le personnage n'a pas atteint la fin du plateau ou n'est pas mort.
     * À chaque tour, le joueur lance le dé pour avancer, les effets de la case sur laquelle il atterrit sont appliqués,
     * les ennemis se déplacent, et le plateau est réaffiché. Si le personnage meurt, un message de fin de partie est affiché.
     * À la fin de la partie, le menu de fin de partie est affiché pour permettre au joueur de recommencer ou de quitter.
     */
    private void playGame() {
        Board savedBoard = this.boardTable.loadBoard(this.character);

        if (savedBoard != null) {
            this.menu.displayMessage("Plateau chargé pour " + this.character.getName() + " !");
            this.board = savedBoard;
        } else {
            this.board = new Board(74);
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

    /** Applique les effets de la tuile sur laquelle le personnage a atterri.
     * En fonction du type de tuile, différentes interactions peuvent se produire :
     * - Si c'est une tuile vide, rien ne se passe.
     * - Si c'est une tuile avec un ennemi, un combat est déclenché entre le personnage et l'ennemi.
     * - Si c'est une tuile avec un trésor, le personnage reçoit une récompense (arme, sort ou bouclier) qui peut être
     * équipée si elle est compatible avec la classe du personnage.
     * - Si c'est une tuile avec un piège, le personnage subit des dégâts. Le montant des dégâts peut être fixe ou
     * aléatoire en fonction du type de piège.
     * - Si c'est une tuile de départ ou d'arrivée, un message est affiché pour indiquer que le personnage a atteint
     * la case de départ ou d'arrivée.
     * @param tile La tuile sur laquelle le personnage a atterri et dont les effets doivent être appliqués.
     */
    private void applyTileEffect(Tile tile) {
        tile.interact(this.character);

        if (tile instanceof EnemyTile enemyTile && enemyTile.isDefeated()) {
            this.board.clearTile(this.character.getPosition());
        }

        if (tile instanceof ChestTile chestTile && chestTile.isOpened()) {
            this.board.clearTile(this.character.getPosition());
        }
    }

    /**
     * Affiche les statistiques du personnage dans le terminal. Cette méthode peut être appelée à différents moments du jeu
     * pour permettre au joueur de voir l'état actuel de son personnage, y compris sa vie, son équipement et ses autres
     * caractéristiques. Les statistiques sont affichées en utilisant la méthode toString() du personnage, qui doit être
     * implémentée pour fournir une représentation claire et informative des attributs du personnage.
     */
    private void displayCharacterStats() {
        System.out.println(this.character.toString());
    }

    /**
     * Affiche le menu de fin de partie, permettant au joueur de choisir entre recommencer une partie ou quitter le jeu.
     * En fonction du choix de l'utilisateur, appelle les méthodes appropriées pour gérer la suite du jeu.
     */
    private void endGameMenu() {
        menu.displayEndGameMenu();
        int choice = menu.readInt();

        if (choice == 1) {
            playGame();
        } else {
            menu.displayMessage("Au revoir !");
            running = false;
            db.closeConnection();
        }
    }

    /**
     *
     * @return Le personnage actuel du jeu.
     */
    public Character getCharacter() { return this.character; }

    /**
     * Définit le personnage actuel du jeu.
     * @param character Le personnage à définir pour le jeu.
     */
    public void setCharacter(Character character) { this.character = character; }

    /**
     * @return Le plateau de jeu actuel.
     */
    public Board getBoard() { return this.board; }

    /**
     * Définit le plateau de jeu actuel.
     * @param board Le plateau de jeu à définir pour le jeu.
     */
    public void setBoard(Board board) { this.board = board; }

    /**
     * @return Le dé utilisé dans le jeu.
     */
    public SixSidedDice getDice() { return this.dice; }

    /**
     * Définit le dé utilisé dans le jeu.
     * @param dice Le dé à définir pour le jeu.
     */
    public void setDice(SixSidedDice dice) { this.dice = dice; }

    /**
     * @return Le menu utilisé dans le jeu.
     */
    public Menu getMenu() { return this.menu; }

    /**
     * Définit le menu utilisé dans le jeu.
     * @param menu Le menu à définir pour le jeu.
     */
    public void setMenu(Menu menu) { this.menu = menu; }

    @Override
    public String toString() {
        return "Partie en cours avec : " + this.character.getName();
    }
}