package ca.fortressdefense.api;

import ca.fortressdefense.model.Game;
import ca.fortressdefense.model.GameBoard;

/**
 * Wrapper class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 *       from the data stored in the model, or required by the model.
 */
public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public String[][] cellStates;

    public ApiBoardWrapper(Game game, boolean revealBoard) {
        this.boardWidth = game.getBoard().getNumberCols();
        this.boardHeight = game.getBoard().getNumberRows();
        this.cellStates = loadCellStates(game.getBoard(), revealBoard);
    }

    private String[][] loadCellStates(GameBoard gameBoard, boolean revealBoard) {
        String[][] cells = new String[this.boardHeight][this.boardWidth];
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (gameBoard.getBoardCellStates()[i][j].isHidden() && !revealBoard) {
                    cells[i][j] = "fog";
                } else if (gameBoard.getBoardCellStates()[i][j].hasTank()) {
                    if (revealBoard) {
                        if (gameBoard.getBoardCellStates()[i][j].hasBeenShot()) {
                            cells[i][j] = "hit";
                        } else {
                            cells[i][j] = "tank";
                        }
                    } else {
                        cells[i][j] = "hit";
                    }
                } else if (gameBoard.getBoardCellStates()[i][j].hasBeenShot()) {
                    cells[i][j] = "miss";
                } else {
                    cells[i][j] = "field";
                }
            }
        }
        return cells;
    }
}
