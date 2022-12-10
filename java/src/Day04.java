import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class Day04 implements DaySolution<Integer> {

    @Override
    public Integer part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            long count = rangePairs(lines)
                    .filter(rangePair ->
                            (rangePair[0][0] <= rangePair[1][0] && rangePair[0][1] >= rangePair[1][1])
                                    || (rangePair[0][0] >= rangePair[1][0] && rangePair[0][1] <= rangePair[1][1]))
                    .count();


            System.out.printf("There are %s assignment pairs in which one range fully contain the other\n", count);

            return (int) count;
        }
    }

    @Override
    public Integer part2(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            long count = rangePairs(lines)
                    .filter(rangePair ->
                            !(rangePair[0][0] > rangePair[1][1] || rangePair[1][0] > rangePair[0][1]))
                    .count();

            System.out.printf("There are %s assignment pairs with overlapping ranges\n", count);

            return (int) count;
        }
    }

    @NotNull
    private Stream<int[][]> rangePairs(Stream<String> lines) {
        return lines
                .map(this::parseRangePair);
    }

    private int[][] parseRangePair(String line) {
        String[] rangeStrings = line.split(",");
        String[] firstRangeSplit = rangeStrings[0].split("-");
        String[] secondRangeSplit = rangeStrings[1].split("-");
        return new int[][]{
                new int[]{
                        parseInt(firstRangeSplit[0]),
                        parseInt(firstRangeSplit[1])},
                new int[]{
                        parseInt(secondRangeSplit[0]),
                        parseInt(secondRangeSplit[1])}};
    }
}
