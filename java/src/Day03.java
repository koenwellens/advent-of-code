import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.stream.Stream;

public class Day03 implements DaySolution<Integer> {

    @Override
    public Integer part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            int sumOfPriorities = lines
                    .map(this::splitLineInHalf)
                    .map(this::findSharedCharacter)
                    .mapToInt(this::toPriority)
                    .sum();

            System.out.println("The sum of the priorities of the misplaced items is " + sumOfPriorities);

            return sumOfPriorities;
        }
    }

    @Override
    public Integer part2(Path inputFilePath) {
        Grouper grouper = Grouper.groupByItemCount(3);
        try (Stream<String> lines = lines(inputFilePath)) {
            int sum = lines.map(grouper::add)
                    .filter(Grouper.Group::isComplete)
                    .map(group -> group.items().get(0)
                            .chars().mapToObj(c -> "" + (char) c)
                            .filter(chr -> group.items().get(1).contains(chr))
                            .filter(chr -> group.items().get(2).contains(chr))
                            .findFirst()
                            .map(s -> s.charAt(0))
                            .orElseThrow())
                    .mapToInt(c -> (int) c)
                    .map(this::toPriority)
                    .sum();

            System.out.println("The sum of the priorities of the badge item types is " + sum);

            return sum;
        }
    }

    @NotNull
    private String[] splitLineInHalf(String line) {
        return new String[]{line.substring(0, line.length() / 2), line.substring(line.length() / 2)};
    }

    private int findSharedCharacter(String[] splitLine) {
        return splitLine[0].chars().filter(chr -> splitLine[1].contains("" + (char) chr)).findFirst().orElseThrow();
    }

    /*
     * priority =
     * - upper case: 26 + offset
     * - lower case: offset
     *
     * ASCII char code
     * A 01000001 -> 01 (ignore) + 0 (upper case) + 00001 (offset within category) -> flip case bit (1) * 26 + offset
     * ...
     * Z 01011010 -> 01 (ignore) + 0 (upper case) + 11010 (offset within category) -> flip case bit (1) * 26 + offset
     * ...
     * a 01100001 -> 01 (ignore) + 1 (lower case) + 00001 (offset within category) -> flip case bit (0) * 26 + offset
     * ...
     * z 01111010 -> 01 (ignore) + 1 (lower case) + 11010 (offset within category) -> flip case bit (0) * 26 + offset
     */
    private int toPriority(Integer s) {
        return (((s & 32) ^ 32) >> 5) * 26 + (s & 31);
    }


}
