package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.Character;

/**
 * Représente une tuile de piège dans le donjon.
 * Lorsque le personnage interagit avec cette tuile, il subit des dégâts.
 */
public class TrapTile extends Tile {

    /**
     * Constructeur de la classe TrapTile.
     * Initialise la tuile avec le nom "Trap".
     */
    public TrapTile() {
        super("Trap");
    }

    /**
     * Retourne l'image de la tuile, qui est une icône représentant un piège.
     * @return L'image de la tuile.
     */
    @Override
    public String getTileImage() {
        return "\uD83D\uDD78\uFE0F";
    }

    /**
     * Permet au personnage d'interagir avec cette tuile, ce qui lui inflige des dégâts.
     * Affiche un message indiquant que le personnage a déclenché un piège et les dégâts subis.
     * @param character Le personnage qui interagit avec cette tuile.
     */
    @Override
    public void interact(Character character) {
        int damage = 2;
        System.out.println(
                ">>> PIÈGE ! Vous perdez " + damage
                + " PV. (PV : "
                + (character.getLifeLevel() - damage)
                + ")"
        );
    }
}
