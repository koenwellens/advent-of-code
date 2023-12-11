package day11;

public final class TheGalaxy implements Galaxy {

    private final long x;
    private final long y;

    public TheGalaxy(final long x, final long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public long x() {
        return x;
    }

    @Override
    public long y() {
        return y;
    }

    @Override
    public long shortestDistanceTo(final Galaxy galaxy) {
        return Math.abs(galaxy.x() - x) + Math.abs(galaxy.y() - y);
    }
}
