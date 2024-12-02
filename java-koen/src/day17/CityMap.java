package day17;

import common.AbstractObjectBasedOnInput;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CityMap extends AbstractObjectBasedOnInput<Long> {

    private static final String START_COORDINATES = "0;0";

    public CityMap(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Long run() {
        final var cityBlockMap = new CityBlockParser(textLines).value();
        final var endCoordinate = new EndCoordinatesParser(textLines).value();
        final var firstBlock = cityBlockMap.get(START_COORDINATES);

        Set<String> unhandledNodes = new HashSet<>();
        unhandledNodes.add(START_COORDINATES);
        firstBlock.updateShortestPath(new ThePath(firstBlock));

        while (!unhandledNodes.isEmpty()) {
            final var block = getShortestHeatLossCityBlock(unhandledNodes, cityBlockMap);
            unhandledNodes.remove(block.toString());
            for (Map.Entry<Direction, String> neighbour : block.neighbours().entrySet()) {
                if (block.canGoTo(neighbour.getKey())) {
                    final var updated = calculateMinimumDistance(neighbour.getKey(), neighbour.getValue(), block, cityBlockMap);
                    if(updated) {
                        unhandledNodes.add(neighbour.getValue());
                    }
                }
            }
        }

        final var endBlock = cityBlockMap.get(endCoordinate);
        return endBlock.shortestPathHeatLoss();
    }

    private boolean calculateMinimumDistance(final Direction direction, final String neighbour, final CityBlock block, final Map<String, CityBlock> cityBlockMap) {
        final var shortestPathUntilNow = block.shortestPath();
        final var cityBlock = cityBlockMap.get(neighbour);
        final var newPath = shortestPathUntilNow.add(direction, cityBlock);
        System.out.println(STR."For (\{block.shortestPath().toString()}): Calculating (\{neighbour}) in direction \{direction}. Existing path length is \{cityBlock.shortestPathHeatLoss()} and new path length is \{newPath.totalHeatLoss()}");
        if (newPath.totalHeatLoss() < cityBlock.shortestPathHeatLoss()) {
            cityBlock.updateShortestPath(newPath);
            System.out.println("We computed that the shortest heat loss is now the new path! Yeehaw!");
            return true;
        }

        if (newPath.totalHeatLoss().equals(cityBlock.shortestPathHeatLoss()) && newPath.numberOfDirectionsTaken() < cityBlock.shortestPath().numberOfDirectionsTaken()) {
            System.out.println("We computed that the shortest heat loss is now the new path because of less directions taken!! Yeehaw!");
            cityBlock.updateShortestPath(newPath);
            return true;
        }

        return false;
    }

    private CityBlock getShortestHeatLossCityBlock(Set<String> unhandledNodes, Map<String, CityBlock> cityBlockMap) {
        CityBlock result = null;
        long lowestHeatLoss = Long.MAX_VALUE;
        for (String str : unhandledNodes) {
            final var cityBlock = cityBlockMap.get(str);
            final var totalHeatLoss = cityBlock.shortestPathHeatLoss();
            if(totalHeatLoss < lowestHeatLoss) {
                lowestHeatLoss = totalHeatLoss;
                result = cityBlock;
            }
        }

        return result;
    }

    @Override
    public Long alternateRun() {
        return null;
    }
}
