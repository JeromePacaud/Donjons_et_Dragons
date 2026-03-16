package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.menu.Menu;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.BigPotion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.StandardPotion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.Thunderbolt;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;
import fr.campus.dungeoncrawler.stuff.offensivestuff.armory.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tuile représentant un marchand.
 * Le joueur peut acheter des potions/armes et vendre ses items.
 * Le marchand se déplace entre les parties.
 */
public class MerchantTile extends Tile {

    private Menu menu;

    private int priceStandardPotion = 5;
    private int priceBigPotion = 10;
    private int priceThunderbolt = 15;
    private int priceMace = 8;
    private int priceSword = 12;
    private int priceBow = 15;
    private int priceLigthning = 8;
    private int priceFireball = 12;
    private int priceInvisibility = 15;

    private int sellStandardPotion = 2;
    private int sellBigPotion = 5;
    private int sellThunderbolt = 7;
    private int sellWeaponBase = 4;
    private int sellWeaponMid = 6;
    private int sellWeaponHigh = 8;

    public MerchantTile() {
        super("Merchant");
        this.menu = new Menu();
    }

    public int getPriceStandardPotion() {
        return priceStandardPotion;
    }

    public void setPriceStandardPotion(int priceStandardPotion) {
        this.priceStandardPotion = priceStandardPotion;
    }

    public int getPriceBigPotion() {
        return priceBigPotion;
    }

    public void setPriceBigPotion(int priceBigPotion) {
        this.priceBigPotion = priceBigPotion;
    }

    public int getPriceThunderbolt() {
        return priceThunderbolt;
    }

    public void setPriceThunderbolt(int priceThunderbolt) {
        this.priceThunderbolt = priceThunderbolt;
    }

    public int getPriceMace() {
        return priceMace;
    }

    public void setPriceMace(int priceMace) {
        this.priceMace = priceMace;
    }

    public int getPriceSword() {
        return priceSword;
    }

    public void setPriceSword(int priceSword) {
        this.priceSword = priceSword;
    }

    public int getPriceBow() {
        return priceBow;
    }

    public void setPriceBow(int priceBow) {
        this.priceBow = priceBow;
    }

    public int getPriceLigthning() {
        return priceLigthning;
    }

    public void setPriceLigthning(int priceLigthning) {
        this.priceLigthning = priceLigthning;
    }

    public int getPriceFireball() {
        return priceFireball;
    }

    public void setPriceFireball(int priceFireball) {
        this.priceFireball = priceFireball;
    }

    public int getPriceInvisibility() {
        return priceInvisibility;
    }

    public void setPriceInvisibility(int priceInvisibility) {
        this.priceInvisibility = priceInvisibility;
    }

    public int getSellStandardPotion() {
        return sellStandardPotion;
    }

    public void setSellStandardPotion(int sellStandardPotion) {
        this.sellStandardPotion = sellStandardPotion;
    }

    public int getSellBigPotion() {
        return sellBigPotion;
    }

    public void setSellBigPotion(int sellBigPotion) {
        this.sellBigPotion = sellBigPotion;
    }

    public int getSellThunderbolt() {
        return sellThunderbolt;
    }

    public void setSellThunderbolt(int sellThunderbolt) {
        this.sellThunderbolt = sellThunderbolt;
    }

    public int getSellWeaponBase() {
        return sellWeaponBase;
    }

    public void setSellWeaponBase(int sellWeaponBase) {
        this.sellWeaponBase = sellWeaponBase;
    }

    public int getSellWeaponMid() {
        return sellWeaponMid;
    }

    public void setSellWeaponMid(int sellWeaponMid) {
        this.sellWeaponMid = sellWeaponMid;
    }

    public int getSellWeaponHigh() {
        return sellWeaponHigh;
    }

    public void setSellWeaponHigh(int sellWeaponHigh) {
        this.sellWeaponHigh = sellWeaponHigh;
    }

    @Override
    public String getTileImage() { return "🧙"; }

