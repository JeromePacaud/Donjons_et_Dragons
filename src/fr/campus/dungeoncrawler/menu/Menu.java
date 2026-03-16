package fr.campus.dungeoncrawler.menu;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.board.tile.MerchantTile;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;

import java.util.List;
import java.util.Scanner;

/**
 * Menu.java
 * <p>
 * Classe responsable de l'affichage des différents menus du jeu et de la gestion des entrées utilisateur.
 * Elle centralise tous les affichages liés aux choix du joueur, que ce soit pour la création de personnage,
 * les combats, les interactions avec le marchand, ou les options en jeu.
 * </p>
 * <p>
 * Cette classe utilise un Scanner pour lire les entrées utilisateur et propose des méthodes dédiées pour chaque type de menu.
 * Elle permet également d'afficher des messages personnalisés et de gérer les choix de manière structurée.
 * </p>
 */
public class Menu {

    private Scanner scanner;

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Affiche le menu principal du jeu avec les options de création, chargement, suppression de personnage et quitter.
     */
    public void displayMainMenu() {
        System.out.println("\n=== DUNGEON CRAWLER ===");
        System.out.println("[1] Nouveau personnage");
        System.out.println("[2] Charger un personnage");
        System.out.println("[3] Supprimer un personnage");
        System.out.println("[4] Quitter");
        System.out.print("Votre choix : ");
    }

    /**
     * Affiche le menu de sélection du type de personnage avec les options pour choisir entre Guerrier et Magicien.
     */
    public void displayTypeMenu() {
        System.out.println("\nChoisissez votre type de personnage :");
        System.out.println("[1] Warrior (Guerrier)");
        System.out.println("[2] Wizard (Magicien)");
        System.out.print("Votre choix : ");
    }

    /**
     * Affiche le menu de gestion du personnage avec les options pour afficher les infos, modifier le nom, démarrer la partie ou quitter.
     */
    public void displayCharacterMenu() {
        System.out.println("\n--- Menu personnage ---");
        System.out.println("[1] Afficher les infos du personnage");
        System.out.println("[2] Modifier le nom");
        System.out.println("[3] Démarrer la partie");
        System.out.println("[4] Quitter");
        System.out.print("Votre choix : ");
    }

    /**
     * Affiche le menu en jeu avec les options pour lancer le dé, sauvegarder et quitter, ou régénérer le plateau.
     */
    public void displayInGameMenu() {
        System.out.println("\n--- Que voulez-vous faire ? ---");
        System.out.println("[1] Lancer le dé");
        System.out.println("[2] Sauvegarder et quitter");
        System.out.println("[3] Régénérer le plateau");
        System.out.print("Votre choix : ");
    }

    /**
     * Affiche le menu de combat avec les options pour attaquer, changer d'arme, utiliser une potion ou fuir.
     * Les options de changement d'arme et d'utilisation de potion affichent des informations sur la disponibilité des armes et potions dans l'inventaire du personnage.
     *
     * @param character Le personnage pour lequel le menu de combat est affiché, utilisé pour afficher les informations sur les armes et potions disponibles.
     */
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

    /**
     * Affiche le menu de gestion des potions avec les options pour consommer la potion immédiatement ou la stocker dans l'inventaire.
     * Affiche également le bonus de points de vie que la potion confère si consommée.
     *
     * @param potion La potion pour laquelle le menu est affiché, utilisée pour afficher le bonus de points de vie associé à la potion.
     */
    public void displayPotionsMenu(Potion potion) {
        System.out.println("[1] Consommer maintenant (+" + potion.getStatBonus() + " PV)");
        System.out.println("[2] Stocker dans l'inventaire");
        System.out.print("Votre choix : ");
    }

    /**
     * Affiche le menu de remplacement d'arme/sort lorsque l'inventaire est plein, avec les options pour remplacer une arme/sort existante ou abandonner le nouvel item.
     * Affiche également les informations sur le nouvel item et les armes/sorts actuellement dans l'inventaire du personnage.
     *
     * @param character Le personnage pour lequel le menu de remplacement est affiché, utilisé pour afficher les armes/sorts actuellement dans l'inventaire du personnage.
     * @param newItem   Le nouvel item que le personnage vient de trouver, utilisé pour afficher les informations sur le nouvel item.
     */
    public void displayWeaponReplaceMenu(Character character, OffensiveStuff newItem) {
        System.out.println(">>> ⚠️ Inventaire armes/sorts plein !");
        System.out.println("Que voulez-vous faire avec : " + newItem.getName() + " (PA: " + newItem.getStatBonus() + ") ?");
        for (int i = 0; i < character.getInventory().getWeaponsSize(); i++) {
            OffensiveStuff weapon = character.getInventory().getWeapon(i);
            System.out.println("[" + (i + 1) + "] Remplacer : " + weapon.getName() + " (PA: " + weapon.getStatBonus() + ")");
        }
        System.out.println("[" + (character.getInventory().getWeaponsSize() + 1) + "] Abandonner");
        System.out.print("Votre choix : ");
    }

