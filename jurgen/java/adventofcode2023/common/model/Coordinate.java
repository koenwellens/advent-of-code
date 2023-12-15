package adventofcode2023.common.model;

public record Coordinate(int x, int y) {

    public int manhattanDistanceTo(Coordinate other) {
        return Math.abs(other.x - x) + Math.abs(other.y - y);
    }

}