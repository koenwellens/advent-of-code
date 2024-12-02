package day17;

import java.util.Map;

public interface CityBlock extends Comparable<CityBlock> {

    int heatLoss();

    Long shortestPathHeatLoss();

    Path shortestPath();

    Map<Direction, String> neighbours();

    boolean is(String coordinate);

    String toString();

    boolean canGoTo(Direction direction);

    void updateShortestPath(Path path);
}
