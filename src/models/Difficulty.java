package models;

public enum Difficulty {
    EASY(10, new ShipType[]{
            new ShipType("Porta-avioes", 6, 1, 'P'),
            new ShipType("Cruzador", 5, 1, 'C'),
            new ShipType("Fragata", 4, 2, 'F'),
            new ShipType("Destroyer", 3, 2, 'D'),
            new ShipType("Submarino", 2, 2, 'S')
    }),
    MEDIUM(15, new ShipType[]{
            new ShipType("Porta-avioes", 6, 1, 'P'),
            new ShipType("Cruzador", 5, 2, 'C'),
            new ShipType("Fragata", 4, 3, 'F'),
            new ShipType("Destroyer", 3, 2, 'D'),
            new ShipType("Submarino", 2, 3, 'S')
    }),
    HARD(30, new ShipType[]{
            new ShipType("Porta-avioes", 6, 2, 'P'),
            new ShipType("Cruzador", 5, 3, 'C'),
            new ShipType("Fragata", 4, 4, 'F'),
            new ShipType("Destroyer", 3, 4, 'D'),
            new ShipType("Submarino", 2, 5, 'S')
    });

    private final int boardSize;
    private final ShipType[] shipTypes;

    Difficulty(int boardSize, ShipType[] shipTypes) {
        this.boardSize = boardSize;
        this.shipTypes = shipTypes;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public ShipType[] getShipTypes() {
        return shipTypes;
    }
}