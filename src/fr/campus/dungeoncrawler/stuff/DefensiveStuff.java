package fr.campus.dungeoncrawler.stuff;

public abstract class DefensiveStuff extends Stuff {

    private int defenseAmount;

    public DefensiveStuff(String name, String type, int defenseAmount) {
        super(name, type);
        this.defenseAmount = defenseAmount;
    }

    @Override
    public int getStatBonus() { return defenseAmount; }

    @Override
    public String getBonusLabel() { return "Défense"; }

    public int getDefenseLevel() { return defenseAmount; }
    public void setDefenseLevel(int defenseAmount) { this.defenseAmount = defenseAmount; }
}
