package adventofcode2023.day04;

import adventofcode2023.common.Input;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static adventofcode2023.common.CollectionUtils.intersectionOf;
import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day04.Day04Input.EXAMPLE;
import static adventofcode2023.day04.Day04Input.INPUT;
import static java.util.stream.Collectors.toList;

class Day04 {
    public static void main(String[] args) {
        runnerFor(Day04.class)
                .execute("Puzzle 1", Day04::sumOfScratchCardScores).withInput(EXAMPLE).expect(13L)
                .execute("Puzzle 1", Day04::sumOfScratchCardScores).withInput(INPUT).expect(28538L)
                .execute("Puzzle 2", Day04::totalScratchCardsWon).withInput(EXAMPLE).expect(30L)
                .execute("Puzzle 2", Day04::totalScratchCardsWon).withInput(INPUT).expect(9425061L)
                .build()
                .run();
    }

    private static long sumOfScratchCardScores(Input allScratchCardsInput) {
        return allScratchCardsInput
                .streamLinesToLong(Day04::parseAndCalculateScratchCardScore)
                .sum();
    }

    private static long totalScratchCardsWon(Input allScratchCardsInput) {
        String[] allScratchCardStrings = allScratchCardsInput.lines();
        List<Integer> cardCounts = IntStream.range(0, allScratchCardStrings.length).map(i -> 1).boxed().collect(toList());
        for (int cardNumber = 0; cardNumber < allScratchCardStrings.length; cardNumber++) {
            int numberOfCardsWon = parseAndCalculateMatchingNumberCount(allScratchCardStrings[cardNumber]);
            for (int cardNumberOffset = 1; cardNumberOffset <= numberOfCardsWon; cardNumberOffset++) {
                int cardNumberToCopy = cardNumber + cardNumberOffset;
                int numberOfCardsToAdd = cardCounts.get(cardNumber);
                cardCounts.set(cardNumberToCopy, cardCounts.get(cardNumberToCopy) + numberOfCardsToAdd);
            }
        }
        return cardCounts.stream().mapToLong(i -> i).sum();
    }

    private static Long parseAndCalculateScratchCardScore(String scratchCardData) {
        int yourWinningNumberCount = parseAndCalculateMatchingNumberCount(scratchCardData);
        return 1L << yourWinningNumberCount - 1;
    }

    private static int parseAndCalculateMatchingNumberCount(String scratchCardData) {
        String[] scratchCardNumberStrings = scratchCardData.split(": ")[1].split(" \\| ");
        Set<Long> winningNumbers = Arrays.stream(scratchCardNumberStrings[0].trim().split("\\s+")).map(Long::parseLong).collect(Collectors.toSet());
        Set<Long> yourNumbers = Arrays.stream(scratchCardNumberStrings[1].trim().split("\\s+")).map(Long::parseLong).collect(Collectors.toSet());
        return intersectionOf(yourNumbers, winningNumbers).size();
    }

}
