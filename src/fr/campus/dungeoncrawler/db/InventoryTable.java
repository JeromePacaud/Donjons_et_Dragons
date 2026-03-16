package fr.campus.dungeoncrawler.db;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.inventory.Inventory;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.Thunderbolt;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.BigPotion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.StandardPotion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.*;

import java.sql.*;

/**
 * Classe responsable de la gestion de la table "inventory" dans la base de données du jeu Dungeon Crawler.
 * <p>
 * Gère les opérations CRUD (Create, Read, Update, Delete) pour l'inventaire des personnages.
 * Chaque entrée dans la table représente un objet (potion ou arme) appartenant à un personnage spécifique.
 * </p>
 *
 * <h2>Structure de la table "inventory"</h2>
 * <ul>
 *   <li><strong>id</strong> : identifiant unique de l'entrée (auto-incrémenté)</li>
 *   <li><strong>character_id</strong> : référence à l'identifiant du personnage auquel appartient l'objet</li>
 *   <li><strong>category</strong> : catégorie de l'objet ("potion" ou "weapon")</li>
 *   <li><strong>stuff_type</strong> : type spécifique de l'objet (ex: "StandardPotion", "Sword")</li>
 *   <li><strong>name</strong> : nom de l'objet</li>
 *   <li><strong>stat_bonus</strong> : bonus de statistiques que l'objet confère au personnage</li>
 * </ul>
 *
 * <h2>Fonctionnalités</h2>
 * <ul>
 *   <li>{@link #createInventoryTable()} : crée la table "inventory" si elle n'existe pas déjà</li>
 *   <li>{@link #saveInventory(Character)} : sauvegarde l'inventaire d'un personnage dans la base de données</li>
 *   <li>{@link #loadInventory(Character)} : charge l'inventaire d'un personnage depuis la base de données</li>
 *   <li>{@link #deleteInventory(Character)} : supprime tous les objets liés à un personnage dans la base de données</li>
 * </ul>
 */
public class InventoryTable {

    private Connection connection;

    /**
     * Constructeur de la classe InventoryTable, initialisant la connexion à la base de données.
     *
     * @param connection La connexion à la base de données utilisée pour exécuter les opérations sur la table "inventory"
     */
    public InventoryTable(Connection connection) {
        this.connection = connection;
    }

    /**
     * Crée la table "inventory" dans la base de données si elle n'existe pas déjà.
     * <p>
     * La table contient les colonnes suivantes :
     * </p>
     * <ul>
     *   <li><strong>id</strong> : identifiant unique de l'entrée (auto-incrémenté)</li>
     *   <li><strong>character_id</strong> : référence à l'identifiant du personnage auquel appartient l'objet</li>
     *   <li><strong>category</strong> : catégorie de l'objet ("potion" ou "weapon")</li>
     *   <li><strong>stuff_type</strong> : type spécifique de l'objet (ex: "StandardPotion", "Sword")</li>
     *   <li><strong>name</strong> : nom de l'objet</li>
     *   <li><strong>stat_bonus</strong> : bonus de statistiques que l'objet confère au personnage</li>
     * </ul>
     * <p>
     * La colonne character_id est une clé étrangère qui référence la table "characters" et est configurée pour supprimer en cascade les entrées liées lorsque le personnage est supprimé.
     * </p>
     */
    public void createInventoryTable() {

        String sql = "CREATE TABLE IF NOT EXISTS inventory ("
                + "id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
                + "character_id INT NOT NULL,"
                + "category VARCHAR(20) NOT NULL,"
                + "stuff_type VARCHAR(100) NOT NULL,"
                + "name VARCHAR(255) NOT NULL,"
                + "stat_bonus INT NOT NULL,"
                + "FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE"
                + ")";
        try {
            connection.createStatement().execute(sql);
        } catch (SQLException e) {
            System.err.println("Erreur création table inventory : " + e.getMessage());
        }
    }

    /**
     * Sauvegarde l'inventaire d'un personnage dans la base de données.
     * <p>
     * Cette méthode supprime d'abord les entrées existantes pour le personnage afin d'éviter les doublons, puis insère les potions et armes actuelles de l'inventaire.
     * Si l'inventaire est vide (pas de potions ni d'armes), aucune entrée n'est créée.
     * </p>
     *
     * @param character Le personnage dont l'inventaire doit être sauvegardé
     */
    public void saveInventory(Character character) {
        deleteInventory(character);

        Inventory inv = character.getInventory();
        if (inv.isPotionsEmpty() && inv.isWeaponsEmpty()) return;

        String sql = "INSERT INTO inventory (character_id, category, stuff_type, name, stat_bonus) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (Potion potion : inv.getPotions()) {
                stmt.setInt(1, character.getId());
                stmt.setString(2, "potion");
                stmt.setString(3, potion.getClass().getSimpleName());
                stmt.setString(4, potion.getName());
                stmt.setInt(5, potion.getStatBonus());
                stmt.executeUpdate();
            }

