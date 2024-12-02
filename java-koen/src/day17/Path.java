package day17;

import java.util.Collection;
import java.util.Map;

public interface Path extends Comparable<Path> {

    boolean isComplete(String endCoordinates);

    Long totalHeatLoss();

    Long numberOfDirectionsTaken();

    boolean canGoTo(Direction direction);

    Collection<Path> continueToAllPossibleDirections(Map<String, CityBlock> cityBlocks);

    Path add(Direction direction, CityBlock cityBlock);

    Direction lastDirection();
}
