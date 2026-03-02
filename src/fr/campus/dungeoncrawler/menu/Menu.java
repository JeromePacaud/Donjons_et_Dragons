package fr.campus.dungeoncrawler.menu;

import java.util.Scanner;

public class Menu {

    private Scanner scanner;

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMainMenu() {
        System.out.println("\n=== DUNGEON CRAWLER ===");
        System.out.println("[1] Nouveau personnage");
        System.out.println("[2] Quitter");
        System.out.print("Votre choix : ");
    }

    public void displayTypeMenu() {
        System.out.println("\nChoisissez votre type de personnage :");
        System.out.println("[1] Warrior (Guerrier)");
        System.out.println("[2] Wizard (Magicien)");
        System.out.print("Votre choix : ");
    }

    public void displayCharacterMenu() {
        System.out.println("\n--- Menu personnage ---");
        System.out.println("[1] Afficher les infos du personnage");
        System.out.println("[2] Modifier le nom");
        System.out.println("[3] Démarrer la partie");
        System.out.println("[4] Quitter");
        System.out.print("Votre choix : ");
    }

    public void displayEndGameMenu() {
        System.out.println("\n--- Fin de partie ---");
        System.out.println("[1] Recommencer");
        System.out.println("[2] Quitter");
        System.out.print("Votre choix : ");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Entrée invalide, réessayez : ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public String toString() {
        return "MENU PRINCIPAL";
    }
}
