package adventofcode2023.day09;

import adventofcode2023.common.Input;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day09.Day09Input.EXAMPLE;
import static adventofcode2023.day09.Day09Input.INPUT;

class Day09 {
    public static void main(String[] args) {
        runnerFor(Day09.class)
                .execute("Puzzle 1", Day09::sumOfExtrapolatedNextValues).withInput(EXAMPLE).expect(114L)
                .execute("Puzzle 1", Day09::sumOfExtrapolatedNextValues).withInput(INPUT).expect(1938731307L)
                .execute("Puzzle 2", Day09::sumOfExtrapolatedPreviousValues).withInput(EXAMPLE).expect(2L)
                .execute("Puzzle 2", Day09::sumOfExtrapolatedPreviousValues).withInput(INPUT).expect(948L)
                .build()
                .run();
    }

    private static Long sumOfExtrapolatedNextValues(Input oasisFileInput) {
        return oasisFileInput
                .streamLinesMapped(Day09::parseSequence)
                .mapToLong(Day09::determineNextValue)
                .sum();
    }

    private static Long sumOfExtrapolatedPreviousValues(Input oasisFileInput) {
        return oasisFileInput
                .streamLinesMapped(Day09::parseSequence)
                .mapToLong(Day09::determinePreviousValue)
                .sum();
    }

    private static long determineNextValue(long[] sequence) {
        if (Arrays.stream(sequence).allMatch(v -> v == 0)) {
            return 0;
        }
        long[] sequenceDiffs = new long[sequence.length - 1];
        for (int diffIndex = 0; diffIndex < sequenceDiffs.length; diffIndex++) {
            sequenceDiffs[diffIndex] = sequence[diffIndex + 1] - sequence[diffIndex];
        }
        return sequence[sequence.length - 1] + determineNextValue(sequenceDiffs);
    }

    private static long determinePreviousValue(long[] sequence) {
        if (Arrays.stream(sequence).allMatch(v -> v == 0)) {
            return 0;
        }
        long[] sequenceDiffs = new long[sequence.length - 1];
        for (int diffIndex = 0; diffIndex < sequenceDiffs.length; diffIndex++) {
            sequenceDiffs[diffIndex] = sequence[diffIndex + 1] - sequence[diffIndex];
        }
        return sequence[0] - determinePreviousValue(sequenceDiffs);
    }

    @NotNull
    private static long[] parseSequence(String sequenceLine) {
        return Arrays.stream(sequenceLine.split(" "))
                .mapToLong(Long::parseLong)
                .toArray();
    }

}
