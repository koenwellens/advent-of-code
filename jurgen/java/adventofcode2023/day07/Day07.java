package adventofcode2023.day07;

import adventofcode2023.common.Input;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day07.Day07Input.EXAMPLE;
import static adventofcode2023.day07.Day07Input.INPUT;

class Day07 {
    public static void main(String[] args) {
        runnerFor(Day07.class)
                .execute("Puzzle 1", Day07::totalWinnings).withInput(EXAMPLE).expect(6440L)
                .execute("Puzzle 1", Day07::totalWinnings).withInput(INPUT).expect(250957639L)
                .execute("Puzzle 2", Day07::totalWinningsWithJokers).withInput(EXAMPLE).expect(5905L)
                .execute("Puzzle 2", Day07::totalWinningsWithJokers).withInput(INPUT).expect(251515496L)
                .build()
                .run();
    }

    private static final Map<String, Integer> CARD_TO_VALUE_MAP = Map.ofEntries(
            Map.entry("2", 2),
            Map.entry("3", 3),
            Map.entry("4", 4),
            Map.entry("5", 5),
            Map.entry("6", 6),
            Map.entry("7", 7),
            Map.entry("8", 8),
            Map.entry("9", 9),
            Map.entry("T", 10),
            Map.entry("J", 11),
            Map.entry("Q", 12),
            Map.entry("K", 13),
            Map.entry("A", 14));

    private static Long totalWinnings(Input input) {
        AtomicLong rankGenerator = new AtomicLong(1);
        return input.streamLines(handAndBidLine -> parseHand(handAndBidLine, false))
                .sorted()
                .reduce(0L, (acc, hand) -> acc + hand.bid() * rankGenerator.getAndIncrement(), Long::sum);
    }

    private static Long totalWinningsWithJokers(Input input) {
        AtomicLong rankGenerator = new AtomicLong(1);
        return input.streamLines(handAndBidLine -> parseHand(handAndBidLine, true))
                .sorted()
                .reduce(0L, (acc, hand) -> acc + hand.bid() * rankGenerator.getAndIncrement(), Long::sum);
    }

    private static Hand parseHand(String handAndBidLine, boolean useJokers) {
        String[] handAndBidStrings = handAndBidLine.split(" ");
        return new Hand(
                Arrays.stream(handAndBidStrings[0].split(""))
                        .mapToInt(CARD_TO_VALUE_MAP::get)
                        .map(cardValue -> useJokers && cardValue == 11 ? 1 : cardValue) // if J==Joker, then its value is 1
                        .toArray(),
                Long.parseLong(handAndBidStrings[1]),
                useJokers);
    }

}
