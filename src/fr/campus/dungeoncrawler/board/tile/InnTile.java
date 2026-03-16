package fr.campus.dungeoncrawler.board.tile;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.menu.Menu;

/**
 * Tuile représentant une auberge.
 * Le joueur peut restaurer tous ses PV contre un coût fixe en or.
 */
public class InnTile extends Tile {

    public int cost = 10;
    private Menu menu;

    public InnTile() {
        super("Inn");
        this.menu = new Menu();
    }

    @Override
    public String getTileImage() { return "🏠"; }

    @Override
    public void interact(Character character) {
        System.out.println("\n>>> 🏠 Une auberge ! Bienvenue voyageur !");
        System.out.println(">>> PV actuels : " + character.getLifeLevel() + "/" + character.getMaxLifeLevel());
        System.out.println(">>> Bourse : " + character.getGold() + " 🪙");

        if (character.getLifeLevel() >= character.getMaxLifeLevel()) {
            System.out.println(">>> Vous êtes en pleine forme, pas besoin de vous reposer !");
            return;
        }

        System.out.println("\n[1] Se reposer (" + cost + " 🪙) → PV restaurés à " + character.getMaxLifeLevel());
        System.out.println("[2] Continuer sans se reposer");
        System.out.print("Votre choix : ");

        int choice = menu.readInt();

        if (choice == 1) {
            if (character.spendGold(cost)) {
                character.setLifeLevel(character.getMaxLifeLevel());
                System.out.println(">>> 💤 Vous vous reposez et récupérez tous vos PV ! (PV : "
                    + character.getLifeLevel() + "/" + character.getMaxLifeLevel() + ")");
            }
        } else {
            System.out.println(">>> Vous continuez votre route...");
        }
    }
}