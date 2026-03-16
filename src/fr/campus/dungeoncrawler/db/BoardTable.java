package fr.campus.dungeoncrawler.db;

import fr.campus.dungeoncrawler.board.Board;
import fr.campus.dungeoncrawler.board.tile.*;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.enemy.*;
import fr.campus.dungeoncrawler.stuff.Stuff;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.ProtectionSpell;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.Thunderbolt;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.WoodShield;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.BigPotion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.StandardPotion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.*;

import java.sql.*;

/**
 * Gère la table "boards" et "board_tiles" pour sauvegarder et charger les plateaux de jeu.
 * Chaque plateau est lié à un personnage, et chaque case du plateau est stockée avec ses détails.
 */
public class BoardTable {

    private Connection connection;

    public BoardTable(Connection connection) {
        this.connection = connection;
    }

    /**
     * Crée la table "boards" si elle n'existe pas déjà.
     * La table contient une référence au personnage et la taille du plateau.
     */
    public void createBoardTable() {
        String sql = "CREATE TABLE IF NOT EXISTS boards ("
                + "id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
                + "character_id INT NOT NULL,"
                + "size INT NOT NULL,"
                + "FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE"
                + ")";
        try {
            connection.createStatement().execute(sql);
            System.out.println("Table 'boards' prête.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table boards : " + e.getMessage());
        }
    }

    /**
     * Crée la table "board_tiles" si elle n'existe pas déjà.
     * Chaque ligne représente une case d'un plateau, avec des détails sur le type de case, les ennemis, les récompenses, etc.
     * La table est liée à la table "boards" via une clé étrangère.
     */
    public void createBoardTilesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS board_tiles ("
                + "id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
                + "board_id INT NOT NULL,"
                + "position INT NOT NULL,"
                + "tile_type VARCHAR(50) NOT NULL,"
                + "enemy_type VARCHAR(50),"
                + "enemy_hp INT,"
                + "stuff_type VARCHAR(50),"
                + "is_opened BOOLEAN,"
                + "FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE"
                + ")";
        try {
            connection.createStatement().execute(sql);
            System.out.println("Table 'board_tiles' prête.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table board_tiles : " + e.getMessage());
        }
    }

