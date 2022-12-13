import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day12 implements DaySolution<Integer> {

    @Override
    public Integer part1(Path inputFilePath) {
        return findShortestPathToANodeMatching(inputFilePath, HeightNode::isStartNode);
    }

    @Override
    public Integer part2(Path inputFilePath) {
        return findShortestPathToANodeMatching(inputFilePath, heightNode -> heightNode.height == 1);
    }

    private int findShortestPathToANodeMatching(Path inputFilePath, Predicate<HeightNode> pathStartPredicate) {
        try (Stream<String> lines = lines(inputFilePath)) {
            HeightNode[][] heightMap = parseMapHeights(lines);

            HeightNode endNode = null;
            for (int row = 0; row < heightMap.length; row++) {
                for (int col = 0; col < heightMap[row].length; col++) {
                    HeightNode node = heightMap[row][col];
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
            while (nodesToProcess.stream().noneMatch(pathStartPredicate)) {
                System.out.println("Processing " + nodesToProcess.size() + " nodes at distance " + nodesToProcess.iterator().next().getDistance());
                nodesToProcess = setDistancesForNeighboursOfNode(nodesToProcess);
            }

            HeightNode matchingNode = nodesToProcess.stream().filter(pathStartPredicate).findFirst().orElseThrow();
            System.out.println("Shortest path distance is " + matchingNode.distance);

            return matchingNode.distance;
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
                        .map(single -> {
                            boolean startNode = single.equals("S");
                            boolean endNode = single.equals("E");
                            int height = startNode ? 1 : endNode ? 26 : single.charAt(0) - 'a' + 1;
                            return new HeightNode(height, row.get(), col.getAndIncrement(), startNode, endNode);
                        })
//                        .map(height -> new HeightNode(height, row.get(), col.getAndIncrement()))
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
        private final boolean startNode;
        private final boolean endNode;
        private List<HeightNode> precedingNodes = new ArrayList<>();

        private int distance = -1;

        public HeightNode(int height, int row, int col, boolean startNode, boolean endNode) {
            this.height = height;
            this.row = row;
            this.col = col;
            this.startNode = startNode;
            this.endNode = endNode;
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
            return startNode;
        }

        boolean isEndNode() {
            return endNode;
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