    /**
     * Permet au joueur d'interagir avec le marchand pour acheter ou vendre des items.
     * Affiche les options disponibles et gère les choix du joueur.
     *
     * @param character Le personnage qui interagit avec le marchand
     */
    @Override
    public void interact(Character character) {
        System.out.println("\n>>> 🧙 Un marchand ! Bonjour aventurier !");
        System.out.println(">>> Votre bourse : " + character.getGold() + " 🪙");

        boolean inShop = true;
        while (inShop) {
            menu.displayMerchantMenu();
            int choice = menu.readInt();

            switch (choice) {
                case 1 -> handleBuy(character);
                case 2 -> handleSell(character);
                case 3 -> {
                    System.out.println(">>> À bientôt aventurier !");
                    inShop = false;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    /**
     * Gère le processus d'achat du marchand.
     * Affiche les items disponibles à l'achat et leurs prix, puis gère le choix du joueur.
     *
     * @param character Le personnage qui interagit avec le marchand
     */
    private void handleBuy(Character character) {
        System.out.println("\n>>> 💰 Bourse : " + character.getGold() + " 🪙");
        menu.displayBuyMenu(character, this);
        int choice = menu.readInt();

        switch (choice) {
            case 1 -> buyPotion(character, new StandardPotion(), priceStandardPotion);
            case 2 -> buyPotion(character, new BigPotion(), priceBigPotion);
            case 3 -> buyPotion(character, new Thunderbolt(), priceThunderbolt);
            case 4 -> buyWeapon(character, character instanceof Warrior ? new Mace() : new Lightning(),
                    character instanceof Warrior ? priceMace : priceLigthning);
            case 5 -> buyWeapon(character, character instanceof Warrior ? new Sword() : new Fireball(),
                    character instanceof Warrior ? priceSword : priceFireball);
            case 6 -> buyWeapon(character, character instanceof Warrior ? new Bow() : new Invisibility(),
                    character instanceof Warrior ? priceBow : priceInvisibility);
            case 7 -> System.out.println(">>> Retour.");
            default -> System.out.println("Choix invalide.");
        }
    }

    /**
     * Tente d'acheter une potion pour le personnage.
     * Vérifie si le personnage a assez d'or, puis tente de l'ajouter à l'inventaire.
     * Si l'inventaire est plein, rembourse le personnage.
     *
     * @param character Le personnage qui achète la potion
     * @param potion La potion à acheter
     * @param price Le prix de la potion
     */
    private void buyPotion(Character character, Potion potion, int price) {
        if (!character.spendGold(price)) return;
        if (!character.getInventory().addPotion(potion)) {
            character.setGold(character.getGold() + price);
            System.out.println(">>> Inventaire plein ! Vous avez été remboursé.");
        }
    }

    /**
     * Tente d'acheter une arme pour le personnage.
     * Vérifie si le personnage peut équiper l'arme, puis si il a assez d'or.
     * Si l'inventaire est plein, propose de remplacer une arme ou d'annuler l'achat.
     *
     * @param character Le personnage qui achète l'arme
     * @param weapon L'arme à acheter
     * @param price Le prix de l'arme
     */
    private void buyWeapon(Character character, OffensiveStuff weapon, int price) {
        if (!character.canEquip(weapon)) {
            System.out.println(">>> Votre classe ne peut pas utiliser : " + weapon.getName());
            return;
        }
        if (!character.spendGold(price)) return;
        if (!character.getInventory().addWeapon(weapon)) {
            menu.displayWeaponReplaceMenu(character, weapon);
            int replaceChoice = menu.readInt();
            if (replaceChoice >= 1 && replaceChoice <= character.getInventory().getWeaponsSize()) {
                character.getInventory().replaceWeapon(replaceChoice - 1, weapon);
            } else {
                character.setGold(character.getGold() + price);
                System.out.println(">>> Achat annulé, vous avez été remboursé.");
            }
        }
    }

    /**
     * Gère le processus de vente du marchand.
     * Affiche les items que le personnage peut vendre et leurs prix, puis gère le choix du joueur.
     *
     * @param character Le personnage qui interagit avec le marchand
     */
    private void handleSell(Character character) {
        List<Object> sellableItems = new ArrayList<>();
        List<Integer> sellPrices   = new ArrayList<>();

        for (Potion p : character.getInventory().getPotions()) {
            sellableItems.add(p);
            sellPrices.add(getSellPrice(p));
        }

        for (OffensiveStuff w : character.getInventory().getWeapons()) {
            sellableItems.add(w);
            sellPrices.add(getSellPrice(w));
        }

        if (sellableItems.isEmpty()) {
            System.out.println(">>> Vous n'avez rien à vendre !");
            return;
        }

        menu.displaySellMenu(sellableItems, sellPrices);
        int choice = menu.readInt();

        if (choice == sellableItems.size() + 1) {
            System.out.println(">>> Retour.");
            return;
        }

        if (choice >= 1 && choice <= sellableItems.size()) {
            Object item = sellableItems.get(choice - 1);
            int price   = sellPrices.get(choice - 1);

            if (item instanceof Potion potion) {
                character.getInventory().getPotions().remove(potion);
            } else if (item instanceof OffensiveStuff weapon) {
                character.getInventory().getWeapons().remove(weapon);
                if (weapon.equals(character.getOffensiveStuff())) {
                    character.setOffensiveStuff(null);
                    character.setAttackLevel(character.getBaseAttackLevel());
                    System.out.println(">>> Arme équipée vendue, retour à l'attaque de base.");
                }
            }

            character.addGold(price);
            System.out.println(">>> Vendu !");
        } else {
            System.out.println("Choix invalide.");
        }
    }

    /**
     * Retourne le prix de vente d'un item en fonction de son type.
     * Les potions ont des prix fixes, tandis que les armes sont classées en trois catégories de prix.
     *
     * @param item L'item dont on veut connaître le prix de vente
     * @return Le prix de vente de l'item
     */
    private int getSellPrice(Object item) {
        if (item instanceof Thunderbolt) return sellThunderbolt;
        if (item instanceof BigPotion) return sellBigPotion;
        if (item instanceof StandardPotion) return sellStandardPotion;
        if (item instanceof Bow || item instanceof Invisibility) return sellWeaponHigh;
        if (item instanceof Sword || item instanceof Fireball) return sellWeaponMid;
        return sellWeaponBase;
    }
}