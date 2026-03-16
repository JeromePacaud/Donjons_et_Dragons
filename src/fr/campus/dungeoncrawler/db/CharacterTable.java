package fr.campus.dungeoncrawler.db;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.stuff.Stuff;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.ProtectionSpell;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.WoodShield;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.*;

import java.sql.*;

/**
 * Classe de gestion de la table "characters" et de la table "equipments" associée.
 * Permet de créer les tables, d'insérer, mettre à jour, supprimer et charger des personnages avec leurs équipements.
 */
public class CharacterTable {

    private Connection connection;

    public CharacterTable(Connection connection) { this.connection = connection; }

    /**
     * Crée la table "characters" si elle n'existe pas déjà.
     * La table contient les colonnes : id, type, name, life_level, damage, defense, position et gold.
     */
    public void createCharacterTable() {
        String sql = "CREATE TABLE IF NOT EXISTS characters ("
                + "id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
                + "type VARCHAR(255) NOT NULL,"
                + "name VARCHAR(255) NOT NULL,"
                + "life_level INT NOT NULL,"
                + "damage INT NOT NULL,"
                + "defense INT NOT NULL DEFAULT 0,"
                + "position INT NOT NULL,"
                + "gold INT NOT NULL DEFAULT 0"
                + ")";
        try {
            connection.createStatement().execute(sql);
            System.out.println("Table 'characters' prête.");
        } catch (SQLException e) {
            System.err.println("Erreur création table characters : " + e.getMessage());
        }
    }

    /**
     * Crée la table "equipments" si elle n'existe pas déjà.
     * La table contient les colonnes : id, character_id, slot, type, name et stat_bonus.
     * La colonne character_id est une clé étrangère référencant l'id de la table characters.
     */
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

    /**
     * Insère un personnage dans la table "characters" et récupère son id généré.
     *
     * @param character Le personnage à insérer.
     */
    public void insertCharacter(Character character) {
        String sql = "INSERT INTO characters (type, name, life_level, damage, defense, position, gold) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(character, stmt);
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) character.setId(keys.getInt(1));
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    /**
     * Prépare la requête d'insertion ou de mise à jour avec les attributs du personnage.
     *
     * @param character Le personnage dont les attributs sont à utiliser.
     * @param stmt La requête préparée à remplir.
     * @throws SQLException Si une erreur SQL survient lors de la préparation de la requête.
     */
    private void setStatement(Character character, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, character.getType());
        stmt.setString(2, character.getName());
        stmt.setInt(3, character.getLifeLevel());
        stmt.setInt(4, character.getAttackLevel());
        stmt.setInt(5, character.getDefenseLevel());
        stmt.setInt(6, character.getPosition());
        stmt.setInt(7, character.getGold());
    }

    /**
     * Met à jour les informations d'un personnage existant dans la table "characters".
     *
     * @param character Le personnage à mettre à jour. Son id doit être défini pour identifier la ligne à modifier.
     */
    public void updateCharacter(Character character) {
        String sql = "UPDATE characters SET type = ?, name = ?, life_level = ?, damage = ?, defense = ?, position = ?, gold = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setStatement(character, stmt);
            stmt.setInt(8, character.getId());
            stmt.executeUpdate();
            System.out.println("Personnage <" + character.getName() + "> mis à jour !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    /**
     * Supprime un personnage de la table "characters". Les équipements associés seront automatiquement supprimés grâce à la contrainte de clé étrangère.
     *
     * @param character Le personnage à supprimer. Son id doit être défini pour identifier la ligne à supprimer.
     */
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

    /**
     * Charge un personnage depuis la table "characters" en fonction de son id, ainsi que ses équipements associés.
     *
     * @param id L'id du personnage à charger.
     * @return Le personnage chargé avec ses équipements, ou null si aucun personnage n'a été trouvé avec cet id.
     */
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
                character.setGold(rs.getInt("gold"));

                loadEquipments(character);
                return character;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
        }
        return null;
    }

    /**
     * Affiche tous les personnages présents dans la table "characters" avec leurs informations de base.
     * Les équipements ne sont pas affichés dans cette méthode, seulement les attributs principaux du personnage.
     */
    public void fetchAllCharacters() {
        String sql = "SELECT * FROM characters";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(
                        "[" + rs.getInt("id") + "] "
                            + rs.getString("name") + " (" + rs.getString("type") + ")\n"
                            + " | PV       : " + rs.getInt("life_level") + "\n"
                            + " | Attaque  : " + rs.getInt("damage") + "\n"
                            + " | Défense  : " + rs.getInt("defense") + "\n"
                            + " | Position : " + rs.getInt("position") + "\n"
                            + " | Or       : " + rs.getInt("gold") + " 🪙\n"
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnages : " + e.getMessage());
        }
    }

    /**
     * Sauvegarde les équipements d'un personnage dans la table "equipments". Les équipements existants pour ce personnage
     * sont d'abord supprimés avant d'insérer les nouveaux.
     *
     * @param character Le personnage dont les équipements sont à sauvegarder. Son id doit être défini pour identifier les lignes à supprimer et à insérer.
     */
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

    /**
     * Charge les équipements d'un personnage depuis la table "equipments" en fonction de son id.
     * Les équipements sont associés au personnage en fonction de leur slot (offensive ou defensive).
     *
     * @param character Le personnage pour lequel les équipements doivent être chargés. Son id doit être défini pour identifier les lignes à charger.
     */
    public void loadEquipments(Character character) {
        String sql = "SELECT * FROM equipments WHERE character_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, character.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String slot = rs.getString("slot");
                String type = rs.getString("type");
                Stuff stuff = buildStuff(type);
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

    /**
     * Supprime tous les équipements associés à un personnage de la table "equipments" en fonction de son id.
     *
     * @param character Le personnage pour lequel les équipements doivent être supprimés. Son id doit être défini pour identifier les lignes à supprimer.
     */
    private void deleteEquipments(Character character) {
        String sql = "DELETE FROM equipments WHERE character_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, character.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des équipements : " + e.getMessage());
        }
    }

    /**
     * Construit un objet Stuff à partir de son type sous forme de chaîne de caractères.
     * Cette méthode utilise un switch pour déterminer quelle classe de Stuff instancier en fonction du type fourni.
     *
     * @param type Le type de Stuff à construire, correspondant au nom de la classe (ex: "Sword", "Fireball", etc.).
     * @return Un objet Stuff correspondant au type fourni, ou null si le type est inconnu.
     */
    private Stuff buildStuff(String type) {
        return switch (type) {
            case "Sword" -> new Sword();
            case "Mace" -> new Mace();
            case "Bow" -> new Bow();
            case "Lightning" -> new Lightning();
            case "Fireball" -> new Fireball();
            case "Invisibility" -> new Invisibility();
            case "WoodShield" -> new WoodShield();
            case "ProtectionSpell"-> new ProtectionSpell();
            default -> {
                System.err.println("Type d'équipement inconnu : " + type);
                yield null;
            }
        };
    }

    /**
     * Vérifie si la table "characters" est vide en essayant de récupérer une ligne.
     *
     * @return true si la table est vide, false sinon ou en cas d'erreur.
     */
    public boolean isEmpty() {
        String sql = "SELECT 1 FROM characters LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            return !rs.next();
        } catch (SQLException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
        return false;
    }

    /**
     * Méthode de test pour supprimer les tables "characters" et "equipments" de la base de données.
     * Utile pour réinitialiser la base de données pendant le développement.
     */
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