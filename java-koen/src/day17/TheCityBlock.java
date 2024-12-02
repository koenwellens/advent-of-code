package day17;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static day17.Direction.D;
import static day17.Direction.L;
import static day17.Direction.R;
import static day17.Direction.U;

public final class TheCityBlock implements CityBlock {

    private final int heatLoss;
    private final int x;
    private final int y;
    private final int maxX;
    private final int maxY;
    private Path shortestPath = null;

    public TheCityBlock(final int heatLoss,
                        final int x,
                        final int y,
                        final int maxX,
                        final int maxY) {
        this.heatLoss = heatLoss;
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    public int compareTo(final CityBlock o) {
        if (shortestPath == null && o.shortestPath() == null) {
            return Integer.compare(this.heatLoss(), o.heatLoss());
        }

        if (shortestPath == null) {
            return -1;
        }

        if (o.shortestPath() == null) {
            return 1;
        }

        return shortestPath.compareTo(o.shortestPath());
    }

    @Override
    public int heatLoss() {
        return this.heatLoss;
    }

    @Override
    public Long shortestPathHeatLoss() {
        return shortestPath == null ? Long.MAX_VALUE : shortestPath.totalHeatLoss();
    }

    @Override
    public Path shortestPath() {
        return shortestPath;
    }

    @Override
    public Map<Direction, String> neighbours() {
        final var result = new EnumMap<Direction, String>(Direction.class);

        if (x < maxX && shortestPath.lastDirection() != L) {
            result.put(R, STR."\{x + 1};\{y}");
        }

        if (y < maxY && shortestPath.lastDirection() != U) {
            result.put(D, STR."\{x};\{y + 1}");
        }

        if (y > 0 && shortestPath.lastDirection() != D) {
            result.put(U, STR."\{x};\{y - 1}");
        }

        if (x > 0 && shortestPath.lastDirection() != R) {
            result.put(L, STR."\{x - 1};\{y}");
        }

        return result;
    }

    @Override
    public boolean is(final String coordinate) {
        return toString().equals(coordinate);
    }

    @Override
    public String toString() {
        return (STR."\{x};\{y}");
    }

    @Override
    public boolean canGoTo(final Direction direction) {
        return shortestPath == null || shortestPath.canGoTo(direction);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return this.toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public void updateShortestPath(final Path path) {
        this.shortestPath = path;
    }
}
