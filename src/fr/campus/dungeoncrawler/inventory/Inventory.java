package fr.campus.dungeoncrawler.inventory;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.Thunderbolt;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Inventory {

    private int max_potions = 3;
    private int max_weapons = 2;

    private List<Potion> potions;
    private List<OffensiveStuff> weapons;

    public Inventory() {
        this.potions = new ArrayList<>();
        this.weapons = new ArrayList<>();
    }

    public boolean addPotion(Potion potion) {
        if (isPotionsFull()) {
            System.out.println(">>> ⚠️ Inventaire plein ! Impossible d'ajouter " + potion.getName() + ".");
            return false;
        }
        potions.add(potion);
        System.out.println(">>> 💼 " + potion.getName() + " ajoutée à l'inventaire ! (" + potions.size() + "/" + max_potions + ")");
        return true;
    }

    public void addPotionSilent(Potion potion) {
        if (isPotionsFull()) return;
        potions.add(potion);
    }

    /**
     * Utilise la première potion disponible.
     * - Thunderbolt → active le buff thunderActive sur le personnage
     * - Autres potions → soigne le personnage normalement
     */
    public void usePotion(Character character) {
        if (isPotionsEmpty()) {
            System.out.println(">>> ⚠️ Aucune potion dans l'inventaire !");
            return;
        }
        Potion potion = potions.removeFirst();

        if (potion instanceof Thunderbolt) {
            character.setThunderActive(true);
            System.out.println(">>> ⚡ " + character.getName() + " utilise Coup de Tonnerre ! Dégâts doublés pour le prochain combat !");
        } else {
            int newHp = Math.min(character.getLifeLevel() + potion.getStatBonus(), character.getMaxLifeLevel());
            character.setLifeLevel(newHp);
            System.out.println(">>> 🧪 " + character.getName() + " utilise " + potion.getName()
                    + " et récupère " + potion.getStatBonus() + " PV. (PV : "
                    + character.getLifeLevel() + "/" + character.getMaxLifeLevel() + ")");
        }
    }

    public boolean addWeapon(OffensiveStuff weapon) {
        if (isWeaponsFull()) return false;
        weapons.add(weapon);
        System.out.println(">>> ⚔️ " + weapon.getName() + " ajouté à l'inventaire ! (" + weapons.size() + "/" + max_weapons + ")");
        return true;
    }

    public void addWeaponSilent(OffensiveStuff weapon) {
        if (isWeaponsFull()) return;
        weapons.add(weapon);
    }

    public void replaceWeapon(int index, OffensiveStuff weapon) {
        if (index >= 0 && index < weapons.size()) {
            weapons.set(index, weapon);
            System.out.println(">>> 🔄 " + weapon.getName() + " remplace l'ancien équipement !");
        }
    }

    public OffensiveStuff getWeapon(int index) {
        if (index >= 0 && index < weapons.size()) return weapons.get(index);
        return null;
    }

    public void clear() {
        potions.clear();
        weapons.clear();
    }

    public boolean isPotionsEmpty() { return potions.isEmpty(); }
    public boolean isPotionsFull() { return potions.size() >= max_potions; }
    public int getPotionsSize() { return potions.size(); }
    public int getMaxPotions() { return max_potions; }
    public List<Potion> getPotions() { return potions; }
    public void setPotions(List<Potion> p) { this.potions = p; }
    public boolean isWeaponsEmpty() { return weapons.isEmpty(); }
    public boolean isWeaponsFull() { return weapons.size() >= max_weapons; }
    public int getWeaponsSize() { return weapons.size(); }
    public int getMaxWeapons() { return max_weapons; }
    public List<OffensiveStuff> getWeapons() { return weapons; }
    public void setWeapons(List<OffensiveStuff> w) { this.weapons = w; }
    public boolean isEmpty() { return isPotionsEmpty(); }
    public boolean isFull() { return isPotionsFull(); }
    public int getSize() { return getPotionsSize(); }
    public int getMaxSize() { return max_potions; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("💼 Potions (").append(potions.size()).append("/").append(max_potions).append(") :\n");
        if (potions.isEmpty()) {
            sb.append("  Aucune potion\n");
        } else {
            ListIterator<Potion> it = potions.listIterator();
            while (it.hasNext()) {
                int i = it.nextIndex();
                sb.append("  [").append(i + 1).append("] ").append(it.next().toString()).append("\n");
            }
        }
        sb.append("⚔️ Armes/Sorts (").append(weapons.size()).append("/").append(max_weapons).append(") :\n");
        if (weapons.isEmpty()) {
            sb.append("  Aucun équipement offensif\n");
        } else {
            for (int i = 0; i < weapons.size(); i++) {
                sb.append("  [").append(i + 1).append("] ").append(weapons.get(i).toString()).append("\n");
            }
        }
        return sb.toString();
    }
}