            for (OffensiveStuff weapon : inv.getWeapons()) {
                stmt.setInt(1, character.getId());
                stmt.setString(2, "weapon");
                stmt.setString(3, weapon.getClass().getSimpleName());
                stmt.setString(4, weapon.getName());
                stmt.setInt(5, weapon.getStatBonus());
                stmt.executeUpdate();
            }

            System.out.println("Inventaire sauvegardé !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde de l'inventaire : " + e.getMessage());
        }
    }

    /**
     * Charge l'inventaire d'un personnage depuis la base de données.
     * <p>
     * Cette méthode récupère toutes les entrées de la table "inventory" associées au personnage, reconstruit les objets correspondants (potions et armes) et les ajoute à l'inventaire du personnage.
     * Si aucune entrée n'est trouvée, l'inventaire du personnage restera vide.
     * </p>
     *
     * @param character Le personnage dont l'inventaire doit être chargé
     */
    public void loadInventory(Character character) {
        String sql = "SELECT * FROM inventory WHERE character_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, character.getId());
            ResultSet rs = stmt.executeQuery();

            Inventory inventory = new Inventory();
            while (rs.next()) {
                String category  = rs.getString("category");
                String stuffType = rs.getString("stuff_type");

                if (category.equals("potion")) {
                    Potion potion = buildPotion(stuffType);
                    if (potion != null) inventory.addPotionSilent(potion);
                } else if (category.equals("weapon")) {
                    OffensiveStuff weapon = buildWeapon(stuffType);
                    if (weapon != null) inventory.addWeaponSilent(weapon);
                }
            }
            character.setInventory(inventory);
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement de l'inventaire : " + e.getMessage());
        }
    }

    /**
     * Supprime tous les objets de l'inventaire d'un personnage dans la base de données.
     * <p>
     * Cette méthode est utilisée avant de sauvegarder un nouvel inventaire pour éviter les doublons. Elle supprime toutes les entrées de la table "inventory" associées au personnage spécifié.
     * </p>
     *
     * @param character Le personnage dont l'inventaire doit être supprimé
     */
    public void deleteInventory(Character character) {
        String sql = "DELETE FROM inventory WHERE character_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, character.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'inventaire : " + e.getMessage());
        }
    }

    /**
     * Méthode utilitaire pour construire une instance de Potion à partir du type de potion stocké dans la base de données.
     * <p>
     * Cette méthode utilise une instruction switch pour déterminer quel type de potion créer en fonction du nom de la classe (ex: "StandardPotion", "BigPotion", "Thunderbolt").
     * Si le type de potion n'est pas reconnu, un message d'erreur est affiché et la méthode retourne null.
     * </p>
     *
     * @param stuffType Le nom de la classe de potion à construire
     * @return Une instance de Potion correspondant au type spécifié, ou null si le type est inconnu
     */
    private Potion buildPotion(String stuffType) {
        return switch (stuffType) {
            case "StandardPotion" -> new StandardPotion();
            case "BigPotion" -> new BigPotion();
            case "Thunderbolt" -> new Thunderbolt();
            default -> {
                System.err.println("Type de potion inconnu : " + stuffType);
                yield null;
            }
        };
    }

    /**
     * Méthode utilitaire pour construire une instance de OffensiveStuff à partir du type d'arme stocké dans la base de données.
     * <p>
     * Cette méthode utilise une instruction switch pour déterminer quel type d'arme ou sort offensif créer en fonction du nom de la classe (ex: "Sword", "Fireball").
     * Si le type d'arme n'est pas reconnu, un message d'erreur est affiché et la méthode retourne null.
     * </p>
     *
     * @param stuffType Le nom de la classe d'arme à construire
     * @return Une instance de OffensiveStuff correspondant au type spécifié, ou null si le type est inconnu
     */
    private OffensiveStuff buildWeapon(String stuffType) {
        return switch (stuffType) {
            case "Sword" -> new Sword();
            case "Mace" -> new Mace();
            case "Lightning" -> new Lightning();
            case "Fireball" -> new Fireball();
            case "Bow" -> new Bow();
            case "Invisibility" -> new Invisibility();
            default -> {
                System.err.println("Type d'arme inconnu : " + stuffType);
                yield null;
            }
        };
    }
}