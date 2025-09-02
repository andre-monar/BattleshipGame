package controller;

import models.Ship;

public class ShotResult {
    public enum ResultType {
        HIT, MISS, SUNK, ALREADY_SHOT, INVALID
    }

    private ResultType type;
    private int x;
    private int y;
    private Ship ship;

    public ShotResult(ResultType type, int x, int y, Ship ship) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.ship = ship;
    }

    public ShotResult(ResultType type) {
        this.type = type;
    }

    public ResultType getType() { return type; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Ship getShip() { return ship; }

    public static final ShotResult INVALID = new ShotResult(ResultType.INVALID);
    public static final ShotResult ALREADY_SHOT = new ShotResult(ResultType.ALREADY_SHOT);
    public static final ShotResult MISS = new ShotResult(ResultType.MISS);
    public static final ShotResult HIT = new ShotResult(ResultType.HIT);
    public static final ShotResult SUNK = new ShotResult(ResultType.SUNK);
}