    /**
     * Affiche le menu de sélection d'arme/sort avant un combat, avec les options pour choisir une arme/sort dans l'inventaire ou attaquer sans changer d'arme.
     * Affiche également les armes/sorts disponibles dans l'inventaire du personnage et indique quelle arme/sort est actuellement équipée.
     *
     * @param character Le personnage pour lequel le menu de sélection d'arme est affiché, utilisé pour afficher les armes/sorts disponibles dans l'inventaire du personnage et indiquer quelle arme/sort est actuellement équipée.
     */
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

    /**
     * Affiche le menu du marchand avec les options pour acheter, vendre ou partir.
     */
    public void displayMerchantMenu() {
        System.out.println("\n🧙 Que voulez-vous faire ?");
        System.out.println("[1] Acheter");
        System.out.println("[2] Vendre");
        System.out.println("[3] Partir");
        System.out.print("Votre choix : ");
    }

    /**
     * Affiche le menu d'achat du marchand avec la liste dynamique des items disponibles à l'achat, en fonction du type de personnage (Guerrier ou Magicien).
     * Affiche également les prix des items et les options pour revenir au menu précédent.
     *
     * @param character Le personnage pour lequel le menu d'achat est affiché, utilisé pour déterminer les items disponibles à l'achat en fonction du type de personnage.
     * @param merchant  Le marchand auprès duquel le personnage interagit, utilisé pour afficher les prix des items disponibles à l'achat.
     */
    public void displayBuyMenu(Character character, MerchantTile merchant) {
        boolean isWarrior = character instanceof Warrior;
        System.out.println("\n🛒 Que voulez-vous acheter ?");
        System.out.println("[1] Potion Standard (" + merchant.getPriceStandardPotion() + " 🪙)");
        System.out.println("[2] Grande Potion (" + merchant.getPriceBigPotion() + " 🪙)");
        System.out.println("[3] Coup de Tonnerre (" + merchant.getPriceThunderbolt() + " 🪙)");
        if (isWarrior) {
            System.out.println("[4] Masse (" + merchant.getPriceMace() + " 🪙)");
            System.out.println("[5] Épée (" + merchant.getPriceSword() + " 🪙)");
            System.out.println("[6] Arc (" + merchant.getPriceBow() + " 🪙)");
        } else {
            System.out.println("[4] Éclair (" + merchant.getPriceLigthning() + " 🪙)");
            System.out.println("[5] Boule de feu (" + merchant.getPriceFireball() + " 🪙)");
            System.out.println("[6] Invisibilité (" + merchant.getPriceInvisibility() + " 🪙)");
        }
        System.out.println("[7] Retour");
        System.out.print("Votre choix : ");
    }

    /**
     * Affiche le menu de vente du marchand avec la liste dynamique des items disponibles à la vente dans l'inventaire du personnage, en fonction du type de personnage (Guerrier ou Magicien).
     * Affiche également les prix des items et les options pour revenir au menu précédent.
     *
     * @param items  La liste des items disponibles à la vente dans l'inventaire du personnage, utilisée pour afficher les items que le personnage peut vendre.
     * @param prices La liste des prix correspondants aux items disponibles à la vente, utilisée pour afficher les prix des items que le personnage peut vendre.
     */
    public void displaySellMenu(List<Object> items, List<Integer> prices) {
        System.out.println("\n💰 Que voulez-vous vendre ?");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + items.get(i).toString() + " → " + prices.get(i) + " 🪙");
        }
        System.out.println("[" + (items.size() + 1) + "] Retour");
        System.out.print("Votre choix : ");
    }

    /**
     * Affiche le menu de fin de partie avec les options pour recommencer ou quitter.
     */
    public void displayEndGameMenu() {
        System.out.println("\n--- Fin de partie ---");
        System.out.println("[1] Recommencer");
        System.out.println("[2] Quitter");
        System.out.print("Votre choix : ");
    }

    /**
     * Affiche un message personnalisé à l'utilisateur.
     *
     * @param message Le message à afficher, utilisé pour communiquer des informations, des résultats de combat, des interactions avec le marchand, ou tout autre message pertinent pour le joueur.
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Lit une entrée entière de l'utilisateur, en vérifiant que l'entrée est valide.
     * Si l'entrée n'est pas un entier, le menu affiche un message d'erreur et invite l'utilisateur à réessayer jusqu'à ce qu'une entrée valide soit fournie.
     *
     * @return L'entier saisi par l'utilisateur, utilisé pour les choix de menu et les interactions dans le jeu.
     */
    public int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Entrée invalide, réessayez : ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    /**
     * Lit une entrée de chaîne de caractères de l'utilisateur.
     *
     * @return La chaîne de caractères saisie par l'utilisateur, utilisée pour les entrées de texte telles que les noms de personnage ou les réponses aux questions.
     */
    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public String toString() {
        return "MENU PRINCIPAL";
    }
}