package models;

public abstract class Ship {
    protected String name;
    protected int size;
    protected char symbol;
    protected int x;
    protected int y;
    protected boolean isHorizontal;
    protected int[] isHit;

    public Ship(String name, int size, char symbol) {
        this.name = name;
        this.size = size;
        this.symbol = symbol;
        this.isHit = new int[size];
    }

    public void setPosition(int x, int y, boolean isHorizontal) {
        this.x = x;
        this.y = y;
        this.isHorizontal = isHorizontal;
    }

    public boolean updateHitStatus(int shotX, int shotY) {
        if (isHorizontal) {
            if (shotY == y && shotX >= x && shotX < x + size) {
                int position = shotX - x;
                isHit[position] = 1;
                return true;
            }
        } else {
            if (shotX == x && shotY >= y && shotY < y + size) {
                int position = shotY - y;
                isHit[position] = 1;
                return true;
            }
        }
        return false;
    }

    public boolean isSunk() {
        for (int hit : isHit) {
            if (hit == 0) return false;
        }
        return true;
    }

    public String getName() { return name; }
    public int getSize() { return size; }
    public char getSymbol() { return symbol; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isHorizontal() { return isHorizontal; }
}