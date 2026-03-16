package fr.campus.dungeoncrawler.inventory;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.Thunderbolt;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Classe responsable de la gestion de l'inventaire du personnage dans le jeu Dungeon Crawler.
 * <p>
 * Gère l'inventaire du personnage, qui peut contenir des potions et des armes/sorts offensifs.
 * </p>
 * <ul>
 *     <li>Potions : max 3, utilisées pour soigner ou activer des buffs (ex: Thunderbolt)</li>
 *     <li>Armes/Sorts : max 2, utilisés pour les combats</li>
 * </ul>
 * <p>
 * Fonctionnalités :
 * </p>
 * <ul>
 *     <li>Ajouter une potion ou une arme (avec vérification de la capacité)</li>
 *     <li>Utiliser une potion (applique ses effets au personnage)</li>
 *     <li>Remplacer une arme dans l'inventaire</li>
 *     <li>Afficher le contenu de l'inventaire</li>
 * </ul>
 */
public class Inventory {

    private int max_potions = 3;
    private int max_weapons = 2;

    private List<Potion> potions;
    private List<OffensiveStuff> weapons;

    /**
     * Constructeur de la classe Inventory, initialisant les listes de potions et d'armes/sorts offensifs.
     */
    public Inventory() {
        this.potions = new ArrayList<>();
        this.weapons = new ArrayList<>();
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

    /**
     * Ajoute une potion à l'inventaire si la capacité n'est pas atteinte.
     * Affiche un message de succès ou d'erreur selon le résultat.
     *
     * @param potion La potion à ajouter
     * @return true si la potion a été ajoutée, false si l'inventaire est plein
     */
    public boolean addPotion(Potion potion) {
        if (isPotionsFull()) {
            System.out.println(">>> ⚠️ Inventaire plein ! Impossible d'ajouter " + potion.getName() + ".");
            return false;
        }
        potions.add(potion);
        System.out.println(">>> 💼 " + potion.getName() + " ajoutée à l'inventaire ! (" + potions.size() + "/" + max_potions + ")");
        return true;
    }

    /**
     * Ajoute une potion à l'inventaire sans afficher de message.
     * Utilisé pour les opérations internes où le feedback n'est pas nécessaire.
     *
     * @param potion La potion à ajouter
     */
    public void addPotionSilent(Potion potion) {
        if (isPotionsFull()) return;
        potions.add(potion);
    }

    /**
     * Utilise la première potion de l'inventaire sur le personnage.
     * Si la potion est un Thunderbolt, active le buff de coup de tonnerre.
     * Sinon, soigne le personnage en fonction du bonus de la potion.
     * Affiche les effets appliqués et les PV actuels du personnage.
     *
     * @param character Le personnage sur lequel la potion est utilisée
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

    /**
     * Ajoute une arme ou un sort offensif à l'inventaire si la capacité n'est pas atteinte.
     * Affiche un message de succès ou d'erreur selon le résultat.
     *
     * @param weapon L'arme ou sort offensif à ajouter
     * @return true si l'arme a été ajoutée, false si l'inventaire est plein
     */
    public boolean addWeapon(OffensiveStuff weapon) {
        if (isWeaponsFull()) return false;
        weapons.add(weapon);
        System.out.println(">>> ⚔️ " + weapon.getName() + " ajouté à l'inventaire ! (" + weapons.size() + "/" + max_weapons + ")");
        return true;
    }

    /**
     * Ajoute une arme ou un sort offensif à l'inventaire sans afficher de message.
     * Utilisé pour les opérations internes où le feedback n'est pas nécessaire.
     *
     * @param weapon L'arme ou sort offensif à ajouter
     */
    public void addWeaponSilent(OffensiveStuff weapon) {
        if (isWeaponsFull()) return;
        weapons.add(weapon);
    }

    /**
     * Remplace une arme ou un sort offensif à l'index spécifié dans l'inventaire.
     * Affiche un message de confirmation du remplacement.
     *
     * @param index  L'index de l'arme à remplacer (0-based)
     * @param weapon La nouvelle arme ou sort offensif à mettre à la place
     */
    public void replaceWeapon(int index, OffensiveStuff weapon) {
        if (index >= 0 && index < weapons.size()) {
            weapons.set(index, weapon);
            System.out.println(">>> 🔄 " + weapon.getName() + " remplace l'ancien équipement !");
        }
    }

    /**
     * Récupère l'arme ou sort offensif à l'index spécifié dans l'inventaire.
     *
     * @param index L'index de l'arme à récupérer (0-based)
     * @return L'arme ou sort offensif à l'index, ou null si l'index est invalide
     */
    public OffensiveStuff getWeapon(int index) {
        if (index >= 0 && index < weapons.size()) return weapons.get(index);
        return null;
    }

    /**
     * Vide l'inventaire en supprimant toutes les potions et armes/sorts offensifs.
     * Utilisé pour réinitialiser l'inventaire du personnage.
     */
    public void clear() {
        potions.clear();
        weapons.clear();
    }

    /**
     * Retourne une représentation textuelle de l'inventaire, affichant les potions et les armes/sorts offensifs.
     * Affiche le nombre d'objets dans chaque catégorie et leurs détails.
     *
     * @return Une chaîne de caractères représentant le contenu de l'inventaire
     */
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