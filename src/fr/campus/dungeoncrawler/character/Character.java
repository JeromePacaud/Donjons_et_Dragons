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

    public void reset() {
        this.resetPosition();
        this.setDefenseLevel(0);
        this.setLifeLevel(this.getBaseLifeLevel());
        this.setAttackLevel(this.getBaseAttackLevel());
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

        // Bonus variable selon l'ennemi (Arc vs Dragon, Invisibilité vs Mauvais Esprit...)
        int weaponDmg = (this.offensiveStuff instanceof OffensiveStuff offStuff)
                ? offStuff.getDamageAgainst(enemy)
                : 0;
        int baseDmg = this.getBaseAttackLevel() + weaponDmg + bonus;

        // Buff Coup de Tonnerre
        if (this.thunderActive) {
            baseDmg *= 2;
            System.out.println(">>> ⚡ Coup de Tonnerre ! Dégâts doublés !");
            this.thunderActive = false;
        }

        enemy.setLifeLevel(enemy.getLifeLevel() - baseDmg);
        System.out.println("\n>>> " + this.getName() + " inflige " + baseDmg + " points de dégâts à " + enemy.getName()
                + " (" + enemy.getName() + " PV : " + Math.max(enemy.getLifeLevel(), 0) + ")");
    }

    public boolean isDead()         { return this.lifePoints <= 0; }
    public void resetPosition()     { this.position = 0; }

    public int getId()                          { return this.id; }
    public void setId(int id)                   { this.id = id; }
    public String getType()                     { return type; }
    public void setType(String type)            { this.type = type; }
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }
    public int getLifeLevel()                   { return lifePoints; }
    public void setLifeLevel(int lifePoints)    { this.lifePoints = lifePoints; }
    public int getAttackLevel()                 { return damage; }
    public void setAttackLevel(int damage)      { this.damage = damage; }
    public int getDefenseLevel()                { return defense; }
    public void setDefenseLevel(int defense)    { this.defense = defense; }
    public int getPosition()                    { return position; }
    public void setPosition(int position)       { this.position = position; }
    public boolean isThunderActive()            { return thunderActive; }
    public void setThunderActive(boolean b)     { this.thunderActive = b; }
    public Stuff getOffensiveStuff()            { return offensiveStuff; }
    public void setOffensiveStuff(Stuff s)      { this.offensiveStuff = s; }
    public Stuff getDefensiveStuff()            { return defensiveStuff; }
    public void setDefensiveStuff(Stuff s)      { this.defensiveStuff = s; }
    public Inventory getInventory()             { return inventory; }
    public void setInventory(Inventory inv)     { this.inventory = inv; }

    @Override
    public String toString() {
        String offensive = (offensiveStuff != null) ? offensiveStuff.toString() : "Aucun";
        String defensive = (defensiveStuff != null) ? defensiveStuff.toString() : "Aucun";
        return "Nom : " + name + "\n"
                + "PV  : " + lifePoints + "/" + getMaxLifeLevel() + "\n"
                + getSpecialStatLabel() + "  : " + damage + "\n"
                + (thunderActive ? "⚡ Coup de Tonnerre actif !\n" : "")
                + "Position : " + (position + 1) + "\n"
                + "Équipement offensif : " + offensive + "\n"
                + "Équipement défensif : " + defensive + "\n"
                + inventory.toString();
    }
}