    /**
     * Sauvegarde le plateau d'un personnage dans la base de données.
     * Si un plateau existe déjà pour ce personnage, il est supprimé avant d'enregistrer le nouveau.
     * Chaque case du plateau est également enregistrée dans la table "board_tiles" avec ses détails.
     *
     * @param board Le plateau à sauvegarder
     */
    public void saveBoard(Board board, Character character) {
        deleteBoardByCharacter(character);

        String sql = "INSERT INTO boards (character_id, size) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, character.getId());
            stmt.setInt(2, board.getSize());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int boardId = keys.getInt(1);
                saveBoardTiles(board, boardId);
            }
            System.out.println("Plateau sauvegardé !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde du plateau : " + e.getMessage());
        }
    }

    /**
     * Sauvegarde les cases d'un plateau dans la table "board_tiles".
     * Chaque case est enregistrée avec son type, les détails de l'ennemi (si c'est une case ennemie), les détails de la récompense (si c'est une case coffre), etc.
     *
     * @param board Le plateau dont les cases doivent être sauvegardées
     * @param boardId L'identifiant du plateau dans la table "boards" pour lier les cases à ce plateau
     */
    private void saveBoardTiles(Board board, int boardId) {
        String sql = "INSERT INTO board_tiles (board_id, position, tile_type, enemy_type, enemy_hp, stuff_type, is_opened) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < board.getSize(); i++) {
                Tile tile = board.getTile(i);

                stmt.setInt(1, boardId);
                stmt.setInt(2, i);
                stmt.setString(3, tile.getType());

                if (tile instanceof EnemyTile enemyTile) {
                    stmt.setString(4, enemyTile.getEnemy() != null ? enemyTile.getEnemy().getType() : null);
                    stmt.setObject(5, enemyTile.getEnemy() != null ? enemyTile.getEnemy().getLifeLevel() : null);
                    stmt.setNull(6, Types.VARCHAR);
                    stmt.setNull(7, Types.BOOLEAN);
                } else if (tile instanceof ChestTile chestTile) {
                    stmt.setNull(4, Types.VARCHAR);
                    stmt.setNull(5, Types.INTEGER);
                    stmt.setString(6, chestTile.getReward() != null ? chestTile.getReward().getClass().getSimpleName() : null);
                    stmt.setBoolean(7, chestTile.isOpened());
                } else {
                    stmt.setNull(4, Types.VARCHAR);
                    stmt.setNull(5, Types.INTEGER);
                    stmt.setNull(6, Types.VARCHAR);
                    stmt.setNull(7, Types.BOOLEAN);
                }

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde des cases : " + e.getMessage());
        }
    }

    /**
     * Charge le plateau d'un personnage depuis la base de données.
     * Récupère d'abord les informations du plateau (taille, etc.) puis charge chaque case avec ses détails.
     *
     * @param character Le personnage dont le plateau doit être chargé
     * @return Le plateau chargé, ou null si aucun plateau n'est trouvé pour ce personnage
     */
    public Board loadBoard(Character character) {
        String sql = "SELECT * FROM boards WHERE character_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, character.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int boardId = rs.getInt("id");
                int size = rs.getInt("size");
                return loadBoardTiles(boardId, size);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement du plateau : " + e.getMessage());
        }
        return null;
    }

    /**
     * Charge les cases d'un plateau depuis la table "board_tiles" en fonction de l'identifiant du plateau.
     * Chaque case est reconstruite avec ses détails (type de case, ennemi, récompense, etc.) pour recréer le plateau complet.
     *
     * @param boardId L'identifiant du plateau dans la table "boards"
     * @param size La taille du plateau pour initialiser l'objet Board
     * @return Le plateau avec toutes ses cases chargées
     */
    private Board loadBoardTiles(int boardId, int size) {
        Board board = new Board(size);
        String sql = "SELECT * FROM board_tiles WHERE board_id = ? ORDER BY position";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, boardId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int position = rs.getInt("position");
                String tileType = rs.getString("tile_type");
                Tile tile = buildTile(tileType, rs);
                if (tile != null) {
                    board.setTile(position, tile);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des cases : " + e.getMessage());
        }
        return board;
    }

    /**
     * Construit une case (Tile) à partir des informations récupérées de la base de données.
     * En fonction du type de case, les détails supplémentaires (ennemi, récompense, etc.) sont utilisés pour créer l'objet Tile approprié.
     *
     * @param tileType Le type de la case (Start, End, Enemy, Chest, etc.)
     * @param rs Le ResultSet contenant les détails de la case pour construire l'objet Tile
     * @return L'objet Tile construit à partir des données, ou une EmptyTile si les données sont invalides
     * @throws SQLException Si une erreur survient lors de la lecture des données du ResultSet
     */
    private Tile buildTile(String tileType, ResultSet rs) throws SQLException {
        switch (tileType) {
            case "Start": return new StartTile();
            case "End": return new EndTile();
            case "Merchant": return new MerchantTile();
            case "Inn": return new InnTile();

            case "Enemy":
                String enemyType = rs.getString("enemy_type");
                int enemyHp = rs.getInt("enemy_hp");
                if (enemyType == null) return new EmptyTile();
                return buildEnemyTile(enemyType, enemyHp);

            case "Chest":
                String stuffType = rs.getString("stuff_type");
                boolean isOpened = rs.getBoolean("is_opened");
                if (isOpened || stuffType == null) return new EmptyTile();
                Stuff reward = buildStuff(stuffType);
                if (reward == null) return new EmptyTile();
                return new ChestTile(reward);

            default: return new EmptyTile();
        }
    }

    /**
     * Construit une case ennemie (EnemyTile) à partir du type d'ennemi et de ses points de vie.
     * En fonction du type d'ennemi, l'objet Enemy approprié est créé et encapsulé dans une EnemyTile.
     *
     * @param enemyType Le type de l'ennemi (Dragon, Sorcerer, Goblin, etc.)
     * @param enemyHp Les points de vie de l'ennemi pour initialiser son niveau de vie
     * @return Une EnemyTile contenant l'ennemi construit à partir des données, ou null si le type d'ennemi est inconnu
     */
    private EnemyTile buildEnemyTile(String enemyType, int enemyHp) {
        switch (enemyType) {
            case "Dragon": {
                Dragon d = new Dragon();
                d.setLifeLevel(enemyHp);
                return new EnemyTile(d);
            }
            case "Sorcerer": {
                Sorcerer s = new Sorcerer();
                s.setLifeLevel(enemyHp);
                return new EnemyTile(s);
            }
            case "Goblin": {
                Goblin g = new Goblin();
                g.setLifeLevel(enemyHp);
                return new EnemyTile(g);
            }
            case "Orc": {
                Orc o = new Orc();
                o.setLifeLevel(enemyHp);
                return new EnemyTile(o);
            }
            case "EvilSpirit":{
                EvilSpirit es = new EvilSpirit();
                es.setLifeLevel(enemyHp);
                return new EnemyTile(es);
            }
            default: return null;
        }
    }

    /**
     * Construit un objet Stuff à partir du type de stuff récupéré de la base de données.
     * En fonction du type de stuff, l'objet Stuff approprié est créé pour être utilisé comme récompense dans une case coffre.
     *
     * @param stuffType Le type de stuff (Sword, Mace, Lightning, Fireball, StandardPotion, BigPotion, WoodShield, ProtectionSpell, Bow, Invisibility, Thunderbolt)
     * @return L'objet Stuff construit à partir du type, ou null si le type de stuff est inconnu
     */
    private Stuff buildStuff(String stuffType) {
        return switch (stuffType) {
            case "Sword" -> new Sword();
            case "Mace" -> new Mace();
            case "Lightning" -> new Lightning();
            case "Fireball" -> new Fireball();
            case "StandardPotion" -> new StandardPotion();
            case "BigPotion" -> new BigPotion();
            case "WoodShield" -> new WoodShield();
            case "ProtectionSpell"-> new ProtectionSpell();
            case "Bow" -> new Bow();
            case "Invisibility" -> new Invisibility();
            case "Thunderbolt" -> new Thunderbolt();
            default -> {
                System.err.println("Type d'item inconnu : " + stuffType);
                yield null;
            }
        };
    }

    /**
     * Supprime le plateau d'un personnage de la base de données.
     * Cette méthode est utilisée avant de sauvegarder un nouveau plateau pour s'assurer qu'il n'y a pas de doublons.
     *
     * @param character Le personnage dont le plateau doit être supprimé
     */
    public void deleteBoardByCharacter(Character character) {
        String sql = "DELETE FROM boards WHERE character_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, character.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du plateau : " + e.getMessage());
        }
    }
}