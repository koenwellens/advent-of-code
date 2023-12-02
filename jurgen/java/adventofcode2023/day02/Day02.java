package adventofcode2023.day02;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static adventofcode2023.day02.InputData.INPUT;
import static adventofcode2023.day02.InputData.PART_1_EXAMPLE;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.*;

public class Day02 {

    public static void main(String[] args) {
        System.out.println("Puzzle 1 example: " + sumOfIndexesOfPossibleGames(PART_1_EXAMPLE)); // expected: 8
        System.out.println("Puzzle 1 input: " + sumOfIndexesOfPossibleGames(INPUT)); // expected: 2149
        System.out.println("Puzzle 2 example: " + sumOfPowersOfMinimalCubeSetsRequiredForGame(PART_1_EXAMPLE)); // expected: 2286
        System.out.println("Puzzle 2 input: " + sumOfPowersOfMinimalCubeSetsRequiredForGame(INPUT)); // expected: 71274
    }

    private static int sumOfIndexesOfPossibleGames(String allGamesDataString) {
        // number of cubes of each color in the game
        Map<String, Integer> gameCubeCount = Map.of(
                "red", 12,
                "green", 13,
                "blue", 14);

        int sumOfIds = 0;
        for (String singleGameDataString : allGamesDataString.split("\n")) {
            String gameIdString = singleGameDataString.substring(5, singleGameDataString.indexOf(":"));
            boolean gameDataPossible = streamIndividualCubeCounts(singleGameDataString)
                    .noneMatch(cubeCount -> parseInt(cubeCount[0]) > gameCubeCount.get(cubeCount[1])); // as soon as a reported color count surpasses the number of cubes of that color in the game, the entire game becomes impossible
            if (gameDataPossible) {
                sumOfIds += parseInt(gameIdString);
            }
        }

        return sumOfIds;
    }

    private static int sumOfPowersOfMinimalCubeSetsRequiredForGame(String allGamesDataString) {
        int sumOfPowersOfMinimalCubeSetsRequiredForGame = 0;
        for (String singleGameDataString : allGamesDataString.split("\n")) {
            sumOfPowersOfMinimalCubeSetsRequiredForGame +=
                    streamIndividualCubeCounts(singleGameDataString)
                            .collect(
                                    groupingBy(
                                            cubeCount -> cubeCount[1], // group by color
                                            collectingAndThen(
                                                    mapping(cubeCountUnparsed -> parseInt(cubeCountUnparsed[0]), maxBy(Integer::compareTo)), // keep only the max value for this color
                                                    Optional::orElseThrow)))
                            .values()
                            .stream()
                            .mapToInt(i -> i)
                            .reduce((a, b) -> a * b) // multiply max values for each color
                            .orElseThrow();
        }

        return sumOfPowersOfMinimalCubeSetsRequiredForGame;
    }

    private static Stream<String[]> streamIndividualCubeCounts(String singleGameDataString) {
        return Arrays.stream(singleGameDataString.substring(singleGameDataString.indexOf(":") + 1).split(";"))
                .flatMap(cubeSetString -> Arrays.stream(cubeSetString.split(",")))
                .map(String::trim)
                .map(cubeCountString -> cubeCountString.split(" "));
    }
}
