package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.Character;

/**
 * Représente la tuile de départ du donjon.
 * Cette tuile est le point de départ de l'aventure pour le personnage.
 */
public class StartTile extends Tile {

    /**
     * Constructeur de la classe StartTile.
     * Initialise la tuile avec le nom "Start".
     */
    public StartTile() {
        super("Start");
    }

    /**
     * Retourne l'image de la tuile, qui est une icône représentant un château.
     * @return L'image de la tuile.
     */
    @Override
    public String getTileImage() {
        return "\uD83C\uDFF0";
    }

    /**
     * Permet au personnage d'interagir avec cette tuile, ce qui déclenche le début de l'aventure.
     * Affiche un message de bienvenue et indique que le personnage entre dans le donjon.
     * @param character Le personnage qui interagit avec cette tuile.
     */
    @Override
    public void interact(Character character) {
        System.out.println(">>> Début de l'Aventure !\n" + character.getName() + " entre de dans le Donjon...");
    }
}
