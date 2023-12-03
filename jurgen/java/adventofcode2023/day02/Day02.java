package adventofcode2023.day02;

import adventofcode2023.common.Input;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static adventofcode2023.day02.Day02Input.INPUT;
import static adventofcode2023.day02.Day02Input.PART_1_EXAMPLE;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.*;

public class Day02 {

    public static void main(String[] args) {
        System.out.println("Puzzle 1 example: " + sumOfIndexesOfPossibleGames(PART_1_EXAMPLE)); // expected: 8
        System.out.println("Puzzle 1 input: " + sumOfIndexesOfPossibleGames(INPUT)); // expected: 2149
        System.out.println("Puzzle 2 example: " + sumOfPowersOfMinimalCubeSetsRequiredForGame(PART_1_EXAMPLE)); // expected: 2286
        System.out.println("Puzzle 2 input: " + sumOfPowersOfMinimalCubeSetsRequiredForGame(INPUT)); // expected: 71274
    }

    private static int sumOfIndexesOfPossibleGames(Input allGamesData) {
        // number of cubes of each color in the game
        Map<String, Integer> gameCubeCount = Map.of(
                "red", 12,
                "green", 13,
                "blue", 14);

        return allGamesData
                .streamLines(line -> line.split(": ")) // [0]=gameIdString [1]=gameCubeSetsString
                .filter(splitLine -> streamIndividualCubeCounts(splitLine[1])
                        .noneMatch(cubeCount -> parseInt(cubeCount[0]) > gameCubeCount.get(cubeCount[1])))
                .map(splitLine -> splitLine[0])
                .map(gameIdString -> gameIdString.substring(5)) // remove leading "Game " string
                .mapToInt(Integer::parseInt)
                .sum();
    }

    private static int sumOfPowersOfMinimalCubeSetsRequiredForGame(Input allGamesData) {
        return allGamesData
                .streamLines(line -> line.split(": ")) // [0]=gameIdString [1]=gameCubeSetsString
                .mapToInt(splitLine -> streamIndividualCubeCounts(splitLine[1])
                        .collect(
                                groupingBy(
                                        cubeCount -> cubeCount[1], // group by color
                                        collectingAndThen(
                                                mapping(cubeCountUnparsed -> parseInt(cubeCountUnparsed[0]), maxBy(Integer::compareTo)), // keep only the max value for this color
                                                Optional::orElseThrow)))
                        .values()
                        .stream()
                        .reduce((a, b) -> a * b) // multiply max values for each color
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
