package aoc2024.d01;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static aoc2024.common.Input.streamInputLines;
import static aoc2024.common.Runner.run;
import static java.lang.Integer.parseInt;
import static java.util.Comparator.comparing;

class D01 {

    public static void main(String[] args) {
        run("Sum of distances", D01::sumOfDistances, "example.txt", 11);
        run("Sum of distances", D01::sumOfDistances, "input.txt", 2192892);
        run("Similarity", D01::similarity, "example.txt", 31);
        run("Similarity", D01::similarity, "input.txt", 22962826);
    }

    private static Integer sumOfDistances(Path input) {
        Set<StarInfo> first = new TreeSet<>();
        Set<StarInfo> second = new TreeSet<>();
        AtomicInteger index = new AtomicInteger(0);
        streamInputLines(input)
                .map(line -> line.split("\\s+"))
                .forEach(couple -> {
                    first.add(new StarInfo(index.getAndIncrement(), parseInt(couple[0])));
                    second.add(new StarInfo(index.getAndIncrement(), parseInt(couple[1])));
                });

        Iterator<StarInfo> firstIterator = first.iterator();
        Iterator<StarInfo> secondIterator = second.iterator();
        int sumDistance = 0;
        while (firstIterator.hasNext() && secondIterator.hasNext()) {
            StarInfo next1 = firstIterator.next();
            StarInfo next2 = secondIterator.next();
            int distance = Math.abs(next1.id - next2.id);
            sumDistance += distance;
        }

        return sumDistance;
    }

    private static int similarity(Path input) {
        List<Integer> firstColIds = new ArrayList<>();
        Map<Integer, Integer> secondColIdOccurrences = new HashMap<>();
        streamInputLines(input)
                .map(line -> line.split("\\s+"))
                .forEach(couple -> {
                    firstColIds.add(parseInt(couple[0]));
                    secondColIdOccurrences.merge(parseInt(couple[1]), 1, Integer::sum);
                });

        int similarity = 0;
        for (Integer id : firstColIds) {
            similarity += id * secondColIdOccurrences.getOrDefault(id, 0);
        }
        return similarity;
    }

    record StarInfo(Integer row, Integer id) implements Comparable<StarInfo> {
        @Override
        public int compareTo(StarInfo o) {
            return comparing((Function<StarInfo, Integer>) starInfo -> starInfo.id)
                    .thenComparing(starInfo -> starInfo.row)
                    .compare(this, o);
        }
    }
}