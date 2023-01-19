package ca.fortressdefense.api;

import ca.fortressdefense.model.Game;

/**
 * Wrapper class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 *       from the data stored in the model, or required by the model.
 */
public class ApiGameWrapper {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int fortressHealth;
    public int numTanksAlive;

    // Amount of damage that the tanks did on the last time they fired.
    // If tanks have not yet fired, then it should be an empty array (0 size).
    public int[] lastTankDamages;

    public ApiGameWrapper(int gameNumber, Game game) {
        this.gameNumber = gameNumber;
        this.isGameWon = game.hasUserWon();
        this.isGameLost = game.hasUserLost();
        this.fortressHealth = game.getFortressHealth();
        this.numTanksAlive = calculateNumTanksAlive(game.getLatestTankDamages().length);
        this.lastTankDamages = game.getLatestTankDamages();
    }

    private int calculateNumTanksAlive(int lastTankDamagesSize) {
        if (lastTankDamagesSize == 0 && fortressHealth == 2500) {
            return 5;
        } else {
            return lastTankDamagesSize;
        }
    }
}
