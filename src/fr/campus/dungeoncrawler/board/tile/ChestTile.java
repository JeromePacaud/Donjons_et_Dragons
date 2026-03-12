package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.menu.Menu;
import fr.campus.dungeoncrawler.stuff.Stuff;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;

/** Tile représentant un coffre contenant une récompense pour le personnage.
 * Lorsque le personnage interagit avec ce tile, il reçoit la récompense s'il peut l'équiper.
 */
public class ChestTile extends Tile {
    private Stuff reward;
    private boolean opened;
    private Menu menu;

    /**
     * Constructeur de ChestTile.
     * @param reward La récompense contenue dans le coffre.
     */
    public ChestTile(Stuff reward) {
        super("Chest");
        this.reward = reward;
        this.opened = false;
        this.menu = new Menu();
    }

    /**
     * Getter pour vérifier si le coffre a déjà été ouvert.
     * @return true si le coffre a été ouvert, false sinon.
     */
    public boolean isOpened() {
        return opened;
    }

    /**
     * Getter pour la récompense contenue dans le coffre.
     * @return La récompense du coffre.
     */
    public Stuff getReward() {
        return reward;
    }

    /**
     * Setter pour la récompense du coffre.
     * @param reward La nouvelle récompense à placer dans le coffre.
     */
    public void setReward(Stuff reward) {
        this.reward = reward;
    }

    /**
     * Retourne une représentation visuelle du coffre.
     * @return Une chaîne de caractères représentant le coffre.
     */
    @Override
    public String getTileImage() {
        return "\uD83D\uDD4B";
    }

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
     * Permet au personnage d'interagir avec le coffre. Si le personnage peut équiper la récompense, il la reçoit.
     * @param character Le personnage qui interagit avec le coffre.
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
