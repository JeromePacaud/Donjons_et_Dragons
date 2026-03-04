package fr.campus.dungeoncrawler.stuff;

public abstract class Stuff {

    private String name;
    private String type;

    public Stuff(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Retourne le bonus de statistique apporté par l'équipement.
     * Chaque sous-classe définit ce que cela signifie (attaque, défense, soin, etc.).
     */
    public abstract int getStatBonus();

    /**
     * Retourne une description textuelle du type de bonus (ex: "Attaque", "Défense").
     */
    public abstract String getBonusLabel();

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return type + " : " + name + " (" + getBonusLabel() + " : +" + getStatBonus() + ")";
    }
}