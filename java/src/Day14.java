import common.Coordinate;
import common.Vector;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class Day14 implements DaySolution<Integer> {

    @Override
    public Integer part1(Path inputFilePath) {
        Function<CaveMap, Predicate<CaveNode>> stopConditionFactory = caveMap -> caveNode -> caveNode.getCoordinate().y() > caveMap.lowestRockDepth();
        return extracted(inputFilePath, stopConditionFactory);
    }

    @Override
    public Integer part2(Path inputFilePath) {
        Function<CaveMap, Predicate<CaveNode>> stopConditionFactory = caveMap -> caveNode -> caveNode.getCoordinate().y() == 0 && caveNode.type.equals("sand");
        return extracted(inputFilePath, stopConditionFactory);
    }

    private int extracted(Path inputFilePath, Function<CaveMap, Predicate<CaveNode>> stopConditionFactory) {
        CaveMap caveMap = new CaveMap();
        CaveNode sandSource = new CaveNode("source", new Coordinate(500, 0), caveMap);
        try (Stream<String> lines = lines(inputFilePath)) {
            lines.forEach(rockFormationDescription -> parseRockNodes(rockFormationDescription, caveMap));
        }

        Sand sand = new Sand(stopConditionFactory.apply(caveMap));

        int counter = 0;
        do {
            sandSource.dropSand(sand);
            counter++;
        } while (!sand.stopConditionMet);

        System.out.printf("%s packets of sand dropped before stopping%n", counter - 1);

        return counter - 1;
    }

    private static class CaveMap {
        private final Map<Coordinate, CaveNode> map = new HashMap<>();
        private int lowestRockDepth;
        private int floorDepth;

        public CaveNode getNodeAt(Coordinate coordinate) {
            return map.computeIfAbsent(coordinate, co ->
                    co.y() == floorDepth()
                            ? new CaveNode("rock", co, this)
                            : new CaveNode("air", co, this));
        }

        public void add(CaveNode rock) {
            map.put(rock.coordinate, rock);
        }

        public int lowestRockDepth() {
            if (lowestRockDepth == 0)
                lowestRockDepth = map.keySet().stream().mapToInt(Coordinate::y).max().orElseThrow();
            return lowestRockDepth;
        }

        public int floorDepth() {
            if (floorDepth == 0)
                floorDepth = lowestRockDepth() + 2;
            return floorDepth;
        }
    }

    private void parseRockNodes(String rockFormationDescription, CaveMap caveMap) {
        Coordinate previousRockCoordinate = null;
        for (String rockCoordinatesString : rockFormationDescription.split(" -> ")) {
            String[] rockCoordinatesAsStrings = rockCoordinatesString.split(",");
            Coordinate rockCoordinates = new Coordinate(
                    parseInt(rockCoordinatesAsStrings[0]),
                    parseInt(rockCoordinatesAsStrings[1]));

            if (previousRockCoordinate == null) {
                CaveNode rock = new CaveNode("rock", rockCoordinates, caveMap);
                caveMap.add(rock);
                previousRockCoordinate = rock.coordinate;
            } else {
                Vector directionVector = previousRockCoordinate.x() == rockCoordinates.x()
                        ? new Vector(0, (rockCoordinates.y() - previousRockCoordinate.y()) / Math.abs(rockCoordinates.y() - previousRockCoordinate.y()))
                        : new Vector((rockCoordinates.x() - previousRockCoordinate.x()) / Math.abs(rockCoordinates.x() - previousRockCoordinate.x()), 0);

                while (!previousRockCoordinate.equals(rockCoordinates)) {
                    Coordinate newCoordinate = previousRockCoordinate.apply(directionVector);
                    CaveNode rock = new CaveNode("rock", newCoordinate, caveMap);
                    caveMap.add(rock);
                    previousRockCoordinate = newCoordinate;
                }
            }
        }
    }

    private static class CaveNode {
        private String type;
        private final Coordinate coordinate;
        private final CaveMap caveMap;

        public CaveNode(String type, Coordinate coordinate, CaveMap caveMap) {
//            System.out.println("Creating " + type + " at " + coordinate);
            this.type = type;
            this.coordinate = coordinate;
            this.caveMap = caveMap;
        }

        public Coordinate getCoordinate() {
            return coordinate;
        }

        public void dropSand(Sand sand) {
            if (sand.shouldContinueDroppingTo(this)) {
//                System.out.println("Dropping to " + coordinate);
                Vector[] vectors = {new Vector(0, 1), new Vector(-1, 1), new Vector(1, 1)};
                Optional<CaveNode> nextAvailableNode = Stream.of(vectors)
                        .map(coordinate::apply)
                        .map(caveMap::getNodeAt)
                        .filter(node -> node.type.equals("air"))
                        .findFirst();
                if (nextAvailableNode.isEmpty()) {
//                    System.out.println("Sand landed at " + coordinate);
                    this.type = "sand";
                } else
                    nextAvailableNode.get().dropSand(sand);
            }
        }
    }

    private static class Sand {
        private final Predicate<CaveNode> stopCondition;
        private boolean stopConditionMet = false;

        private Sand(Predicate<CaveNode> stopCondition) {
            this.stopCondition = stopCondition;
        }

        public boolean shouldContinueDroppingTo(CaveNode caveNode) {
            stopConditionMet = stopCondition.test(caveNode);
            return !stopConditionMet;
        }
    }
}
