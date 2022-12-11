import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Day01 implements DaySolution<Integer> {

    public Integer part1(Path inputFilePath) {
        Elf elfWithMostCalories = getElvesSortedByMostCalories(inputFilePath).findFirst().orElseThrow();
        System.out.printf("Elf with most calories is %s", elfWithMostCalories);
        return elfWithMostCalories.calories;
    }

    public Integer part2(Path inputFilePath) {
        System.out.println("Top 3 elves with most calories are");
        Integer total = getElvesSortedByMostCalories(inputFilePath).limit(3)
                .peek(System.out::println)
                .map(Elf::getCalories)
                .reduce(0, Integer::sum);

        System.out.printf("Totalling %s calories%n", total);

        return total;
    }

    private Stream<Elf> getElvesSortedByMostCalories(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            return lines
                    .map(Day01::parseLine)
                    .reduce(new Accumulator(), Accumulator::add, (a, b) -> a)
                    .elvesSortedByMostCalories();
        }
    }

    private static Integer parseLine(String line) {
        return line == null || line.isEmpty() || line.isBlank()
                ? null
                : Integer.parseInt(line);
    }

    static class Accumulator {
        private final Map<Integer, Elf> groupAccumulations = new HashMap<>();
        private int currentId = 1;

        Accumulator add(Integer integer) {
            if (integer == null) {
                finishGroup();
            } else {
                addToGroup(integer);
            }
            return this;
        }

        private void addToGroup(Integer value) {
            groupAccumulations.computeIfAbsent(currentId, Elf::new).addCalories(value);
        }

        private void finishGroup() {
            currentId++;
        }

        public Stream<Elf> elvesSortedByMostCalories() {
            return groupAccumulations.values().stream().sorted(Comparator.comparing(Elf::getCalories).reversed());
        }
    }

    static class Elf {
        private final Integer id;
        private int calories;

        public Elf(Integer id) {
            this.id = id;
        }

        void addCalories(int calories) {
            this.calories += calories;
        }

        public int getCalories() {
            return calories;
        }

        @Override
        public String toString() {
            return "Elf nr %s: %s calories".formatted(id, calories);
        }
    }
}