package fr.campus.dungeoncrawler.dice;

import java.util.Random;

/**
 * Classe représentant un dé à six faces. Elle implémente l'interface Dice et fournit une méthode pour lancer le dé et obtenir un résultat aléatoire entre 1 et 6.
 */
public class SixSidedDice implements Dice {

    private int faces;
    private Random random;

    public SixSidedDice() {
        this.faces  = 6;
        this.random = new Random();
    }

    public int getFaces() { return faces; }
    public void setFaces(int faces) { this.faces = faces; }

    /**
     * Lance le dé et retourne un résultat aléatoire entre 1 et le nombre de faces du dé (inclus).
     * @return un entier représentant le résultat du lancer de dé.
     */
    public int roll() {
        return random.nextInt(faces) + 1;
    }

    @Override
    public String toString() {
        return "\uD83C\uDFB2 " + faces + " faces";
    }
}
