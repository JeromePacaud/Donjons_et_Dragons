package fr.campus.dungeoncrawler.stuff;

public class DefensiveStuff extends Stuff {

    private int defenseAmount;

    public DefensiveStuff(String name, String type, int defenseAmount) {
        super(name, type);
        this.defenseAmount = defenseAmount;
    }

    public int getDefenseLevel() {
        return defenseAmount;
    }

    public void setDefenseLevel(int defenseAmount) {
        this.defenseAmount = defenseAmount;
    }

    @Override
    public String toString() {
        return super.toString() + " (Défense : +" + defenseAmount + ")";
    }
}
