package adventofcode2023.day06;

import adventofcode2023.common.Input;
import adventofcode2023.common.LongOperations;

import java.util.Arrays;
import java.util.function.Function;

import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day06.Day06Input.EXAMPLE;
import static adventofcode2023.day06.Day06Input.INPUT;

class Day06 {
    public static void main(String[] args) {
        runnerFor(Day06.class)
                .execute("Puzzle 1", Day06::numberOfWaysToWinTheRaces).withInput(EXAMPLE).expect(288L)
                .execute("Puzzle 1", Day06::numberOfWaysToWinTheRaces).withInput(INPUT).expect(128700L)
                .execute("Puzzle 2", Day06::numberOfWaysToWinTheSingleRace).withInput(EXAMPLE).expect(71503L)
                .execute("Puzzle 2", Day06::numberOfWaysToWinTheSingleRace).withInput(INPUT).expect(39594072L)
                .build()
                .run();
    }

    private static Long numberOfWaysToWinTheRaces(Input input) {
        return numberOfWaysToWinTheRaces(input, Day06::parseRaceInfoLineAsMultipleValues);
    }

    private static Long numberOfWaysToWinTheSingleRace(Input input) {
        return numberOfWaysToWinTheRaces(input, Day06::parseRaceInfoLineAsSingleValue);
    }

    private static Long numberOfWaysToWinTheRaces(Input input, Function<String, long[]> raceInfoLineParser) {
        String[] raceInfoLines = input.lines();
        long[] raceDurations = raceInfoLineParser.apply(raceInfoLines[0]);
        long[] raceRecords = raceInfoLineParser.apply(raceInfoLines[1]);

        long[] numberOfWaysToBreakTheRecord = new long[raceRecords.length];
        for (int raceNumber = 0; raceNumber < raceDurations.length; raceNumber++) {
            long raceDuration = raceDurations[raceNumber];
            for (long buttonPressDuration = 0; buttonPressDuration <= raceDuration; buttonPressDuration++) {
                long distanceTravelled = (raceDuration - buttonPressDuration) * buttonPressDuration; // speed == buttonPressDuration
                if (distanceTravelled > raceRecords[raceNumber])
                    numberOfWaysToBreakTheRecord[raceNumber]++;
            }
        }

        return Arrays.stream(numberOfWaysToBreakTheRecord)
                .reduce(LongOperations.L_MULTIPLY)
                .orElseThrow();
    }

    private static long[] parseRaceInfoLineAsMultipleValues(String raceInfoLine) {
        return Arrays.stream(raceInfoLine.split("\\s+"))
                .skip(1) // discard header
                .mapToLong(Long::parseLong)
                .toArray();
    }

    private static long[] parseRaceInfoLineAsSingleValue(String raceInfoLine) {
        return new long[]{
                Long.parseLong(raceInfoLine.split(":")[1].replaceAll("\\s+", ""))
        };
    }
}
