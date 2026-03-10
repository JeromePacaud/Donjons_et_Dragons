package fr.campus.dungeoncrawler.inventory;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class Inventory {

    private  int max_size = 3;
    private List<Potion> potions;

    public Inventory() {
        this.potions = new ArrayList<>();
    }


    public boolean addPotion(Potion potion) {
        if (isFull()) {
            System.out.println("⚠️ Inventaire plein ! Impossible d'ajouter " + potion.getName() + ".");
            return false;
        }
        potions.add(potion);
        System.out.println("\uD83D\uDCBC " + potion.getName() + " ajoutée à l'inventaire ! ("
                + potions.size() + "/" + max_size + ")");
        return true;
    }


    public boolean usePotion(Character character) {
        if (this.isEmpty()) {
            System.out.println("⚠️ Aucune potion dans l'inventaire !");
            return false;
        }
        Potion potion = this.potions.removeFirst();
        int newHp = Math.min(character.getLifeLevel() + potion.getStatBonus(), character.getMaxLifeLevel());
        character.setLifeLevel(newHp);
        System.out.println("🧪  " + character.getName() + " utilise " + potion.getName()
                + " et récupère " + potion.getStatBonus() + " PV. (PV : "
                + character.getLifeLevel() + "/" + character.getMaxLifeLevel() + ")");
        return true;
    }

    public boolean isEmpty() { return potions.isEmpty(); }
    public boolean isFull() { return potions.size() >= max_size; }
    public int getSize() { return potions.size(); }
    public int getMaxSize() { return max_size; }

    public List<Potion> getPotions() { return potions; }
    public void setPotions(List<Potion> potions) { this.potions = potions; }

    @Override
    public String toString() {
        if (isEmpty()) return "Inventaire vide";

        ListIterator<Potion> iterator = potions.listIterator();
        String result = "💼 Inventaire (" + potions.size() + "/" + max_size + ") :\n";

        while (iterator.hasNext()) {
            iterator.next();
            result += "  [" + (iterator.previousIndex()) + "] " + potions.get(iterator.previousIndex()).toString() + "\n";
        }

        return result;
    }
}