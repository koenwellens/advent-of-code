package aoc2024.d16;

import aoc2024.common.Collections;
import aoc2024.common.Input;
import aoc2024.common.Runner;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static aoc2024.common.Collections.union;
import static aoc2024.common.Runner.Run.r;
import static java.lang.Integer.MAX_VALUE;

public class D16 {

    public static void main(String[] args) {
        Runner.run("Lowest possible reindeer score", D16::lowestPossibleReindeerScore,
                r("example1.txt", 7036),
                r("example2.txt", 11048),
                r("input.txt", 88468)
        );
        Runner.run("Number of nodes on any shortest path", D16::numberOfNodesOnAnyShortestPath,
                r("example1.txt", 45),
                r("example2.txt", 64),
                r("input.txt", 616)
        );
    }

    private static Integer numberOfNodesOnAnyShortestPath(Path path) {
        return findBestPath(path).second().size();
    }

    private static Integer lowestPossibleReindeerScore(Path path) {
        return findBestPath(path).first();
    }

    private static Collections.Tuple2<Integer, Set<Position>> findBestPath(Path path) {
        int[] start = new int[2];
        AtomicInteger rowCount = new AtomicInteger(0);
        String[][] grid = Input.streamInputLines(path)
                .peek(line -> {
                    int row = rowCount.getAndIncrement();
                    int startIndex = line.indexOf("S");
                    if (startIndex != -1) {
                        start[0] = row;
                        start[1] = startIndex;
                    }
                })
                .map(line -> line.split(""))
                .toArray(String[][]::new);


        int minScore = MAX_VALUE;
        Queue<Head> heads = new LinkedList<>();
        Map<PositionPathKey, Integer> bestScores = new HashMap<>();
        Map<PositionPathKey, Set<PositionPathKey>> bestScorePositions = new HashMap<>();
        Set<Set<PositionPathKey>> pathsWithMinScore = new HashSet<>();
        Position startPosition = new Position(start[0], start[1]);
        PositionPathKey startPositionPathKey = new PositionPathKey(startPosition, Direction.DIR_RIGHT);
        heads.add(
                new Head(startPosition,
                        Direction.DIR_RIGHT,
                        Set.of(startPositionPathKey),
                        0));
        bestScorePositions.put(startPositionPathKey,
                Set.of(startPositionPathKey));

        while (!heads.isEmpty()) {
            Head currentHead = heads.poll();

            for (Direction direction : Direction.values()) {
                if (direction == currentHead.direction.opposite())
                    continue;

                Position position = currentHead.position.add(direction);
                PositionPathKey positionPathKey = new PositionPathKey(position, direction);
                if (currentHead.visited2.contains(positionPathKey))
                    continue;

                String positionValue = grid[position.row][position.col];
                if (positionValue.equals("#"))
                    continue;

                int score = currentHead.score + (currentHead.direction == direction ? 1 : 1001);
                if (score > minScore)
                    continue;

                if (positionValue.equals("E")) {
                    if (score < minScore) {
                        minScore = score;
                        pathsWithMinScore.clear();
                    }
                    pathsWithMinScore.add(union(currentHead.visited2, positionPathKey));
                }

                Integer bestScoreForPosition = bestScores.get(positionPathKey);
                if (bestScoreForPosition == null || score < bestScoreForPosition) {
                    // adding head should not be executed anymore if previous block ("E") executed
                    heads.add(new Head(
                            position,
                            direction,
                            union(currentHead.visited2, positionPathKey),
                            score));
                    bestScores.put(positionPathKey, score);
                    bestScorePositions.put(positionPathKey, new HashSet<>(union(currentHead.visited2, positionPathKey)));
                } else if (score == bestScoreForPosition) {
                    bestScorePositions.get(positionPathKey).addAll(currentHead.visited2);
                    bestScorePositions.get(positionPathKey).add(positionPathKey);
                }
            }
        }

        Set<Position> allNodesOnAnyPathWithMinScore = pathsWithMinScore.stream()
                .flatMap(Set::stream)
                .flatMap(pad -> bestScorePositions.get(pad).stream().map(PositionPathKey::position))
                .collect(Collectors.toSet());

        return new Collections.Tuple2<>(minScore, allNodesOnAnyPathWithMinScore);
    }

    record PositionPathKey(Position position, Direction direction) {
    }

    record Position(int row, int col) {
        Position add(Direction dir) {
            return new Position(
                    row + dir.diffRow(),
                    col + dir.diffCol());
        }
    }

    record Head(
            Position position,
            Direction direction,
            Set<PositionPathKey> visited2,
            int score) {
    }

    enum Direction {
        DIR_UP,
        DIR_LEFT,
        DIR_DOWN,
        DIR_RIGHT;

        Direction opposite() {
            return switch (this) {
                case DIR_UP -> DIR_DOWN;
                case DIR_DOWN -> DIR_UP;
                case DIR_LEFT -> DIR_RIGHT;
                case DIR_RIGHT -> DIR_LEFT;
            };
        }

        public int diffRow() {
            return switch (this) {
                case DIR_UP -> -1;
                case DIR_DOWN -> 1;
                default -> 0;
            };
        }

        public int diffCol() {
            return switch (this) {
                case DIR_LEFT -> -1;
                case DIR_RIGHT -> 1;
                default -> 0;
            };
        }
    }
}
