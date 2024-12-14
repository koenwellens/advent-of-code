package aoc2024.d10;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static aoc2024.common.Input.streamInputLines;
import static aoc2024.common.Runner.run;

public class D10 {
    public static void main(String[] args) {
        run("Message", D10::sumOfTrailHeadScores,
                "example.txt", 36,
                "input.txt", 816
        );
        run("Message", D10::sumOfTrailHeadRatings,
                "example.txt", 81,
                "input.txt", 1960
        );
    }

    private static int sumOfTrailHeadScores(Path path) {
        return sumOfTrailHeadThingies(path, TopsReachedCollector::new);
    }

    private static int sumOfTrailHeadRatings(Path path) {
        return sumOfTrailHeadThingies(path, UniquePathsCollector::new);
    }

    private static int sumOfTrailHeadThingies(Path path, Supplier<Collector> collectorSupplier) {
        AtomicInteger mapLineNr = new AtomicInteger(0);
        List<Position> trailHeads = new ArrayList<>();
        Map<Position, Integer> heights = new HashMap<>();
        AtomicInteger columnsWrapper = new AtomicInteger(-1);
        streamInputLines(path)
                .map(line -> line.split(""))
                .forEach(mapLine -> {
                    columnsWrapper.set(mapLine.length);
                    int lineNr = mapLineNr.getAndIncrement();
                    for (int c = 0; c < columnsWrapper.get(); c++) {
                        Position position = new Position(c, lineNr);
                        int height = Integer.parseInt(mapLine[c]);
                        heights.put(position, height);
                        if (height == 0)
                            trailHeads.add(position);
                    }
                });

        final int rows = mapLineNr.get();
        final int columns = columnsWrapper.get();

        AtomicInteger topsReached = new AtomicInteger();
        trailHeads.forEach(trailHead -> {
            Collector collector = collectorSupplier.get();
            findTrail(trailHead, collector, heights, rows, columns);
            topsReached.addAndGet(collector.size());
        });

        return topsReached.get();
    }

    private static void findTrail(Position current, Collector topsReached, Map<Position, Integer> heights, int rows, int columns) {
        findTrail(current, new HashSet<>(), topsReached, heights, rows, columns);
    }

    private static void findTrail(Position current, HashSet<Position> visited, Collector collector, Map<Position, Integer> heights, int rows, int columns) {
        visited.add(current);
        for (Position neighbour : current.neighbours()) {
            if (neighbour.x >= 0 && neighbour.x < columns && neighbour.y >= 0 && neighbour.y < rows) {
                if (!visited.contains(neighbour)
                        && heights.get(neighbour) == heights.get(current) + 1) {
                    if (heights.get(neighbour) == 9) {
                        collector.add(neighbour);
                    } else {
                        findTrail(neighbour, visited, collector, heights, rows, columns);
                    }
                }
            }
        }
        visited.remove(current);
    }

    record Position(int x, int y) {
        public Position[] neighbours() {
            return new Position[]{
                    new Position(x - 1, y),
                    new Position(x + 1, y),
                    new Position(x, y - 1),
                    new Position(x, y + 1)
            };
        }
    }

    interface Collector {
        void add(Position position);

        int size();
    }

    static class TopsReachedCollector implements Collector {
        Set<Position> topsReached = new HashSet<>();

        @Override
        public void add(Position position) {
            topsReached.add(position);
        }

        @Override
        public int size() {
            return topsReached.size();
        }
    }

    static class UniquePathsCollector implements Collector {

        private int count = 0;
        @Override
        public void add(Position position) {
            count++;
        }

        @Override
        public int size() {
            return count;
        }
    }
}
