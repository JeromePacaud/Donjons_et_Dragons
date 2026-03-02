package fr.campus.dungeoncrawler.board;

import fr.campus.dungeoncrawler.character.Character;

public class Board {

    private int totalCases;

    public Board() {
        this.totalCases = 64;
    }

    public boolean isFinished(Character character) {
        return character.getPosition() >= totalCases;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    @Override
    public String toString() {
        return "Plateau de " + totalCases + " cases";
    }
}
