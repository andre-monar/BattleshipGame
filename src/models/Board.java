package models;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
    private char[][] matrix;
    private int size;
    private List<Ship> ships;

    public Board(int size) {
        this.size = size;
        this.matrix = new char[size][size];
        this.ships = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = '~';
            }
        }
    }

    public char getSlotStatus(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            return matrix[y][x];
        }
        return ' ';
    }

    public boolean isSlotOccupied(int x, int y) {
        return getSlotStatus(x, y) != '~';
    }

    public boolean getSlotExistence(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    // coloca o navio no tabuleiro, conferindo posicionamento, retornando 0 caso não foi possível colocar e 1 caso sucesso
    public boolean placeShip(Ship ship, int x, int y, boolean isHorizontal) {
        if (!checkPlacement(ship, x, y, isHorizontal)) {
            return false;
        }

        ship.setPosition(x, y, isHorizontal);
        ships.add(ship);

        if (isHorizontal) {
            for (int i = x; i < x + ship.getSize(); i++) {
                matrix[y][i] = ship.getSymbol();
            }
        } else {
            for (int i = y; i < y + ship.getSize(); i++) {
                matrix[i][x] = ship.getSymbol();
            }
        }

        return true;
    }

    public boolean checkPlacement(Ship ship, int x, int y, boolean isHorizontal) {
        if (isHorizontal) {
            if (x + ship.getSize() > size) return false;
            for (int i = x; i < x + ship.getSize(); i++) {
                if (!getSlotExistence(i, y) || isSlotOccupied(i, y)) return false;
            }
        } else {
            if (y + ship.getSize() > size) return false;
            for (int i = y; i < y + ship.getSize(); i++) {
                if (!getSlotExistence(x, i) || isSlotOccupied(x, i)) return false;
            }
        }
        return true;
    }

    public Ship getShip(int x, int y) {
        for (Ship ship : ships) {
            if (ship.isHorizontal()) {
                if (y == ship.getY() && x >= ship.getX() && x < ship.getX() + ship.getSize()) {
                    return ship;
                }
            } else {
                if (x == ship.getX() && y >= ship.getY() && y < ship.getY() + ship.getSize()) {
                    return ship;
                }
            }
        }
        return null;
    }

    public void removeShip(Ship ship) {
        ships.remove(ship);
        if (ship.isHorizontal()) {
            for (int i = ship.getX(); i < ship.getX() + ship.getSize(); i++) {
                matrix[ship.getY()][i] = '~';
            }
        } else {
            for (int i = ship.getY(); i < ship.getY() + ship.getSize(); i++) {
                matrix[i][ship.getX()] = '~';
            }
        }
    }

    public void eraseAll() {
        ships.clear();
        initializeBoard();
    }

    public boolean markShot(int x, int y) {
        if (!getSlotExistence(x, y)) return false;

        char status = getSlotStatus(x, y);
        if (status == 'X' || status == 'O') return false;

        if (status == '~') {
            matrix[y][x] = 'O';
            return false;
        } else {
            matrix[y][x] = 'X';
            return true;
        }
    }

    public void display(boolean showShips) {
        System.out.print("  ");
        for (int i = 0; i < size; i++) {
            System.out.print((char)('A' + i % 26) + " ");
            if (i >= 25) System.out.print((char)('A' + i / 26 - 1));
        }
        System.out.println();

        for (int i = 0; i < size; i++) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < size; j++) {
                char displayChar = matrix[i][j];
                if (!showShips && displayChar != '~' && displayChar != 'X' && displayChar != 'O') {
                    displayChar = '~';
                }
                System.out.print(displayChar + " ");
            }
            System.out.println();
        }
    }

    public List<Ship> getShips() {
        return ships;
    }

    public int getSize() {
        return size;
    }

    public int countAvailableSlots() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == '~') count++;
            }
        }
        return count;
    }

    public int[] getRandomAvailableSlot() {
        int available = countAvailableSlots();
        if (available == 0) return null;

        int randomIndex = ThreadLocalRandom.current().nextInt(available);
        int count = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == '~') {
                    if (count == randomIndex) {
                        return new int[]{j, i};
                    }
                    count++;
                }
            }
        }
        return null;
    }
}