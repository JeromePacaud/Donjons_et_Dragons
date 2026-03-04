package fr.campus.dungeoncrawler.dice;

import java.util.Random;

public class Dice {

    private int faces;
    private Random random;

    public Dice(int faces) {
        this.faces  = faces;
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
