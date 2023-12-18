package adventofcode2023.day12;

import adventofcode2023.common.Input;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day12.Day12Input.EXAMPLE;
import static adventofcode2023.day12.Day12Input.INPUT;

class Day12 {
    public static void main(String[] args) {
        runnerFor(Day12.class)
                .execute("Puzzle 1", Day12::sumOfNumberOfPossibleArrangementsPerRow).withInput(EXAMPLE).expect(21)
                .execute("Puzzle 1", Day12::sumOfNumberOfPossibleArrangementsPerRow).withInput(INPUT).expect(7221)
//                .execute("Puzzle 2", Day12::sumOfNumberOfPossibleArrangementsPerUnfoldedRow).withInput(EXAMPLE).expect(525152)
                .build()
                .run();
    }

    private static int sumOfNumberOfPossibleArrangementsPerRow(Input springMapData) {
        return springMapData.streamLinesMappedToInt(springRowData -> calculateNumberOfPossibleArrangementsForRow(springRowData, false))
                .parallel()
//                .sequential()
                .sum();
    }

    private static int sumOfNumberOfPossibleArrangementsPerUnfoldedRow(Input springMapData) {
        return springMapData.streamLinesMappedToInt(springRowData -> calculateNumberOfPossibleArrangementsForRow(springRowData, true))
                .parallel()
//                .sequential()
                .sum();
    }

    private static Integer calculateNumberOfPossibleArrangementsForRow(String springRowData, boolean unfolded) {
        String[] split = springRowData.split(" ");

        int[] expectedBrokenSpringGroupSizes = Arrays.stream(split[1].split(",")).mapToInt(Integer::parseInt).toArray();
        String springsPattern = split[0];

        if (unfolded) {
            String unfoldedSpringsPattern = IntStream.range(0, 5)
                    .mapToObj(i -> springsPattern)
                    .collect(Collectors.joining("?"));
            int[] unfoldedExpectedBrokenSpringGroupSizes = IntStream.range(0, 5)
                    .flatMap(i -> Arrays.stream(expectedBrokenSpringGroupSizes))
                    .toArray();
            int calculated = calculate("", unfoldedSpringsPattern, unfoldedExpectedBrokenSpringGroupSizes);
            System.out.println("finished " + springsPattern + " " + calculated);
            return calculated;
        } else
            return calculate("", springsPattern, expectedBrokenSpringGroupSizes);
    }

    private static int calculate(String completedPattern, String remainingPattern, int[] expectedBrokenSpringGroupSizes) {
        if (remainingPattern.isEmpty()) {
            int result = Arrays.equals(
                    Arrays.stream(completedPattern.split("\\.")).filter(part -> !part.isEmpty()).mapToInt(String::length).toArray(),
                    expectedBrokenSpringGroupSizes)
                    ? 1
                    : 0;

//            System.out.println("\t" + result + " -> " + completedPattern);

            return result;
        }

        if (remainingPattern.charAt(0) == '?') {
            return calculate(completedPattern + ".", remainingPattern.substring(1), expectedBrokenSpringGroupSizes)
                    + calculate(completedPattern + "#", remainingPattern.substring(1), expectedBrokenSpringGroupSizes);
        }

        return calculate(completedPattern + remainingPattern.charAt(0), remainingPattern.substring(1), expectedBrokenSpringGroupSizes);
    }

}
