package fr.campus.dungeoncrawler.character;

import fr.campus.dungeoncrawler.character.enemy.Enemy;
import fr.campus.dungeoncrawler.exceptions.OutOfBoardException;
import fr.campus.dungeoncrawler.inventory.Inventory;
import fr.campus.dungeoncrawler.stuff.Stuff;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.ProtectionSpell;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.Shield;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;

import java.util.Random;

/**
 * Classe abstraite représentant un personnage du jeu, que ce soit un héros ou un ennemi.
 * Elle définit les propriétés communes à tous les personnages et les méthodes abstraites
 * que les sous-classes doivent implémenter.
 */
public abstract class Character {

    private int id;
    private String type;
    private String name;
    private int lifePoints;
    private int damage;
    private int defense;
    private int position;
    private Stuff offensiveStuff;
    private Stuff defensiveStuff;
    private Inventory inventory;

    /**
     * Constructeur de la classe Character. Initialise les propriétés de base du personnage.
     * @param type Le type de personnage (ex: "Warrior", "Wizard", "Goblin", etc.)
     * @param name le nom du personnage
     * @param lifePoints les points de vie du personnage
     * @param damage le niveau d'attaque du personnage
     */
    public Character(String type, String name, int lifePoints, int damage) {
        this.type = type;
        this.name = name;
        this.lifePoints = lifePoints;
        this.damage = damage;
        this.defense = 0;
        this.position = 0;
        this.offensiveStuff = null;
        this.defensiveStuff = null;
        this.inventory = new Inventory();
    }

    /**
     * Méthode abstraite pour obtenir le label de la statistique spéciale du personnage.
     * Les sous-classes doivent implémenter cette méthode pour fournir une description
     * appropriée de leur statistique spéciale (ex: "Mana", "Agilité", etc.)
     * @return Le label de la statistique spéciale
     */
    public abstract String getSpecialStatLabel();

    /**
     * Méthode abstraite pour obtenir le chemin de l'image représentant le personnage.
     * Les sous-classes doivent implémenter cette méthode pour fournir le chemin de leur image.
     * @return Le chemin de l'image du personnage
     */
    public abstract String getCharacterImage();

    /**
     * Méthode abstraite pour vérifier si le personnage peut équiper un certain type d'équipement.
     * Les sous-classes doivent implémenter cette méthode pour définir les règles d'équipement spécifiques
     * à leur type de personnage.
     * @param stuff L'équipement à vérifier
     * @return true si le personnage peut équiper l'équipement, false sinon
     */
    public abstract boolean canEquip(Stuff stuff);

    /**
     * Méthode pour équiper un objet au personnage. Vérifie d'abord si le personnage peut équiper
     * l'objet en utilisant la méthode canEquip(). Si l'équipement est valide, il est équipé et les
     * statistiques du personnage sont mises à jour en conséquence. Si l'équipement n'est pas valide,
     * un message d'erreur est affiché et la récompense est abandonnée.
     * @param stuff L'équipement à équiper
     */
    public void equip(Stuff stuff) {
        if (!canEquip(stuff)) {
            System.out.println(
                    "    Le " + this.getType() + " ne peuvent pas porter se type d'item : " + stuff.getName()
                    + "\n    Récompense abandonnée !"
            );
            return;
        }
        if (stuff instanceof OffensiveStuff) {
            if (this.getOffensiveStuff() == null || this.getOffensiveStuff().getStatBonus() < stuff.getStatBonus()) {
                this.setOffensiveStuff(stuff);
                this.setAttackLevel(this.getAttackLevel() + stuff.getStatBonus());
                System.out.println("    Équipé : " + stuff);
            } else {
                System.out.println(
                    "    Le niveau d'attaque de " + stuff.getName() + " est inférieur au niveau d'attaque actuel \n"
                    + "    Récompense abandonnée !"
                );
            }
        } else if (stuff instanceof Potion) {
            if (this.getLifeLevel() >= this.getMaxLifeLevel()) {
                System.out.println(
                    "Vous avez déjà tous vos points de vie ! \n"
                    + "Récompense abandonnée !"
                );
            } else {
                this.setLifeLevel(Math.min(getLifeLevel() + stuff.getStatBonus(), getMaxLifeLevel()));
                System.out.println(stuff + " Potion bue ! (PV : " + this.getLifeLevel() + ")");;
            }
        } else if (stuff instanceof Shield || stuff instanceof ProtectionSpell) {
            this.setDefensiveStuff(stuff);
            this.setDefenseLevel(this.getDefenseLevel() + stuff.getStatBonus());
            System.out.println("    Équipé : " + stuff);
        }
    }

