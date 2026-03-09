package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.stuff.Stuff;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;

/** Tile représentant un coffre contenant une récompense pour le personnage.
 * Lorsque le personnage interagit avec ce tile, il reçoit la récompense s'il peut l'équiper.
 */
public class ChestTile extends Tile {
    private Stuff reward;
    boolean opened;

    /**
     * Constructeur de ChestTile.
     * @param reward La récompense contenue dans le coffre.
     */
    public ChestTile(Stuff reward) {
        super("Chest");
        this.reward = reward;
        this.opened = false;
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

    /**
     * Permet au personnage d'interagir avec le coffre. Si le personnage peut équiper la récompense, il la reçoit.
     * @param character Le personnage qui interagit avec le coffre.
     */
    @Override
    public void interact(Character character) {
        System.out.println("\n>>> \uD83D\uDD4B COFFRE ! Vous trouvez : " + reward);

        if (reward instanceof Potion potion) {
            if (character.getInventory().isFull()) {
                System.out.println("    ⚠️ Inventaire plein ! La potion est abandonnée.");
            } else {
                character.getInventory().addPotion(potion);
            }
        } else {
            // Armes, sorts, boucliers — géré par character.equip()
            character.equip(reward);
        }

        opened = true;
    }
}
