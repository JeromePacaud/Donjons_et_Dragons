package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.Character;

/**
 * Représente la tuile de fin du donjon. Lorsque le personnage interagit avec cette tuile, il gagne la partie.
 */
public class EndTile extends Tile {

    /**
     * Constructeur de la tuile de fin.
     * Cette tuile est identifiée par le nom "End" et affiche une icône de torii (⛩️) pour représenter la sortie du donjon.
     */
    public EndTile() {
        super("End");
    }

    /**
     * Retourne l'icône de la tuile de fin.
     * @return une chaîne de caractères représentant l'icône de la tuile de fin.
     */
    @Override
    public String getTileImage() {
        return "⛩️";
    }

    /**
     * Lorsque le personnage interagit avec la tuile de fin, affiche un message de victoire.
     * @param character le personnage qui interagit avec la tuile de fin.
     */
    @Override
    public void interact(Character character) {
        System.out.println("\n=== " + character.getName() + " a atteint la sortie du donjon ! Victoire ! ===");
    }
}