    /**
     * Méthode pour déplacer le personnage sur le plateau de jeu. La position du personnage est mise à jour
     * en fonction du nombre de cases à avancer. Si le personnage dépasse la dernière case du plateau,
     * une exception OutOfBoardException est levée et la position du personnage est plafonnée à la dernière case.
     * @param steps Le nombre de cases à avancer
     * @param totalCases Le nombre total de cases sur le plateau de jeu
     * @throws OutOfBoardException Si le personnage dépasse la dernière case du plateau
     */
    public void move(int steps, int totalCases) throws OutOfBoardException {
        this.position += steps;
        if (this.position >= totalCases) {
            this.position = totalCases - 1;
            throw new OutOfBoardException(
                name + " dépasse la dernière case ! Position plafonnée à " + totalCases + "."
            );
        }
    }

    public void reset() {
        this.resetPosition();
        this.setDefenseLevel(0);
        this.setLifeLevel(this.getBaseLifeLevel());
        this.setAttackLevel(this.getBaseAttackLevel());
        this.offensiveStuff = null;
        this.defensiveStuff = null;
        this.inventory = new Inventory();
    }

    public void flee(Enemy enemy) {
        Random random  = new Random();
        int halfDmg = Math.max((enemy.getAttackLevel() - this.defense) / 2, 0);
        this.lifePoints -= halfDmg;

        int recoil = 1 + random.nextInt(3);
        int newPos = Math.max(this.position - recoil, 0);
        this.position  = newPos;

        System.out.println("🏃 " + this.name + " prend la fuite !");
        if (halfDmg > 0) {
            System.out.println("💥 " + enemy.getName() + " inflige " + halfDmg
                    + " dégâts dans le dos. (PV : " + this.lifePoints + ")");
        }
        System.out.println(this.name + " recule à la case " + (newPos + 1) + " !");
    }

    public void attack(Enemy enemy) {
        enemy.setLifeLevel(enemy.getLifeLevel() - this.getAttackLevel());
        System.out.println(
                "\n>>> " + this.getName() + " inflige "
                + this.getAttackLevel() + " points de dégâts à " + enemy.getName()
                + "(" + enemy.getName() + " PV : " + Math.max(enemy.getLifeLevel(), 0) + ")"
        );
    }

    public abstract int getBaseAttackLevel();

    public abstract int getBaseLifeLevel();
    /**
     * Méthode pour réinitialiser la position du personnage à la case de départ (position 0).
     */
    public void resetPosition() {
        this.position = 0;
    }

    /**
     * Getter pour l'identifiant du personnage.
     * @return L'identifiant du personnage
     */
    public int getId() {
        return this.id;
    }

    /**
     * Setter pour l'identifiant du personnage.
     * @param id L'identifiant à attribuer au personnage
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter pour le type du personnage.
     * @return Le type du personnage
     */
    public String getType() { return type; }

    /**
     * Setter pour le type du personnage.
     * @param type Le type à attribuer au personnage
     */
    public void setType(String type) { this.type = type; }

    /**
     * Getter pour le nom du personnage.
     * @return Le nom du personnage
     */
    public String getName() { return name; }

