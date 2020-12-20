
import battleship.BattleShip;
import battleship.CellState;

import java.awt.Point;
import java.util.*;

public class DeathStarBot {
    private int gameSize;
    private BattleShip battleShip;

    private final CellState[][] map;
    private Stack<Point> shotHitStack;
    private ArrayList<Point> lastShot = new ArrayList<>();
    private final ArrayList<Integer> shipSize = new ArrayList<>();
    private int numberOfShotsTaken;
    private int numberOfSunkShips;
    int lastShotX = -1; //since there have not been any shots fired yet.
    int lastShotY = -1;


    /**
     * Constructor keeps a copy of the BattleShip instance
     *
     * @param b previously created battleship instance - should be a new game
     */
    public DeathStarBot(BattleShip b) {
        battleShip = b;
        gameSize = b.boardSize;

        map = new CellState[gameSize][gameSize];
        shotHitStack = new Stack<>();
        shipSize.add(2);
        shipSize.add(3);
        shipSize.add(3);
        shipSize.add(4);
        shipSize.add(5);

        for (int i = 0; i < gameSize; i++)
            for (int j = 0; j < gameSize; j++)
                map[i][j] = CellState.Empty; // initialize a game reset. Basically, no shots have been fired at the
        //board yet so all cells are "empty" to be fired at

    }

    /**
     * Create a random shot and calls the battleship shoot method
     *
     * @return true if a Ship is hit, false otherwise
     */

    public boolean fireShot() {
        Point shot = null;

        if (!shotHitStack.isEmpty())
            shot = shotHitStack.pop();
        else {
            int highestProbabilityCell = 0;
            numberOfShotsTaken = 0;
            lastShot = new ArrayList<>();

            for (int x = 0; x < gameSize; x++)
                for (int y = 0; y < gameSize; y++)
                    if (map[x][y] == CellState.Empty) {
                        //calculates the probability of a cell at each iteration since the probability changes after
                        //each shot is fired. There might be less empty cells around a random cell chosen at random.
                        int probabilityForThatCell = getProbabilityForACell(x, y);

                        if (probabilityForThatCell >= highestProbabilityCell) {
                            highestProbabilityCell = probabilityForThatCell;
                            shot = new Point(x, y);
                        }
                    }
        }

        boolean hit = battleShip.shoot(shot);

        if (hit) {
            ENGAGE_AND_FIRE(shot);
        } else {
            map[shot.x][shot.y] = CellState.Miss;

        }
        return hit;
    }

    public void checkSurroundingCells(Point shot) {
        Point nextTarget;
        //left of the current shot
        if (shot.x - 1 >= 0 && map[shot.x - 1][shot.y] == CellState.Empty) {
            nextTarget = new Point(shot.x - 1, shot.y);
            shotHitStack.push(nextTarget);
        }

        //right of the current shot
        if (shot.x + 1 < gameSize && map[shot.x + 1][shot.y] == CellState.Empty) {
            nextTarget = new Point(shot.x + 1, shot.y);
            shotHitStack.push(nextTarget);
        }

        //above the current shot
        if (shot.y - 1 >= 0 && map[shot.x][shot.y - 1] == CellState.Empty) {
            nextTarget = new Point(shot.x, shot.y - 1);
            shotHitStack.push(nextTarget);
        }

        //below the current shot
        if (shot.y + 1 < gameSize && map[shot.x][shot.y + 1] == CellState.Empty) {
            nextTarget = new Point(shot.x, shot.y + 1);
            shotHitStack.push(nextTarget);
        }

    }

    public int getProbabilityForACell(int x, int y) {
        int xToRight = getProbabilityForACellX(x, y, 0, 1);
        int xToLeft = getProbabilityForACellX(x, y, 0, -1);
        int yTop = getProbabilityForACellY(x, y, 0, 1);
        int yDown = getProbabilityForACellY(x, y, 0, -1);

        return xToRight + xToLeft + yTop + yDown;
    }

