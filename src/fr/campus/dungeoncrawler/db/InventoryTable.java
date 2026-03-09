package fr.campus.dungeoncrawler.db;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.inventory.Inventory;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.BigPotion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.StandardPotion;

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
                + "stuff_type VARCHAR(100) NOT NULL,"
                + "name VARCHAR(255) NOT NULL,"
                + "stat_bonus INT NOT NULL,"
                + "FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE"
                + ")";
        try {
            connection.createStatement().execute(sql);
            System.out.println("Table 'inventory' prête.");
        } catch (SQLException e) {
            System.err.println("Erreur création table inventory : " + e.getMessage());
        }
    }

    public void saveInventory(Character character) {
        deleteInventory(character);

        if (character.getInventory().isEmpty()) return;

        String sql = "INSERT INTO inventory (character_id, stuff_type, name, stat_bonus) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Potion potion : character.getInventory().getPotions()) {
                stmt.setInt(1, character.getId());
                stmt.setString(2, potion.getClass().getSimpleName());
                stmt.setString(3, potion.getName());
                stmt.setInt(4, potion.getStatBonus());
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
                String stuffType = rs.getString("stuff_type");
                String name = rs.getString("name");
                int statBonus = rs.getInt("stat_bonus");
                Potion potion = buildPotion(stuffType, name, statBonus);
                if (potion != null) {
                    inventory.addPotion(potion);
                }
            }
            character.setInventory(inventory);
            System.out.println("Inventaire chargé !");
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

    private Potion buildPotion(String stuffType, String name, int statBonus) {
        return switch (stuffType) {
            case "StandardPotion" -> new StandardPotion();
            case "BigPotion" -> new BigPotion();
            default -> {
                System.err.println("Type de potion inconnu : " + stuffType);
                yield null;
            }
        };
    }
}