    /**
     * Setter pour le nom du personnage.
     * @param name Le nom à attribuer au personnage
     */
    public void setName(String name) { this.name = name; }

    /**
     * Getter pour les points de vie du personnage.
     * @return Les points de vie du personnage
     */
    public int getLifeLevel() { return lifePoints; }

    /**
     * Setter pour les points de vie du personnage.
     * @param lifePoints Les points de vie à attribuer au personnage
     */
    public void setLifeLevel(int lifePoints) { this.lifePoints = lifePoints; }

    /**
     * Calcule le nombre de points de vie maximum du personnage en fonction de sa classe et de son équipement.
     * Un guerrier a 15 points de vie de base, tandis qu'un magicien en a 12. Si le personnage est équipé d'un bouclier,
     * les points de vie maximum sont augmentés du bonus de ce bouclier. Cette méthode peut être appelée à chaque fois que
     * le personnage équipe ou déséquipe un objet, ou à chaque fois que le personnage subit des dégâts, pour s'assurer que
     * les points de vie actuels ne dépassent pas le maximum.
     * @return Le nombre de points de vie maximum du personnage.
     */
    public abstract int getMaxLifeLevel();

    /**
     * Getter pour le niveau d'attaque du personnage.
     * @return Le niveau d'attaque du personnage
     */
    public int getAttackLevel() { return damage; }

    /**
    * Setter pour le niveau d'attaque du personnage.
    * @param damage Le niveau d'attaque à attribuer au personnage
    */
    public void setAttackLevel(int damage) { this.damage = damage; }

    /**
     * Getter pour le niveau de défense du personnage.
     * @return Le niveau de défense du personnage
     */
    public int getDefenseLevel() { return defense; }

    /**
    * Setter pour le niveau de défense du personnage.
    * @param defense Le niveau de défense à attribuer au personnage
    */
    public void setDefenseLevel(int defense) { this.defense = defense; }

    /**
     * Getter pour la position du personnage sur le plateau de jeu.
     * @return La position du personnage
     */
    public int getPosition() { return position; }

    /**
     * Setter pour la position du personnage sur le plateau de jeu.
     * @param position La position à attribuer au personnage
     */
    public void setPosition(int position) { this.position = position; }

    /**
     * Getter pour l'équipement offensif du personnage.
     * @return L'équipement offensif du personnage
     */
    public Stuff getOffensiveStuff() { return offensiveStuff; }

    /**
     * Setter pour l'équipement offensif du personnage.
     * @param offensiveStuff L'équipement offensif à attribuer au personnage
     */
    public void setOffensiveStuff(Stuff offensiveStuff) { this.offensiveStuff = offensiveStuff; }

    /**
     * Getter pour l'équipement défensif du personnage.
     * @return L'équipement défensif du personnage
     */
    public Stuff getDefensiveStuff() { return defensiveStuff; }

    /**
     * Setter pour l'équipement défensif du personnage.
     * @param defensiveStuff L'équipement défensif à attribuer au personnage
     */
    public void setDefensiveStuff(Stuff defensiveStuff) { this.defensiveStuff = defensiveStuff; }

    /**
     * Méthode pour afficher les informations du personnage de manière lisible.
     * Affiche le nom, les points de vie, le niveau d'attaque, la position sur le plateau,
     * ainsi que les équipements offensifs et défensifs équipés (ou "Aucun" s'il n'y en a pas).
     * @return Une chaîne de caractères représentant les informations du personnage
     */
    @Override
    public String toString() {
        String offensive = (offensiveStuff != null) ? offensiveStuff.toString() : "Aucun";
        String defensive = (defensiveStuff != null) ? defensiveStuff.toString() : "Aucun";
        return "Nom : " + name + "\n"
                + "PV  : " + lifePoints + "\n"
                + getSpecialStatLabel() + "  : " + damage + "\n"
                + "Position : " + (position + 1) + "\n"
                + "Équipement offensif : " + offensive + "\n"
                + "Équipement défensif : " + defensive;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
