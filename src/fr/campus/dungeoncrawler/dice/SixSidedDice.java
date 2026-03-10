package fr.campus.dungeoncrawler.dice;

import java.util.Random;

public class SixSidedDice implements Dice {

    private int faces;
    private Random random;

    public SixSidedDice() {
        this.faces  = 6;
        this.random = new Random();
    }

    public int roll() {
        return random.nextInt(faces) + 1;
    }

    public int getFaces() { return faces; }
    public void setFaces(int faces) { this.faces = faces; }

    @Override
    public String toString() {
        return "\uD83C\uDFB2 " + faces + " faces";
    }
}
