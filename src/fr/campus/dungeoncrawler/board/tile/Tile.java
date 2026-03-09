package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.Character;

/**
 * Classe abstraite représentant une tuile du donjon.
 * Chaque tuile a un type et une image associée, et permet au personnage d'interagir avec elle.
 */
public abstract class Tile {
    /*
    * TODO : Ajouter une propriété static pour stocker les images des tuiles,
    *        afin d'éviter de les charger à chaque fois que la méthode getTileImage est appelée.
    */

    private String type;

    /**
     * Constructeur de la classe Tile.
     * @param type Le type de la tuile (ex: "Start", "Enemy", "Treasure", etc.).
     */
    public Tile(String type) {
        this.type = type;
    }

    /**
     *  Getter pour le type de la tuile.
     * @return Le type de la tuile.
     */
    public String getType() {
        return this.type;
    }

    /**
    * Setter pour le type de la tuile.
    * @param type Le type de la tuile à définir.
    */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Méthode abstraite pour obtenir l'image de la tuile.
     * Chaque type de tuile doit implémenter cette méthode pour retourner son image spécifique.
     * @return L'image de la tuile.
     */
    public abstract String getTileImage();

    /**
     * Méthode abstraite pour permettre au personnage d'interagir avec la tuile.
     * Chaque type de tuile doit implémenter cette méthode pour définir son comportement spécifique lors de l'interaction.
     * @param character Le personnage qui interagit avec la tuile.
     */
    public abstract void interact(Character character);

    /**
     * Redéfinit la méthode toString pour retourner l'image de la tuile.
     * Cela permet d'afficher facilement la tuile dans la console ou dans une interface utilisateur.
     * @return L'image de la tuile.
     */
    @Override
    public String toString() {
        return getTileImage();
    }
}
