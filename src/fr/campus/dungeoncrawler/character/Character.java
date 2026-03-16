package fr.campus.dungeoncrawler.character;

import fr.campus.dungeoncrawler.character.enemy.Enemy;
import fr.campus.dungeoncrawler.dice.TwentySidedDice;
import fr.campus.dungeoncrawler.exceptions.OutOfBoardException;
import fr.campus.dungeoncrawler.inventory.Inventory;
import fr.campus.dungeoncrawler.stuff.Stuff;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.ProtectionSpell;
import fr.campus.dungeoncrawler.stuff.defensivestuff.defense.Shield;
import fr.campus.dungeoncrawler.stuff.defensivestuff.healing.Potion;
import fr.campus.dungeoncrawler.stuff.offensivestuff.OffensiveStuff;

import java.util.Random;

public abstract class Character {

    private int id;
    private String type;
    private String name;
    private int lifePoints;
    private int damage;
    private int defense;
    private int position;
    private int gold;
    private boolean thunderActive;
    private Stuff offensiveStuff;
    private Stuff defensiveStuff;
    private Inventory inventory;
    private TwentySidedDice dice;

    public Character(String type, String name, int lifePoints, int damage) {
        this.type = type;
        this.name = name;
        this.lifePoints = lifePoints;
        this.damage = damage;
        this.defense = 0;
        this.position = 0;
        this.gold = 0;
        this.thunderActive = false;
        this.offensiveStuff = null;
        this.defensiveStuff = null;
        this.inventory = new Inventory();
        this.dice = new TwentySidedDice();
    }

    public abstract String getSpecialStatLabel();
    public abstract String getCharacterImage();
    public abstract boolean canEquip(Stuff stuff);
    public abstract int getBaseAttackLevel();
    public abstract int getBaseLifeLevel();
    public abstract int getMaxLifeLevel();

    public void equip(Stuff stuff) {
        if (!canEquip(stuff)) {
            System.out.println(">>> Le " + this.getType() + " ne peut pas porter ce type d'item : " + stuff.getName() + "\nRécompense abandonnée !");
            return;
        }
        if (stuff instanceof OffensiveStuff) {
            if (this.getOffensiveStuff() == null || this.getOffensiveStuff().getStatBonus() < stuff.getStatBonus()) {
                this.setOffensiveStuff(stuff);
                this.setAttackLevel(this.getBaseAttackLevel() + stuff.getStatBonus());
                System.out.println(">>> Équipé : " + stuff);
            } else {
                System.out.println(">>> Le niveau d'attaque de " + stuff.getName() + " est inférieur au niveau d'attaque actuel\nRécompense abandonnée !");
            }
        } else if (stuff instanceof Potion) {
            if (this.getLifeLevel() >= this.getMaxLifeLevel()) {
                System.out.println("Vous avez déjà tous vos points de vie !\nRécompense abandonnée !");
            } else {
                this.setLifeLevel(Math.min(getLifeLevel() + stuff.getStatBonus(), getMaxLifeLevel()));
                System.out.println(">>> " + stuff + " Potion bue ! (PV : " + this.getLifeLevel() + ")");
            }
        } else if (stuff instanceof Shield || stuff instanceof ProtectionSpell) {
            this.setDefensiveStuff(stuff);
            this.setDefenseLevel(this.getDefenseLevel() + stuff.getStatBonus());
            System.out.println(">>> Équipé : " + stuff);
        }
    }

    public void equipFromInventory(OffensiveStuff stuff) {
        this.setOffensiveStuff(stuff);
        this.setAttackLevel(this.getBaseAttackLevel() + stuff.getStatBonus());
        System.out.println(">>> 🗡️ " + this.getName() + " équipe : " + stuff.getName() + " (PA: " + this.getAttackLevel() + ")");
    }

    public void move(int steps, int totalCases) throws OutOfBoardException {
        this.position += steps;
        if (this.position >= totalCases) {
            this.position = totalCases - 1;
            throw new OutOfBoardException(">>> " + name + " dépasse la dernière case ! Position plafonnée à " + totalCases + ".");
        }
    }

    /**
     * Réinitialise le personnage pour une nouvelle partie.
     * L'or est remis à zéro — il est conservé uniquement si on sauvegarde en cours de partie.
     */
    public void reset() {
        this.resetPosition();
        this.setDefenseLevel(0);
        this.setLifeLevel(this.getBaseLifeLevel());
        this.setAttackLevel(this.getBaseAttackLevel());
        this.gold = 0;
        this.thunderActive = false;
        this.offensiveStuff = null;
        this.defensiveStuff = null;
        this.inventory = new Inventory();
    }

