package common;

public record Coordinate(int x, int y) {
    public Coordinate apply(Vector vector) {
        return new Coordinate(x + vector.deltaX(), y + vector.deltaY());
    }

    public Vector diffVectorTo(Coordinate other) {
        return new Vector(other.x() - x, other.y() - y);
    }
}
