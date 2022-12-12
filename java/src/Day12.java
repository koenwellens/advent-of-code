import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Day12 implements DaySolution<Integer> {

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
                                node.addOutgoingNode(heightMap[nextNodeRow][nextNodeCol]);
                            }
                        }
                    }
                }
            }

            Set<HeightNode> nodesToProcess = Set.of(endNode);
            while (!nodesToProcess.contains(startNode)) {
                System.out.println("Processing " + nodesToProcess.size() + " nodes at distance " + nodesToProcess.iterator().next().getDistance());
                nodesToProcess = setDistancesForNeighboursOfNode(nodesToProcess);
            }

            System.out.println("Shortest path distance is " + startNode.distance);

            return startNode.distance;
        }
    }

    private Set<HeightNode> setDistancesForNeighboursOfNode(Set<HeightNode> nodesToProcess) {
        Set<HeightNode> processedNodes = new HashSet<>();
        for (HeightNode nodeToProcess : nodesToProcess) {
            for (HeightNode precedingNode : nodeToProcess.precedingNodes) {
                precedingNode.setDistanceVia(nodeToProcess);
                processedNodes.add(precedingNode);
            }
            for (HeightNode outgoingNode : nodeToProcess.outgoingNodes) {
                outgoingNode.precedingNodes.remove(nodeToProcess);
            }
        }
        return processedNodes;
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

    private static int[] findHighestNode(HeightNode[][] heightMap) {
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[i].length; j++) {
                if (heightMap[i][j].height == 27)
                    return new int[]{i, j};
            }
        }
        throw new IllegalArgumentException();
    }

    private static class HeightNode {
        private final int height;
        private final List<HeightNode> outgoingNodes = new ArrayList<>();
        private final int row;
        private final int col;
        private List<HeightNode> precedingNodes = new ArrayList<>();

        private int distance = -1;

        public HeightNode(int height, int row, int col) {
            this.height = height;
            this.row = row;
            this.col = col;
        }

        public void addOutgoingNode(HeightNode node) {
            if (!node.isStartNode() && node.height <= height + 1) {
                this.outgoingNodes.add(node);
                node.addPrecedingNode(this);
            }
        }

        private void addPrecedingNode(HeightNode node) {
            this.precedingNodes.add(node);
        }

        boolean isStartNode() {
            return height == 0;
        }

        boolean isEndNode() {
            return height == 27;
        }

        @Override
        public String toString() {
            return "[%2s %2s: %2s]".formatted(row, col, height);
        }

        public int getDistance() {
            if (isEndNode())
                return 0;
            return distance;
        }

        public void setDistanceVia(HeightNode node) {
            int calculatedDistance = node.getDistance() + 1;
            this.precedingNodes.remove(node);
            if (this.distance == -1 || calculatedDistance < this.distance)
                this.distance = calculatedDistance;
        }
    }

}
