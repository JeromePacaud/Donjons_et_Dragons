package fr.campus.dungeoncrawler.board;

public class Tile {

    public enum Type {
        EMPTY, START, END, TRAP, CHEST, ENEMY
    }

    private Type type;

    public Tile(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Représentation visuelle de la case dans le terminal.
     * Le joueur est affiché séparément via Board.display().
     */
    @Override
    public String toString() {
        return switch (type) {
            case START -> "\uD83C\uDFF0";
            case TRAP -> "\uD83D\uDD78\uFE0F";
            case CHEST -> "\uD83D\uDD4B";
            case ENEMY -> "\uD83D\uDC32";
            case END -> "\uD80C\uDE78";
            default -> "\uD83D\uDFEB";
        };
    }
}
