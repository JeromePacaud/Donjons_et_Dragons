package fr.campus.dungeoncrawler.stuff;

public abstract class OffensiveStuff extends Stuff {

    private int damage;

    public OffensiveStuff(String name, String type, int damage) {
        super(name, type);
        this.damage = damage;
    }

    @Override
    public int getStatBonus() { return damage; }

    @Override
    public String getBonusLabel() { return "Attaque"; }

    public int getAttackLevel() { return damage; }
    public void setAttackLevel(int damage) { this.damage = damage; }
}
