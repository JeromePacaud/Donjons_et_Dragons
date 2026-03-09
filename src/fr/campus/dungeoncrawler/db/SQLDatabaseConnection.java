package fr.campus.dungeoncrawler.db;

import java.sql.*;

public class SQLDatabaseConnection {

    private final String url      = "jdbc:mysql://localhost:3306/";
    private final String user     = "root";
    private final String password = "";
    private Connection connection;

    public SQLDatabaseConnection() {}

    public void getConnection() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connecté au serveur MySQL.");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
    }

    public void createDatabase() {
        String sql = "CREATE DATABASE IF NOT EXISTS dungeoncrawler"
                + " CHARACTER SET utf8mb4"
                + " COLLATE utf8mb4_unicode_ci";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("Base de données 'dungeoncrawler' prête.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la base : " + e.getMessage());
        }
    }

    public void useDatabase(String databaseName) {
        String sql = "USE " + databaseName;
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Base de données introuvable <" + databaseName + "> : " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données rompue.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la déconnexion : " + e.getMessage());
        }
    }

    public Connection getActiveConnection() {
        return connection;
    }

    public void dropTable(String characters) {
        String sql = "DROP TABLE IF EXISTS " + characters;
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("Table '" + characters + "' supprimée.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la table : " + e.getMessage());
        }
    }
}