package fr.campus.dungeoncrawler.stuff;

public class OffensiveStuff extends Stuff {

    private int damage;

    public OffensiveStuff(String name, String type, int damage) {
        super(name, type);
        this.damage = damage;
    }

    public int getAttackLevel() {
        return damage;
    }

    public void setAttackLevel(int damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return super.toString() + " (Attaque : +" + damage + ")";
    }
}
