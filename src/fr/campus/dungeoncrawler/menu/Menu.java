package fr.campus.dungeoncrawler.menu;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;

import java.util.Scanner;

public class Menu {

    private Scanner scanner;

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMainMenu() {
        System.out.println("\n=== DUNGEON CRAWLER ===");
        System.out.println("[1] Nouveau personnage");
        System.out.println("[2] Charger un personnage");
        System.out.println("[3] Supprimer un personnage");
        System.out.println("[4] Quitter");
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

    public void displayInGameMenu() {
        System.out.println("\n--- Que voulez-vous faire ? ---");
        System.out.println("[1] Lancer le dé");
        System.out.println("[2] Sauvegarder et quitter");
        System.out.println("[3] Régénérer le plateau");
        System.out.print("Votre choix : ");
    }

    public void displayCombatMenu(Character character) {
        System.out.println("\n⚔️  Que faites-vous ?");

        System.out.println("[1] Attaquer");

        String weaponInfo = character.getInventory().isWeaponsEmpty()
                ? character instanceof Warrior ? " (poing)" : " (ki)"
                : " (" + character.getInventory().getWeaponsSize() + " arme(s) dispo)";
        System.out.println("[2] Changer d'arme" + weaponInfo);

        String potionInfo = character.getInventory().isPotionsEmpty()
                ? " (vide)"
                : " (" + character.getInventory().getPotionsSize() + " dispo)";
        System.out.println("[3] Utiliser une potion" + potionInfo);

        System.out.println("[4] Fuir");
        System.out.print("Votre choix : ");
    }

    public void displayPotionsMenu(Potion potion) {
        System.out.println("[1] Consommer maintenant (+" + potion.getStatBonus() + " PV)");
        System.out.println("[2] Stocker dans l'inventaire");
        System.out.print("Votre choix : ");
    }

    public void displayWeaponReplaceMenu(Character character, OffensiveStuff newItem) {
        System.out.println("⚠️ Inventaire armes/sorts plein !");
        System.out.println("Que voulez-vous faire avec : " + newItem.getName() + " (PA: " + newItem.getStatBonus() + ") ?");
        for (int i = 0; i < character.getInventory().getWeaponsSize(); i++) {
            OffensiveStuff weapon = character.getInventory().getWeapon(i);
            System.out.println("[" + (i + 1) + "] Remplacer : " + weapon.getName() + " (PA: " + weapon.getStatBonus() + ")");
        }
        System.out.println("[" + (character.getInventory().getWeaponsSize() + 1) + "] Abandonner");
        System.out.print("Votre choix : ");
    }

    public void displayWeaponSelectMenu(Character character) {
        System.out.println("\n🗡️  Choisissez votre arme :");
        for (int i = 0; i < character.getInventory().getWeaponsSize(); i++) {
            OffensiveStuff weapon = character.getInventory().getWeapon(i);
            String active = (character.getOffensiveStuff() != null
                    && character.getOffensiveStuff().equals(weapon)) ? " ✅" : "";
            System.out.println("[" + (i + 1) + "] " + weapon.toString() + active);
        }
        System.out.println("[" + (character.getInventory().getWeaponsSize() + 1) + "] Attaquer sans changer d'arme");
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