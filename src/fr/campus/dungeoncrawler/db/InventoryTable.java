package fr.campus.dungeoncrawler.db;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.inventory.Inventory;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.BigPotion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.StandardPotion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Fireball;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Lightning;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Mace;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.Sword;

import java.sql.*;

public class InventoryTable {

    private Connection connection;

    public InventoryTable(Connection connection) {
        this.connection = connection;
    }

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

    public void loadInventory(Character character) {
        String sql = "SELECT * FROM inventory WHERE character_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, character.getId());
            ResultSet rs = stmt.executeQuery();

            Inventory inventory = new Inventory();
            while (rs.next()) {
                String category  = rs.getString("category");
                String stuffType = rs.getString("stuff_type");
                String name = rs.getString("name");
                int statBonus = rs.getInt("stat_bonus");

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

    public void deleteInventory(Character character) {
        String sql = "DELETE FROM inventory WHERE character_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, character.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'inventaire : " + e.getMessage());
        }
    }

    private Potion buildPotion(String stuffType) {
        return switch (stuffType) {
            case "StandardPotion" -> new StandardPotion();
            case "BigPotion" -> new BigPotion();
            default -> {
                System.err.println("Type de potion inconnu : " + stuffType);
                yield null;
            }
        };
    }

    private OffensiveStuff buildWeapon(String stuffType) {
        return switch (stuffType) {
            case "Sword" -> new Sword();
            case "Mace" -> new Mace();
            case "Lightning" -> new Lightning();
            case "Fireball" -> new Fireball();
            default -> {
                System.err.println("Type d'arme inconnu : " + stuffType);
                yield null;
            }
        };
    }
}