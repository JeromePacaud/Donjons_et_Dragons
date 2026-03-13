package fr.campus.dungeoncrawler.db;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.stuff.defensivestuff.DefensiveStuff;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.WoodShield;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;
import fr.campus.dungeoncrawler.stuff.Stuff;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.ProtectionSpell;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Fireball;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Lightning;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Mace;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Sword;

import java.rmi.ServerError;
import java.sql.*;

public class CharacterTable {

    private Connection connection;

    public CharacterTable(Connection connection) {this.connection = connection;}

    public void createCharacterTable() {
        String sql = "CREATE TABLE IF NOT EXISTS characters ("
                + "id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
                + "type VARCHAR(255) NOT NULL,"
                + "name VARCHAR(255) NOT NULL,"
                + "life_level INT NOT NULL,"
                + "damage INT NOT NULL,"
                + "defense INT NOT NULL DEFAULT 0,"
                + "position INT NOT NULL"
                + ")";
        try {
            connection.createStatement().execute(sql);
            System.out.println("Table 'characters' prête.");
        } catch (SQLException e) {
            System.err.println("Erreur création table characters : " + e.getMessage());
        }
    }

    public void createEquipmentTable() {
        String sql = "CREATE TABLE IF NOT EXISTS equipments ("
                + "id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
                + "character_id INT NOT NULL,"
                + "slot VARCHAR(50) NOT NULL,"
                + "type VARCHAR(100) NOT NULL,"
                + "name VARCHAR(255) NOT NULL,"
                + "stat_bonus INT NOT NULL,"
                + "FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE"
                + ")";
        try {
            connection.createStatement().execute(sql);
            System.out.println("Table 'equipments' prête.");
        } catch (SQLException e) {
            System.err.println("Erreur création table equipments : " + e.getMessage());
        }
    }

    public void insertCharacter(Character character) {
        String sql = "INSERT INTO characters (type, name, life_level, damage, defense, position) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(character, stmt);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                character.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    private void setStatement(Character character, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, character.getType());
        stmt.setString(2, character.getName());
        stmt.setInt(3, character.getLifeLevel());
        stmt.setInt(4, character.getAttackLevel());
        stmt.setInt(5, character.getDefenseLevel());
        stmt.setInt(6, character.getPosition());
    }

    public void updateCharacter(Character character) {
        String sql = "UPDATE characters SET type = ?, name = ?, life_level = ?, damage = ?, defense = ?, position = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setStatement(character, stmt);
            stmt.setInt(7, character.getId());
            stmt.executeUpdate();
            System.out.println("Personnage <" + character.getName() + "> mis à jour !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    public void deleteCharacter(Character character) {
        String sql = "DELETE FROM characters WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, character.getId());
            stmt.executeUpdate();
            System.out.println("Personnage <" + character.getName() + "> supprimé !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    public Character loadCharacter(int id) {
        String sql = "SELECT * FROM characters WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String type = rs.getString("type");
                Character character = type.equals("Warrior")
                        ? new Warrior(rs.getString("name"))
                        : new Wizard(rs.getString("name"));

                character.setId(rs.getInt("id"));
                character.setLifeLevel(rs.getInt("life_level"));
                character.setAttackLevel(rs.getInt("damage"));
                character.setDefenseLevel(rs.getInt("defense"));
                character.setPosition(rs.getInt("position"));

                loadEquipments(character);

                return character;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
        }
        return null;
    }

    public void fetchAllCharacters() {
        String sql = "SELECT * FROM characters";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(
                        "[" + rs.getInt("id") + "] "
                        + rs.getString("name") + " (" + rs.getString("type") + ")\n"
                        + " | PV : " + rs.getInt("life_level") + "\n"
                        + " | Attaque  : " + rs.getInt("damage") + "\n"
                        + " | Défense  : " + rs.getInt("defense") + "\n"
                        + " | Position : " + rs.getInt("position") + "\n"
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnages : " + e.getMessage());
        }
    }

    public void saveEquipments(Character character) {
        deleteEquipments(character);

        String sql = "INSERT INTO equipments (character_id, slot, type, name, stat_bonus) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (character.getOffensiveStuff() != null) {
                Stuff s = character.getOffensiveStuff();
                stmt.setInt(1, character.getId());
                stmt.setString(2, "offensive");
                stmt.setString(3, s.getClass().getSimpleName());
                stmt.setString(4, s.getName());
                stmt.setInt(5, s.getStatBonus());
                stmt.executeUpdate();
            }

            if (character.getDefensiveStuff() != null) {
                Stuff s = character.getDefensiveStuff();
                stmt.setInt(1, character.getId());
                stmt.setString(2, "defensive");
                stmt.setString(3, s.getClass().getSimpleName());
                stmt.setString(4, s.getName());
                stmt.setInt(5, s.getStatBonus());
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde des équipements : " + e.getMessage());
        }
    }

    public void loadEquipments(Character character) {
        String sql = "SELECT * FROM equipments WHERE character_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, character.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String slot = rs.getString("slot");
                String type = rs.getString("type");
                String name = rs.getString("name");
                int statBonus = rs.getInt("stat_bonus");

                Stuff stuff = buildStuff(type, name, statBonus);
                if (stuff == null) continue;

                if (slot.equals("offensive")) {
                    character.setOffensiveStuff(stuff);
                } else {
                    character.setDefensiveStuff(stuff);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des équipements : " + e.getMessage());
        }
    }

    private void deleteEquipments(Character character) {
        String sql = "DELETE FROM equipments WHERE character_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, character.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des équipements : " + e.getMessage());
        }
    }

    private Stuff buildStuff(String type, String name, int statBonus) {
        return switch (type) {
            case "Sword" -> new Sword();
            case "Mace" -> new Mace();
            case "Lightning" -> new Lightning();
            case "Fireball" -> new Fireball();
            case "WoodShield" -> new WoodShield();
            case "ProtectionSpell" -> new ProtectionSpell();
            default -> {
                System.err.println("Type d'équipement inconnu : " + type);
                yield null;
            }
        };
    }

    public boolean isEmpty() {
        String sql = "SELECT 1 FROM characters LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return !rs.next();

        } catch (SQLException e) {
            System.err.println("Erreur : " + e.getMessage());;
        }
        return false;
    }

    public static void main(String[] args) {
        SQLDatabaseConnection db = new SQLDatabaseConnection();
        db.getConnection();
        db.useDatabase("dungeoncrawler");
        db.dropTable("board_tiles");
        db.dropTable("boards");
        db.dropTable("equipments");
        db.dropTable("inventory");
        db.dropTable("characters");
        db.closeConnection();
    }
}