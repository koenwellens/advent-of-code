package aoc2024.d06;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static aoc2024.common.Input.streamInputLines;
import static aoc2024.common.Runner.run;
import static aoc2024.d06.D06.Direction.*;

public class D06 {
    public static void main(String[] args) {
        run("Number of distinct positions visited by guard", D06::countDistinctPositionsVisited, "example.txt", 41);
        run("Number of distinct positions visited by guard", D06::countDistinctPositionsVisited, "input.txt", 4973);
//        run("Number of distinct positions visited by guard", D06::countPossibleWaysToCreateLoop, "example.txt", 6);
    }

    private static int countPossibleWaysToCreateLoop(Path path) {
        AtomicInteger rowCounter = new AtomicInteger();
        final int[] initialGuardCoordinates = new int[2]; // row, column
//        List<Position> obstaclePositions = new ArrayList<>();
        Map<Integer, List<Position>> obstaclesByRow = new HashMap<>();
        Map<Integer, List<Position>> obstaclesByColumn = new HashMap<>();
        String[][] map = streamInputLines(path)
                .map(line -> {
                    String[] columns = line.split("");
                    for (int c = 0; c < columns.length; c++) {
                        if (columns[c].equals("#")) {
                            Position newObstaclePosition = new Position(rowCounter.get(), c);
                            obstaclesByRow.computeIfAbsent(rowCounter.get(), ArrayList::new).add(newObstaclePosition);
                            obstaclesByColumn.computeIfAbsent(c, ArrayList::new).add(newObstaclePosition);
                        } else if (columns[c].equals("^")) {
                            initialGuardCoordinates[0] = rowCounter.get();
                            initialGuardCoordinates[1] = c;
                        }
                    }
                    rowCounter.incrementAndGet();
                    return columns;
                })
                .toArray(String[][]::new);

//        Position initialGuardPosition = new Position(initialGuardCoordinates[0], initialGuardCoordinates[1]);
        Map<Direction, Map<Integer, List<Position>>> obstacleSearchSpaces = new HashMap<>();
        obstacleSearchSpaces.put(UP, obstaclesByColumn);
        obstacleSearchSpaces.put(DOWN, obstaclesByColumn);
        obstacleSearchSpaces.put(LEFT, obstaclesByRow);
        obstacleSearchSpaces.put(RIGHT, obstaclesByRow);

        for (Position extraObstaclePosition = new Position(0, 0);
             extraObstaclePosition.row < map.length; ) {
            if (!map[extraObstaclePosition.row][extraObstaclePosition.col].equals("#") && !map[extraObstaclePosition.row][extraObstaclePosition.col].equals("^")) {
                obstaclesByRow.get(extraObstaclePosition.row).add(extraObstaclePosition);
                obstaclesByColumn.get(extraObstaclePosition.col).add(extraObstaclePosition);

                Direction direction = UP;
                Position guardPosition = new Position(initialGuardCoordinates[0], initialGuardCoordinates[1]);
                while (true) {
                    Function<Position, Integer> searchSpaceSelector = direction == UP || direction == DOWN
                            ? position -> position.col
                            : position -> position.row;
                    Function<Position, Integer> searchSpaceIndexSelector = direction == UP || direction == DOWN
                            ? position -> position.row
                            : position -> position.col;
                    List<Position> obstacleSearchSpace = obstacleSearchSpaces
                            .get(direction)
                            .get(searchSpaceSelector.apply(guardPosition));
                    UnaryOperator<Integer> delta = direction == UP || direction == LEFT
                            ? co -> co - 1
                            : co -> co + 1;

                    List<Position> obstaclePositions = obstacleSearchSpaces
                            .get(direction)
                            .get(searchSpaceSelector.apply(guardPosition));

                }
            }

            int extraObstaclePositionRow = extraObstaclePosition.row;
            int extraObstaclePositionCol = extraObstaclePosition.col + 1;
            if (extraObstaclePositionCol == map[0].length) {
                extraObstaclePositionCol = 0;
                extraObstaclePositionRow++;
            }
            extraObstaclePosition = new Position(extraObstaclePositionRow, extraObstaclePositionCol);
        }

        return 0;
    }

    public static int countDistinctPositionsVisited(Path path) {
        AtomicInteger rowCounter = new AtomicInteger();
        AtomicBoolean guardPositionFound = new AtomicBoolean(false);
        final int[] initialGuardCoordinates = new int[2]; // row, column
        String[][] map = streamInputLines(path)
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

    enum Direction {
        UP, DOWN, LEFT, RIGHT;

        Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> UP;
            };
        }

        Direction next() {
            return switch (this) {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };
        }
    }

    record Position(int row, int col) {
        Position move(Direction direction) {
            return switch (direction) {
                case UP -> new Position(row - 1, col);
                case DOWN -> new Position(row + 1, col);
                case LEFT -> new Position(row, col - 1);
                case RIGHT -> new Position(row, col + 1);
            };
        }
    }

    record ObstacleHit(Position position, Direction direction) {
    }
}
