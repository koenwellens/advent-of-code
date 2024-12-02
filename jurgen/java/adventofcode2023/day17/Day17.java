package adventofcode2023.day17;

import adventofcode2023.common.Input;
import adventofcode2023.common.model.Coordinate;
import adventofcode2023.common.model.Vector;

import java.util.*;
import java.util.function.Predicate;

import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day17.Day17Input.EXAMPLE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;

class Day17 {
    public static void main(String[] args) {
        runnerFor(Day17.class)
                .execute("Puzzle 1", Day17::findPathWithMinimalHeatLoss).withInput(EXAMPLE).expect(102)
                .build()
                .run();
    }

    private static int findPathWithMinimalHeatLoss(Input input) {
        Map<Coordinate, Node> nodes = input
                .chars()
                .map(inputChar -> new Node(Integer.parseInt(inputChar.value()), new Coordinate(inputChar.linePosition(), inputChar.line())))
                .collect(toMap(node -> node.coordinate, node -> node));

        Coordinate startCoordinate = new Coordinate(0, 0);
        Coordinate destinationCoordinate = nodes.keySet().stream()
                .max(comparing(co -> co.manhattanDistanceTo(startCoordinate)))
                .orElseThrow();
        Predicate<Coordinate> isValid = co ->
                co.x() >= 0
                        && co.x() <= destinationCoordinate.x()
                        && co.y() >= 0
                        && co.y() <= destinationCoordinate.y();

        Queue<Node> nodesToVisit = new LinkedList<>();
        Set<Coordinate> visitedNodeCoordinates = new HashSet<>();

        Node startNode = nodes.get(startCoordinate);
        nodesToVisit.add(startNode);
        startNode.pathCost = 0;
        startNode.path.add(startCoordinate);

        int MAX_PATH_DIRECTION_COUNT = 3;
        while (!nodesToVisit.isEmpty()) {
            Node currentNode = nodesToVisit.poll();
            for (Direction direction : Direction.values()) {
                if (!(currentNode.lastPathDirection == direction && currentNode.lastPathDirectionCount == MAX_PATH_DIRECTION_COUNT)) {
                    Coordinate nextNodeCoordinate = currentNode.coordinate.apply(direction.vector());
                    if (isValid.test(nextNodeCoordinate)
                            && !visitedNodeCoordinates.contains(nextNodeCoordinate)
                            && !currentNode.path.contains(nextNodeCoordinate)) {
                        Node nextNode = nodes.get(nextNodeCoordinate);
                        int nextNodePathCost = currentNode.pathCost + nextNode.cost;
                        if (nextNodePathCost < nextNode.pathCost) {
                            nextNode.pathCost = nextNodePathCost;
                            nextNode.lastPathDirectionCount = currentNode.lastPathDirection == direction ? currentNode.lastPathDirectionCount + 1 : 1;
                            nextNode.lastPathDirection = direction;
                            nextNode.path.clear();
                            nextNode.path.addAll(currentNode.path);
                            nextNode.path.add(nextNodeCoordinate);
                        }
                        nodesToVisit.add(nextNode);
                    }
                }
            }
            visitedNodeCoordinates.add(currentNode.coordinate);
        }

        printPath(nodes.get(destinationCoordinate), nodes);

        return nodes.get(destinationCoordinate).pathCost;
    }

    private static void printPath(Node pathEndNode, Map<Coordinate, Node> nodes) {
        for (int y = 0; y <= pathEndNode.coordinate.y(); y++) {
            for (int x = 0; x <= pathEndNode.coordinate.x(); x++) {
                Coordinate co = new Coordinate(x, y);
                Node node = nodes.get(co);
                if (pathEndNode.path.contains(co) && node.lastPathDirection != null) {
                    System.out.print(node.lastPathDirection.symbol);
                } else {
                    System.out.print(node.cost);
                }
            }
            System.out.println();
        }

    }

    static class Node {

        private final int cost;
        public Direction lastPathDirection;
        public int lastPathDirectionCount;
        private int pathCost = Integer.MAX_VALUE;
        private final Coordinate coordinate;
        private final List<Coordinate> path = new ArrayList<>();

        Node(int cost, Coordinate coordinate) {
            this.cost = cost;
            this.coordinate = coordinate;
        }

    }

    enum Direction {
        LEFT(-1, 0, "<"),
        RIGHT(1, 0, ">"),
        UP(0, -1, "^"),
        DOWN(0, 1, "v");

        private final Vector vector;
        private final String symbol;

        Direction(int deltaX, int deltaY, String symbol) {
            this.symbol = symbol;
            this.vector = new Vector(deltaX, deltaY);
        }

        Vector vector() {
            return vector;
        }
    }
}
