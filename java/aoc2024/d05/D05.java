package aoc2024.d05;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static aoc2024.common.Input.streamInputLines;
import static aoc2024.common.Runner.run;
import static java.lang.Integer.parseInt;

public class D05 {
    public static void main(String[] args) {
        run("Sum of middle pages in correctly ordered updates", D05::sumOfMiddlePageNumbers, "example.txt", 143L);
        run("Sum of middle pages in correctly ordered updates", D05::sumOfMiddlePageNumbers, "input.txt", 5064L);
        run("Sum of middle pages in corrected updates", D05::sumOfMiddlePageNumbersInCorrectedUpdates, "example.txt", 123L);
        run("Sum of middle pages in corrected updates", D05::sumOfMiddlePageNumbersInCorrectedUpdates, "input.txt", 5152L);
    }

    private static Long sumOfMiddlePageNumbers(Path path) {
        return sumOfMiddlePageNumbers(path, new CorrectUpdateHandler());
    }

    private static Long sumOfMiddlePageNumbersInCorrectedUpdates(Path path) {
        return sumOfMiddlePageNumbers(path, new IncorrectUpdateHandler());
    }

    private static Long sumOfMiddlePageNumbers(Path path, ValuesHandler valuesHandler) {
        AtomicBoolean loadingUpdates = new AtomicBoolean(false);
        streamInputLines(path)
                .filter(line -> !line.isEmpty())
                .peek(line -> {
                    if (line.contains(","))
                        loadingUpdates.set(true);
                })
                .map(line -> line.split("[|,]"))
                .forEach(values -> {
                    if (loadingUpdates.get()) {
                        valuesHandler.handle(values);
                    } else {
                        valuesHandler.addRule(values);
                    }
                });
        return valuesHandler.getResult();
    }

    abstract static class ValuesHandler {

        protected final AtomicInteger accumulator = new AtomicInteger(0);
        protected final Map<String, Set<String>> beforeRules = new HashMap<>();
        protected final Map<String, Set<String>> afterRules = new HashMap<>();

        abstract void handle(String[] values, boolean correctOrder);

        long getResult() {
            return accumulator.get();
        }


        void handle(String[] values) {
            boolean correctOrder = true;
            for (int i = 0; correctOrder && i < values.length - 1; i++) {
                String currentValue = values[i];
                for (int j = i + 1; j < values.length; j++) {
                    String nextValue = values[j];
                    correctOrder = correctOrder
                            && (beforeRules.getOrDefault(currentValue, Set.of()).contains(nextValue)
                            || afterRules.getOrDefault(nextValue, Set.of()).contains(currentValue));
                }
            }
            handle(values, correctOrder);
        }

        void addMiddlePageNumberToResult(String[] values) {
            int middlePage = values.length / 2;
            int middleValue = parseInt(values[middlePage]);
            accumulator.addAndGet(middleValue);
        }

        public void addRule(String[] values) {
            beforeRules.computeIfAbsent(values[0], k -> new HashSet<>()).add(values[1]);
            afterRules.computeIfAbsent(values[1], k -> new HashSet<>()).add(values[0]);
        }
    }

    static class CorrectUpdateHandler extends ValuesHandler {

        @Override
        public void handle(String[] values, boolean correctOrder) {
            if (correctOrder) {
                addMiddlePageNumberToResult(values);
            }
        }
    }

    static class IncorrectUpdateHandler extends ValuesHandler {

        @Override
        public void handle(String[] values, boolean correctOrder) {
            if (!correctOrder) {
                String[] sortedValues = sort(values);
                addMiddlePageNumberToResult(sortedValues);
            }
        }

        private String[] sort(String[] values) {
            String[] sortedValues = Arrays.copyOf(values, values.length, String[].class);
            Arrays.sort(sortedValues, new Comparator<String>() {
                @Override
                public int compare(String v1, String v2) {
                    if (beforeRules.getOrDefault(v1, Set.of()).contains(v2))
                        return -1;
                    if (afterRules.getOrDefault(v2, Set.of()).contains(v1))
                        return -1;
                    return 1;
                }
            });
            return sortedValues;
        }

    }
}
