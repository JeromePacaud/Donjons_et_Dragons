package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.menu.Menu;
import fr.campus.dungeoncrawler.stuff.Stuff;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;

/**
 * La classe ChestTile représente une tuile de coffre dans le jeu.
 * Elle contient une récompense que le personnage peut obtenir en interagissant avec elle.
 */
public class ChestTile extends Tile {
    private Stuff reward;
    private boolean opened;
    private Menu menu;

    /**
     * Constructeur de la classe ChestTile.
     * @param reward La récompense contenue dans le coffre.
     */
    public ChestTile(Stuff reward) {
        super("Chest");
        this.reward = reward;
        this.opened = false;
        this.menu = new Menu();
    }

    /**
     * Retourne la récompense contenue dans le coffre.
     *
     * @return reward
     */
    public Stuff getReward() {
        return reward;
    }

    /**
     * Retourne true si le coffre a été ouvert, false sinon.
     *
     * @return opened
     */
    public boolean isOpened() {
        return opened;
    }

    /**
     * Retourne l'image représentant la tuile de coffre.
     *
     * @return une chaîne de caractères représentant l'image du coffre
     */
    @Override
    public String getTileImage() {
        return "\uD83D\uDD4B";
    }

    /**
     * Gère l'interaction du personnage avec la tuile de coffre.
     * Si le coffre est déjà ouvert, affiche un message d'avertissement.
     * Sinon, affiche la récompense trouvée et gère son acquisition en fonction de son type.
     *
     * @param character Le personnage qui interagit avec le coffre
     */
    private void handlePotion(Potion potion, Character character) {
        if (character.getInventory().isPotionsFull()) {
            System.out.println(">>> ⚠️ Inventaire plein de potions ! La potion est abandonnée.");
            return;
        }

        if (character.getLifeLevel() < character.getMaxLifeLevel()) {
            menu.displayPotionsMenu(potion);
            int choice = menu.readInt();

            if (choice == 1) {
                int newHp = Math.min(character.getLifeLevel() + potion.getStatBonus(), character.getMaxLifeLevel());
                character.setLifeLevel(newHp);
                System.out.println(">>> 🧪 Potion bue ! (PV : " + character.getLifeLevel() + "/" + character.getMaxLifeLevel() + ")");
                return;
            }
        } else {
            System.out.println(">>> ⚠️ PV déjà au maximum ! La potion est ajoutée à l'inventaire.");
        }
        character.getInventory().addPotion(potion);
    }

    /**
     * Gère l'acquisition d'un équipement offensif trouvé dans le coffre.
     * Si l'inventaire du personnage n'est pas plein, ajoute l'équipement et l'équipe si c'est le premier.
     * Sinon, affiche un menu de remplacement pour choisir une arme à remplacer ou abandonner l'équipement.
     *
     * @param stuff L'équipement offensif trouvé
     * @param character Le personnage qui interagit avec le coffre
     */
    private void handleOffensiveEquipment(OffensiveStuff stuff, Character character) {
        if (!character.getInventory().isWeaponsFull()) {
            character.getInventory().addWeapon(stuff);

            if (character.getInventory().getWeaponsSize() == 1) {
                character.equip(stuff);
            }
            return;
        }

        menu.displayWeaponReplaceMenu(character, stuff);
        int choice = menu.readInt();

        if (choice >= 1 && choice <= character.getInventory().getWeaponsSize()) {
            character.getInventory().replaceWeapon(choice - 1, stuff);
        } else {
            System.out.println(">>> ❌ " + stuff.getName() + " abandonné !");
        }
    }

    /**
     * Gère l'interaction du personnage avec la tuile de coffre.
     * Si le coffre est déjà ouvert, affiche un message d'avertissement.
     * Sinon, affiche la récompense trouvée et gère son acquisition en fonction de son type.
     *
     * @param character Le personnage qui interagit avec le coffre
     */
    @Override
    public void interact(Character character) {
        System.out.println("\n>>> \uD83D\uDD4B COFFRE ! Vous trouvez : " + reward);

        if (!character.canEquip(reward)) {
            System.out.println(">>> ❌ Votre classe ne peut pas utiliser : " + reward.getName() + ". Abandonné !");
            opened = true;
            return;
        }

        if (reward instanceof Potion potion) {
            handlePotion(potion, character);
        } else if (reward instanceof OffensiveStuff offensiveStuff) {
            handleOffensiveEquipment(offensiveStuff, character);
        } else {
            character.equip(reward);
        }

        opened = true;
    }
}
