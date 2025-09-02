package controller;

import models.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameController {
    private Board userBoard;
    private Board computerBoard;
    private Difficulty difficulty;
    private boolean isPlayerTurn;
    private List<Ship> userShips;
    private List<Ship> computerShips;

    public void initializeGame(Difficulty difficulty) {
        this.difficulty = difficulty;
        int boardSize = difficulty.getBoardSize();
        this.userBoard = new Board(boardSize);
        this.computerBoard = new Board(boardSize);
        this.userShips = new ArrayList<>();
        this.computerShips = new ArrayList<>();
        this.isPlayerTurn = new Random().nextBoolean();

        System.out.println("Game initialized with " + difficulty + " difficulty");
        System.out.println("Board size: " + boardSize + "x" + boardSize);
        System.out.println((isPlayerTurn ? "Player" : "Computer") + " goes first");
    }

    public boolean placeUserShip(Ship ship, int x, int y, boolean isHorizontal) {
        boolean placed = userBoard.placeShip(ship, x, y, isHorizontal);
        if (placed) {
            userShips.add(ship);
        }
        return placed;
    }

    public void placeComputerShips() {
        ShipType[] shipTypes = difficulty.getShipTypes();

        for (ShipType shipType : shipTypes) {
            for (int i = 0; i < shipType.getCount(); i++) {
                Ship ship = createShip(shipType);
                boolean placed = false;
                int attempts = 0;

                while (!placed && attempts < 20) {
                    int x = ThreadLocalRandom.current().nextInt(computerBoard.getSize());
                    int y = ThreadLocalRandom.current().nextInt(computerBoard.getSize());
                    boolean isHorizontal = ThreadLocalRandom.current().nextBoolean();

                    placed = computerBoard.placeShip(ship, x, y, isHorizontal);
                    attempts++;
                }

                if (placed) {
                    computerShips.add(ship);
                } else {
                    computerBoard.eraseAll();
                    computerShips.clear();
                    i = -1;
                    break;
                }
            }
        }
    }

    private Ship createShip(ShipType shipType) {
        switch (shipType.getName()) {
            case "Porta-avioes": return new AircraftCarrier();
            case "Cruzador": return new Cruiser();
            case "Fragata": return new Frigate();
            case "Destroyer": return new Destroyer();
            case "Submarino": return new Submarine();
            default: return null;
        }
    }

    public ShotResult playerShot(int x, int y) {
        if (!computerBoard.getSlotExistence(x, y)) {
            return ShotResult.INVALID;
        }

        char status = computerBoard.getSlotStatus(x, y);
        if (status == 'X' || status == 'O') {
            return ShotResult.ALREADY_SHOT;
        }

        boolean hit = computerBoard.markShot(x, y);
        if (hit) {
            Ship hitShip = computerBoard.getShip(x, y);
            if (hitShip != null) {
                hitShip.updateHitStatus(x, y);
                if (hitShip.isSunk()) {
                    return new ShotResult(ShotResult.ResultType.SUNK, x, y, hitShip);
                }
                return ShotResult.HIT;
            }
        }

        return ShotResult.MISS;
    }

    public ShotResult computerShot() {
        int[] target = computerBoard.getRandomAvailableSlot();
        if (target == null) {
            return ShotResult.INVALID;
        }

        int x = target[0];
        int y = target[1];

        boolean hit = userBoard.markShot(x, y);
        if (hit) {
            Ship hitShip = userBoard.getShip(x, y);
            if (hitShip != null) {
                hitShip.updateHitStatus(x, y);
                if (hitShip.isSunk()) {
                    return new ShotResult(ShotResult.ResultType.SUNK, x, y, hitShip);
                }
                return new ShotResult(ShotResult.ResultType.HIT, x, y, hitShip);
            }
        }

        return new ShotResult(ShotResult.ResultType.MISS, x, y, null);
    }

    public List<Ship> getUserShipsAlive() {
        List<Ship> alive = new ArrayList<>();
        for (Ship ship : userShips) {
            if (!ship.isSunk()) {
                alive.add(ship);
            }
        }
        return alive;
    }

    public List<Ship> getComputerShipsAlive() {
        List<Ship> alive = new ArrayList<>();
        for (Ship ship : computerShips) {
            if (!ship.isSunk()) {
                alive.add(ship);
            }
        }
        return alive;
    }

    public List<Ship> getUserShipsSunk() {
        List<Ship> sunk = new ArrayList<>();
        for (Ship ship : userShips) {
            if (ship.isSunk()) {
                sunk.add(ship);
            }
        }
        return sunk;
    }

    public List<Ship> getComputerShipsSunk() {
        List<Ship> sunk = new ArrayList<>();
        for (Ship ship : computerShips) {
            if (ship.isSunk()) {
                sunk.add(ship);
            }
        }
        return sunk;
    }

    public void eraseUserBoard() {
        userBoard.eraseAll();
        userShips.clear();
    }

    public void removeUserShip(int x, int y) {
        Ship ship = userBoard.getShip(x, y);
        if (ship != null) {
            userBoard.removeShip(ship);
            userShips.remove(ship);
        }
    }

    public Board getUserBoard() {
        return userBoard;
    }

    public Board getComputerBoard() {
        return computerBoard;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void switchTurn() {
        isPlayerTurn = !isPlayerTurn;
    }

    public boolean isGameOver() {
        return getUserShipsAlive().isEmpty() || getComputerShipsAlive().isEmpty();
    }

    public String getWinner() {
        if (getUserShipsAlive().isEmpty()) {
            return "Computer";
        } else if (getComputerShipsAlive().isEmpty()) {
            return "Player";
        }
        return null;
    }
}