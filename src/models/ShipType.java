package models;

public class ShipType {
    private final String name;
    private final int size;
    private final int count;
    private final char symbol;

    public ShipType(String name, int size, int count, char symbol) {
        this.name = name;
        this.size = size;
        this.count = count;
        this.symbol = symbol;
    }

    public String getName() { return name; }
    public int getSize() { return size; }
    public int getCount() { return count; }
    public char getSymbol() { return symbol; }
}