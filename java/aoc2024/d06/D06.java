package aoc2024.d06;

import aoc2024.common.Input;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static aoc2024.common.Runner.run;

public class D06 {
    public static void main(String[] args) {
        run("Number of distinct positions visited by guard", D06::countDistinctPositionsVisited, "example.txt", 41);
        run("Number of distinct positions visited by guard", D06::countDistinctPositionsVisited, "input.txt", 4973);
        run("Number of distinct positions visited by guard", D06::countDistinctPositionsVisited, "example.txt", 41);
    }

    public static int countDistinctPositionsVisited(Path path) {
        AtomicInteger rowCounter = new AtomicInteger();
        AtomicBoolean guardPositionFound = new AtomicBoolean(false);
        final int[] initialGuardCoordinates = new int[2]; // row, column
        String[][] map = Input.streamInputLines(path)
                .map(line -> {
                    String[] columns = line.split("");
                    if (!guardPositionFound.get())
                        for (int c = 0; c < columns.length; c++) {
                            if (columns[c].equals("^")) {
                                initialGuardCoordinates[0] = rowCounter.get();
                                initialGuardCoordinates[1] = c;
                                guardPositionFound.set(true);
                            }
                        }
                    rowCounter.incrementAndGet();
                    return columns;
                })
                .toArray(String[][]::new);

        Set<Position> visitedPositions = new HashSet<>();
        int[][] directions = new int[][]{
                new int[]{-1, 0},
                new int[]{0, 1},
                new int[]{1, 0},
                new int[]{0, -1}};
        int directionIndex = 0;
        visitedPositions.add(new Position(initialGuardCoordinates[0], initialGuardCoordinates[1]));
        int[] guardPosition = initialGuardCoordinates;
        while (true) {
            int[] direction = directions[directionIndex];
            int[] nextGuardCoordinates = new int[]{
                    guardPosition[0] + direction[0],
                    guardPosition[1] + direction[1],
            };
            if (nextGuardCoordinates[0] < 0
                    || nextGuardCoordinates[0] >= map.length
                    || nextGuardCoordinates[1] < 0
                    || nextGuardCoordinates[1] >= map[0].length)
                break;
            else if (map[nextGuardCoordinates[0]][nextGuardCoordinates[1]].equals("#")) {
                directionIndex = (directionIndex + 1) % directions.length;
            } else {
                visitedPositions.add(new Position(nextGuardCoordinates[0], nextGuardCoordinates[1]));
                guardPosition = nextGuardCoordinates;
            }
        }

        return visitedPositions.size();
    }

    record Position(int row, int col) {}
}
