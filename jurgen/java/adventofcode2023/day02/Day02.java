package adventofcode2023.day02;

import adventofcode2023.common.Input;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static adventofcode2023.common.IntOperations.MULTIPLY;
import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day02.Day02Input.PART_1_EXAMPLE;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.*;

class Day02 {

    public static void main(String[] args) {
        runnerFor(Day02.class)
                .execute("Puzzle 1", Day02::sumOfIndexesOfPossibleGames).withInput(Day02Input.PART_1_EXAMPLE).expect(8)
                .execute("Puzzle 1", Day02::sumOfIndexesOfPossibleGames).withInput(Day02Input.INPUT).expect(2149)
                .execute("Puzzle 2", Day02::sumOfPowersOfMinimalCubeSetsRequiredForGame).withInput(PART_1_EXAMPLE).expect(2286)
                .execute("Puzzle 2", Day02::sumOfPowersOfMinimalCubeSetsRequiredForGame).withInput(Day02Input.INPUT).expect(71274)
                .build()
                .run();
    }

    private static int sumOfIndexesOfPossibleGames(Input allGamesData) {
        // number of cubes of each color in the game
        Map<String, Integer> gameCubeCount = Map.of(
                "red", 12,
                "green", 13,
                "blue", 14);

        return allGamesData
                .streamLinesMapped(line -> line.split(": ")) // [0]=gameIdString [1]=gameCubeSetsString
                .filter(splitLine -> streamIndividualCubeCounts(splitLine[1])
                        .noneMatch(cubeCount -> parseInt(cubeCount[0]) > gameCubeCount.get(cubeCount[1])))
                .map(splitLine -> splitLine[0])
                .map(gameIdString -> gameIdString.substring(5)) // remove leading "Game " string
                .mapToInt(Integer::parseInt)
                .sum();
    }

    private static int sumOfPowersOfMinimalCubeSetsRequiredForGame(Input allGamesData) {
        return allGamesData
                .streamLinesMapped(line -> line.split(": ")) // [0]=gameIdString [1]=gameCubeSetsString
                .mapToInt(splitLine -> streamIndividualCubeCounts(splitLine[1])
                        .collect(
                                groupingBy(
                                        cubeCount -> cubeCount[1], // group by color
                                        collectingAndThen(
                                                mapping(cubeCountUnparsed -> parseInt(cubeCountUnparsed[0]), maxBy(Integer::compareTo)), // keep only the max value for this color
                                                Optional::orElseThrow)))
                        .values()
                        .stream()
                        .reduce(MULTIPLY) // multiply max values for each color
                        .orElseThrow())
                .sum();
    }

    private static Stream<String[]> streamIndividualCubeCounts(String singleGameCubeSetsString) {
        return Arrays.stream(singleGameCubeSetsString.split(";"))
                .flatMap(cubeSetString -> Arrays.stream(cubeSetString.split(",")))
                .map(String::trim)
                .map(cubeCountString -> cubeCountString.split(" "));
    }
}
