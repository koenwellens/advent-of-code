package aoc2024.d18;

import aoc2024.common.Input;
import aoc2024.common.Runner;

import java.nio.file.Path;
import java.util.*;

import static aoc2024.common.Collections.union;
import static aoc2024.common.Runner.Run.r;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;

public class D18 {
    public static void main(String[] args) {
        Runner.run("Minimum number of steps", D18::minimumNumberOfSteps,
                r("example.txt", List.of(7, 12), 22),
                r("input.txt", List.of(71, 1024), 360));
        Runner.run("Corrupted byte that makes end unreachable", D18::corruptedByteThatMakesEndUnreachable,
                r("example.txt", List.of(7, 12), "6,1"),
                r("input.txt", List.of(71, 1024), "58,62"));
    }

    private static Integer minimumNumberOfSteps(Path path, List<Object> params) {
        int size = (int) params.getFirst();
        int seconds = (int) params.getLast();

        List<int[]> corruptedBytePositions = parseCorruptedBytePositions(path);
        String[][] grid = new String[size][size];
        rainCorruptedBytes(0, seconds, corruptedBytePositions, grid, false);
//        print(grid);

        return searchNodes(grid, new MinScoreCollector());
    }

    private static String corruptedByteThatMakesEndUnreachable(Path path, List<Object> params) {
        int size = (int) params.getFirst();
        int secondsThatDefinitelyLeadsToEndReachable = (int) params.getLast();

        List<int[]> corruptedBytePositions = parseCorruptedBytePositions(path);
        String[][] grid = new String[size][size];
        rainCorruptedBytes(0, corruptedBytePositions.size(), corruptedBytePositions, grid, false);

        for (int s = corruptedBytePositions.size() - 1; s > secondsThatDefinitelyLeadsToEndReachable; s--) {
            rainCorruptedBytes(s, corruptedBytePositions.size(), corruptedBytePositions, grid, true);
            if (searchNodes(grid, new EndReacheableCollector())) {
                int[] ints = corruptedBytePositions.get(s);
                return ints[1] + "," + ints[0];
            }
        }
        return "END REACHABLE";
    }

    private static void rainCorruptedBytes(int fromSeconds, int toSeconds, List<int[]> corruptedBytePositions, String[][] grid, boolean negative) {
        for (int s = fromSeconds; s < toSeconds; s++) {
            int[] byteLocation = corruptedBytePositions.get(s);
            grid[byteLocation[0]][byteLocation[1]] = negative ? null : "#";
        }
    }

    private static <T> T searchNodes(String[][] grid, ResultCollector<T> collector) {
        int size = grid.length;
        int minScore = MAX_VALUE;
        Position startPosition = new Position(0, 0);
        Position endPosition = new Position(size - 1, size - 1);
        Queue<Head> heads = new LinkedList<>();
        Map<Position, Integer> bestScores = new HashMap<>();
        heads.add(
                new Head(startPosition,
                        Set.of(startPosition),
                        0));

        int[][] directions = {new int[]{-1, 0}, new int[]{1, 0}, new int[]{0, 1}, new int[]{0, -1}};
        while (!heads.isEmpty() && !collector.endReached()) {
            Head currentHead = heads.poll();

            Position currentPosition = currentHead.position;
            for (int[] direction : directions) {
                int nextRow = currentPosition.row + direction[0];
                int nextCol = currentPosition.col + direction[1];
                if (nextRow < 0 || nextRow >= size || nextCol < 0 || nextCol >= size) {
                    continue;
                }

                Position position = new Position(nextRow, nextCol);
                if (currentHead.visited.contains(position))
                    continue;

                String positionValue = grid[position.row][position.col];
                if ("#".equals(positionValue))
                    continue;

                int score = currentHead.score + 1;
                if (score > minScore)
                    continue;

                if (position.equals(endPosition)) {
                    if (score < minScore) {
                        minScore = score;
                        collector.endReached(score);
                    }
                    continue;
                }

                Integer bestScoreForPosition = bestScores.get(position);
                if (bestScoreForPosition == null || score < bestScoreForPosition) {
                    heads.add(new Head(
                            position,
                            union(currentHead.visited, position),
                            score));
                    bestScores.put(position, score);
                }
            }
        }

        return collector.result();
    }

    private static void print(String[][] grid) {
        for (String[] strings : grid) {
            for (int col = 0; col < grid.length; col++) {
                System.out.print(strings[col] == null ? "." : strings[col]);
            }
            System.out.println();
        }
    }

    private static List<int[]> parseCorruptedBytePositions(Path path) {
        return Input.streamInputLines(path)
                .map(line -> line.split(","))
                .map(lineStrings -> new int[]{parseInt(lineStrings[1]), parseInt(lineStrings[0])})
                .toList();
    }

    record Position(int row, int col) {
    }

    record Head(
            Position position,
            Set<Position> visited,
            int score) {
    }

    interface ResultCollector<T> {
        boolean endReached();

        T result();

        void endReached(int score);
    }

    static class MinScoreCollector implements ResultCollector<Integer> {

        private int score;

        @Override
        public boolean endReached() {
            return false;
        }

        @Override
        public Integer result() {
            return score;
        }

        @Override
        public void endReached(int score) {
            this.score = score;
        }
    }

    static class EndReacheableCollector implements ResultCollector<Boolean> {

        private boolean endFound = false;

        @Override
        public boolean endReached() {
            return endFound;
        }

        @Override
        public Boolean result() {
            return endFound;
        }

        @Override
        public void endReached(int score) {
            this.endFound = true;
        }
    }
}
