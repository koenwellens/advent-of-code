import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Day12Copy implements DaySolution<Integer> {

    @Override
    public Integer part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            HeightNode[][] heightMap = parseMapHeights(lines);

            HeightNode endNode = null;
            HeightNode startNode = null;
            for (int row = 0; row < heightMap.length; row++) {
                for (int col = 0; col < heightMap[row].length; col++) {
                    HeightNode node = heightMap[row][col];
                    if (node.isStartNode())
                        startNode = node;
                    if (node.isEndNode())
                        endNode = node;
                    if (!node.isEndNode()) {
                        for (int[] diff : List.of(new int[]{0, 1}, new int[]{0, -1}, new int[]{1, 0}, new int[]{-1, 0})) {
                            int nextNodeRow = row + diff[0];
                            int nextNodeCol = col + diff[1];
                            if (nextNodeRow > -1
                                    && nextNodeRow < heightMap.length
                                    && nextNodeCol > -1
                                    && nextNodeCol < heightMap[0].length) {
                                node.addNeighbourNode(heightMap[nextNodeRow][nextNodeCol]);
                            }
                        }
                    }
                }
            }

            PathCollector pathCollector = new PathCollector(heightMap.length, heightMap[0].length);
            calculatePathLengths(startNode, endNode, pathCollector);



            Comparator<BigDecimal> comp = Comparator.nullsFirst(Comparator.naturalOrder());
            comp.compare(new BigDecimal("3"), BigDecimal.ZERO);

            return pathCollector.shortestPathLength - 1; // #vertices = #nodes - 1
        }
    }

    class PathCollector {
        private final int rows;
        private final int cols;
        private LinkedList<HeightNode> currentPathNodes = new LinkedList<>();
        private int currentPathLength = 0;

        private List<HeightNode> shortestPathNodes = List.of();
        private int shortestPathLength = 0;

        private boolean pathRegistered = false;

        public PathCollector(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }

        void pushNode(HeightNode node) {
            node.setVisited(true);
            currentPathNodes.add(node);
            currentPathLength++;
        }

        void popNode() {
            HeightNode node = currentPathNodes.removeLast();
            currentPathLength--;
            node.setVisited(false);
        }

        void register() {
            System.out.println("Path found, length " + currentPathLength);
            printCurrentPath();
            if (!pathRegistered || currentPathLength < shortestPathLength) {
                System.out.println("Path registered as shortest path");
                shortestPathNodes = List.copyOf(currentPathNodes);
                shortestPathLength = currentPathLength;
                pathRegistered = true;
            }
            System.out.println();
        }

        private void printCurrentPath() {
            Map<String, String> directions = new HashMap<>();
            HeightNode previousNode = null;
            for (HeightNode currentPathNode : currentPathNodes) {
                if (previousNode != null) {
                    directions.put(
                            "" + previousNode.row + "," + previousNode.col,
                            currentPathNode.row - previousNode.row == 1 ? "v" : currentPathNode.row - previousNode.row == -1 ? "^" : currentPathNode.col - previousNode.col == 1 ? ">" : "<");
                }
                previousNode = currentPathNode;
            }
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    System.out.print(directions.getOrDefault("" + r + "," + c, "."));
                }
                System.out.println();
            }
        }
    }

    private void calculatePathLengths(HeightNode node, HeightNode endNode, PathCollector pathCollector) {
        pathCollector.pushNode(node);
        if (node == endNode) {
            pathCollector.register();
        } else if (!pathCollector.pathRegistered || pathCollector.shortestPathLength > pathCollector.currentPathLength + 1)
            for (HeightNode outgoingNode : node.outgoingNodes) {
                if (!outgoingNode.visited)
                    calculatePathLengths(outgoingNode, endNode, pathCollector);
            }
        pathCollector.popNode();
    }

    @NotNull
    private static HeightNode[][] parseMapHeights(Stream<String> lines) {
        AtomicInteger row = new AtomicInteger();
        AtomicInteger col = new AtomicInteger();
        return lines
                .map(line -> Arrays.stream(line.split(""))
                        .map(single -> single.equals("S") ? 0 : single.equals("E") ? 27 : single.charAt(0) - 'a' + 1)
                        .map(height -> new HeightNode(height, row.get(), col.getAndIncrement()))
                        .toArray(HeightNode[]::new))
                .peek(r -> row.incrementAndGet())
                .peek(r -> col.set(0))
                .toArray(HeightNode[][]::new);
    }

    private static class HeightNode {
        private final int height;
        private final List<HeightNode> outgoingNodes = new ArrayList<>();
        private final List<HeightNode> blockedNodes = new ArrayList<>();
        private final int row;
        private final int col;
        private boolean visited;

        public HeightNode(int height, int row, int col) {
            this.height = height;
            this.row = row;
            this.col = col;
        }

        public void addNeighbourNode(HeightNode node) {
            if (node.isStartNode() || node.height - height > 1) {
                this.blockedNodes.add(node);
            } else {
                this.outgoingNodes.add(node);
            }
        }

        boolean isStartNode() {
            return height == 0;
        }

        boolean isEndNode() {
            return height == 27;
        }

        boolean visited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        @Override
        public String toString() {
            return "[%2s %2s: %2s %5s ]".formatted(row, col, height, visited);
        }
    }

}
