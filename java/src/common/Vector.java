package common;

public record Vector(int deltaX, int deltaY) {

    public int manhattanDistance() {
        return Math.abs(deltaX) + Math.abs(deltaY);
    }
}