    /**
     * The two methods below recursively call the function to check the probability of the ship being around the
     * selected cell (x,y) till length 5 (counting itself of course) on both x and y because if (x,y) is a hit then
     * these 16 cells are the only ones which can contain the max length ship.
     *
     * @param x            x-coordinate of the current shot fired
     * @param y            y-coordinate of the current shot fired
     * @param count        used to check the maximum of 5 cells (including itself) for a possibility of hitting a ship
     * @param xLeftOrRight move left or right based on the argument. If it is negative then moves left and vice versa
     * @return
     */
    public int getProbabilityForACellX(int x, int y, int count, int xLeftOrRight) {
        if ((x >= 0 && x < gameSize) && map[x][y] == CellState.Empty && count < 4) {
            x += xLeftOrRight;
            count++;
            count = getProbabilityForACellX(x, y, count, xLeftOrRight);
        }

        return count;
    }

    public int getProbabilityForACellY(int x, int y, int count, int yLeftOrRight) {
        if ((y >= 0 && y < gameSize) && map[x][y] == CellState.Empty && count < 4) {
            y += yLeftOrRight;
            count++;
            count = getProbabilityForACellY(x, y, count, yLeftOrRight);
        }

        return count;
    }

    public void ENGAGE_AND_FIRE(Point shot) {
        map[shot.x][shot.y] = CellState.Hit;
        lastShot.add(shot);
        numberOfShotsTaken++;
        int count = 0;

        if (numberOfSunkShips == battleShip.numberOfShipsSunk()) {
            if (lastShotX >= 0) {

                if (lastShotX - shot.x == -1 && lastShotY - shot.y == 0 ||
                        (lastShotX - shot.x == 0 && lastShotY - shot.y == -1) ||
                        (lastShotX - shot.x == 0 && lastShotY - shot.y == 1)) checkSurroundingCells(shot);
                else if (lastShotX - shot.x == 1 && lastShotY - shot.y == 0) {
                    if (shot.x + 1 < gameSize && map[shot.x + 1][shot.y] == CellState.Empty) {
                        Point target = new Point(shot.x + 1, shot.y);
                        shotHitStack.push(target);
                    }
                    if (shot.y - 1 >= 0 && map[shot.x][shot.y - 1] == CellState.Empty) {
                        Point target = new Point(shot.x, shot.y - 1);
                        shotHitStack.push(target);
                    }
                    if (shot.y + 1 < gameSize && map[shot.x][shot.y + 1] == CellState.Empty) {
                        Point target = new Point(shot.x, shot.y + 1);
                        shotHitStack.push(target);
                    }
                    if (shot.x - 1 >= 0 && map[shot.x - 1][shot.y] == CellState.Empty) {
                        Point target = new Point(shot.x - 1, shot.y);
                        shotHitStack.push(target);
                    }
                } else
                    checkSurroundingCells(shot);
            } else
                checkSurroundingCells(shot);

            lastShotX = shot.x;
            lastShotY = shot.y;
        } else {
            numberOfSunkShips++;
            int smallestShip = Collections.min(shipSize);

            switch (numberOfShotsTaken) {
                case 2:
                    shipSize.remove(Integer.valueOf(2));
                    shotHitStack = new Stack<>();
                    break;
                case 3:
                    if (numberOfSunkShips == smallestShip) {
                        shotHitStack = new Stack<>();
                        shipSize.remove(Integer.valueOf(3));
                    } else {
                        shotHitStack = new Stack<>();
                        checkSurroundingCells(lastShot.get(0));
                    }
                    break;
                case 4:
                    if (smallestShip == 3) {
                        shotHitStack = new Stack<>();
                        checkSurroundingCells(lastShot.get(0));
                    } else if (smallestShip == 2) {
                        shotHitStack = new Stack<>();
                        checkSurroundingCells(lastShot.get(1));
                        checkSurroundingCells(lastShot.get(0));
                    }
                    break;
                case 5:
                    switch (smallestShip) {
                        case 2:
                            shotHitStack = new Stack<>();
                            checkSurroundingCells(lastShot.get(0));
                            break;
                        case 3:
                            shotHitStack = new Stack<>();
                            checkSurroundingCells(lastShot.get(1));
                            checkSurroundingCells(lastShot.get(0));
                            break;
                        case 4:
                            shotHitStack = new Stack<>();
                            checkSurroundingCells(lastShot.get(2));
                            checkSurroundingCells(lastShot.get(1));
                            checkSurroundingCells(lastShot.get(0));
                            break;
                    }
            }
            numberOfSunkShips = battleShip.numberOfShipsSunk();
        }
    }
}
