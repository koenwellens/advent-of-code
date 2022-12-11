import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;

public class Day05 implements DaySolution<String> {

    private static final String CRATE_MOVER_9000 = "CrateMover 9000";
    private static final String CRATE_MOVER_9001 = "CrateMover 9001";

    @Override
    public String part1(Path inputFilePath) {
        return simulateCrateMoving(inputFilePath, CRATE_MOVER_9000);
    }

    @Override
    public String part2(Path inputFilePath) {
        return simulateCrateMoving(inputFilePath, CRATE_MOVER_9001);
    }

    @NotNull
    private String simulateCrateMoving(Path inputFilePath, String crateMoverType) {
        Map<String, Stack> stacks = new HashMap<>();
        try (Stream<String> lines = lines(inputFilePath)) {
            lines
                    .flatMap(line -> parseStackAction(line, crateMoverType))
                    .forEach(action -> action.execute(stacks));

            String topCrates = stacks.values().stream().map(Stack::peek).collect(Collectors.joining());
            System.out.println("Crates on top of stacks are " + topCrates);
            return topCrates;
        }
    }

    private Stream<StackAction> parseStackAction(String line, String crateMoverType) {
//        System.out.println("parsing " + line);
//        if (line.isEmpty()) {
//            return Stream.of(stacks -> {
//                System.out.println("Stacks configured");
//                System.out.println(stacks);
//            });
//        }

        if (line.startsWith("move")) {
            Pattern movePattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
            Matcher moveMatcher = movePattern.matcher(line);
            if (!moveMatcher.matches())
                throw new IllegalArgumentException("Line did not match move pattern: " + line);

            if (crateMoverType.equals(CRATE_MOVER_9000))
                return Stream.of(
                        stacks ->
                                range(0, Integer.parseInt(moveMatcher.group(1)))
                                        .forEach(x -> stacks.get(moveMatcher.group(3)).push(stacks.get(moveMatcher.group(2)).pop())));
            else // CrateMover 9001
                return Stream.of(
                        stacks -> {
                            List<String> poppedCrates = range(0, Integer.parseInt(moveMatcher.group(1)))
                                    .mapToObj(i -> stacks.get(moveMatcher.group(2)).pop())
                                    .collect(Collectors.toList());
                            Collections.reverse(poppedCrates);
                            poppedCrates.forEach(crate -> stacks.get(moveMatcher.group(3)).push(crate));
                        });
        }

        if (line.contains("[")) {
            Pattern cratePattern = Pattern.compile("\\[(\\w)]");
            Matcher matcher = cratePattern.matcher(line);

            return matcher
                    .results()
                    .map(result ->
                            stacks ->
                                    stacks.computeIfAbsent("" + (result.start() / 4 + 1), x -> new Stack())
                                            .pushReverse(result.group(1)));
        }

        return Stream.empty();
    }

    private interface StackAction {
        void execute(Map<String, Stack> stacks);
    }

    static class Stack {
        /*
         * first() is top of stack
         */
        private final LinkedList<String> items = new LinkedList<>();

        /*
         * push an item at the bottom of the stack, because of reverse input order: stack is parsed from top to bottom
         */
        public void pushReverse(String item) {
            items.add(item);
        }

        public String pop() {
            return items.removeFirst();
        }

        public void push(String item) {
            items.addFirst(item);
        }

        public String peek() {
            return items.getFirst();
        }
    }
}