    public void flee(Enemy enemy) {
        Random random = new Random();
        int halfDmg = Math.max((enemy.getAttackLevel() - this.defense) / 2, 0);
        this.lifePoints -= halfDmg;
        int recoil = 1 + random.nextInt(6);
        int newPos = Math.max(this.position - recoil, 0);
        this.position = newPos;
        System.out.println(">>> 🏃 " + this.name + " prend la fuite !");
        if (halfDmg > 0) {
            System.out.println(">>> 💥 " + enemy.getName() + " inflige " + halfDmg + " dégâts dans le dos. (PV : " + this.lifePoints + ")");
        }
        System.out.println(">>> " + this.name + " recule à la case " + (newPos + 1) + " !");
    }

    public void attack(Enemy enemy) {
        int bonus = 0;
        int roll = this.dice.roll();
        System.out.println(">>> Vous avez obtenu : " + roll + " avec le " + this.dice);

        if (roll == 1) {
            System.out.println(">>> ❌ Vous manquez votre attaque !");
            bonus -= this.damage;
        } else if (roll == 20) {
            bonus = 2;
            System.out.println(">>> 💥 Attaque critique ! Dégâts + 2 !");
        }

        int weaponDmg = (this.offensiveStuff instanceof OffensiveStuff offStuff)
                ? offStuff.getDamageAgainst(enemy)
                : 0;
        int baseDmg = this.getBaseAttackLevel() + weaponDmg + bonus;

        if (this.isThunderActive()) {
            baseDmg *= 2;
            System.out.println(">>> ⚡ Coup de Tonnerre ! Dégâts doublés !");
            this.thunderActive = false;
        }

        enemy.setLifeLevel(enemy.getLifeLevel() - baseDmg);
        System.out.println("\n>>> " + this.getName() + " inflige " + baseDmg + " points de dégâts à " + enemy.getName()
                + " (" + enemy.getName() + " PV : " + Math.max(enemy.getLifeLevel(), 0) + ")");
    }

    /**
     * Ajoute de l'or au personnage.
     */
    public void addGold(int amount) {
        this.gold += amount;
        System.out.println(">>> \uD83D\uDCB0 " + this.name + " gagne " + amount + " pièces d'or ! (Total : " + this.gold + " 🪙)");
    }

    /**
     * Dépense de l'or si le personnage en a assez.
     * @return true si la transaction a réussi, false sinon.
     */
    public boolean spendGold(int amount) {
        if (this.gold < amount) {
            System.out.println(">>> ❌ Pas assez d'or ! (Vous avez " + this.gold + " 🪙, il en faut " + amount + ")");
            return false;
        }
        this.gold -= amount;
        System.out.println(">>> 💸 " + amount + " pièces dépensées. (Reste : " + this.gold + " 🪙)");
        return true;
    }

    public boolean isDead() { return this.lifePoints <= 0; }
    public void resetPosition() { this.position = 0; }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return this.type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public int getLifeLevel() { return this.lifePoints; }
    public void setLifeLevel(int lifePoints) { this.lifePoints = lifePoints; }
    public int getAttackLevel() { return this.damage; }
    public void setAttackLevel(int damage) { this.damage = damage; }
    public int getDefenseLevel() { return this.defense; }
    public void setDefenseLevel(int defense) { this.defense = defense; }
    public int getPosition() { return this.position; }
    public void setPosition(int position) { this.position = position; }
    public int getGold() { return this.gold; }
    public void setGold(int gold) { this.gold = gold; }
    public boolean isThunderActive() { return this.thunderActive; }
    public void setThunderActive(boolean b) { this.thunderActive = b; }
    public Stuff getOffensiveStuff() { return this.offensiveStuff; }
    public void setOffensiveStuff(Stuff s) { this.offensiveStuff = s; }
    public Stuff getDefensiveStuff() { return this.defensiveStuff; }
    public void setDefensiveStuff(Stuff s) { this.defensiveStuff = s; }
    public Inventory getInventory() { return this.inventory; }
    public void setInventory(Inventory inv) { this.inventory = inv; }

    @Override
    public String toString() {
        String offensive = (this.offensiveStuff != null) ? this.offensiveStuff.toString() : "Aucun";
        String defensive = (this.defensiveStuff != null) ? this.defensiveStuff.toString() : "Aucun";
        return "Nom : " + this.name + "\n"
                + "PV  : " + this.lifePoints + "/" + getMaxLifeLevel() + "\n"
                + getSpecialStatLabel() + "  : " + this.damage + "\n"
                + "Or  : " + this.gold + " 🪙\n"
                + (this.isThunderActive() ? "⚡ Coup de Tonnerre actif !\n" : "")
                + "Position : " + (this.position + 1) + "\n"
                + "Équipement offensif : " + offensive + "\n"
                + "Équipement défensif : " + defensive + "\n"
                + this.inventory.toString();
    }
}