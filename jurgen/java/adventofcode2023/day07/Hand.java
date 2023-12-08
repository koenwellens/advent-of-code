package adventofcode2023.day07;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

import static adventofcode2023.day07.Hand.HandType.*;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

record Hand(int[] cards, long bid, HandType type) implements Comparable<Hand> {

    Hand(int[] cards, long bid, boolean useJokers) {
        this(cards, bid, determineHandType(cards, useJokers));
    }

    private static final Comparator<Hand> COMPARATOR;

    static {
        Comparator<Hand> comparator = comparing(Hand::type);
        for (int i = 0; i < 5; i++) {
            final int cardNr = i;
            Comparator<Hand> cardValueComparator = Comparator.comparing(hand -> hand.getCardValue(cardNr));
            comparator = comparator.thenComparing(cardValueComparator);
        }
        COMPARATOR = comparator;
    }

    private static HandType determineHandType(int[] cards, boolean useJokers) {
        if (useJokers && Arrays.stream(cards).anyMatch(cardValue -> cardValue == 1)) { // Joker == 1
            Stream<int[]> streamOfHandsAfterJokereplacement = Arrays.stream(cards)
                    .filter(cardValue -> cardValue != 1)
                    .distinct()
                    .mapToObj(cardValueToUseForJoker -> Arrays.stream(cards).map(cardValue -> cardValue == 1 ? cardValueToUseForJoker : cardValue).toArray());
            return Stream.concat(streamOfHandsAfterJokereplacement, Stream.of(cards))
                    .map(Hand::determinHandType)
                    .max(Enum::compareTo)
                    .orElseThrow();
        } else
            return determinHandType(cards);
    }

    @NotNull
    private static HandType determinHandType(int[] cards) {
        Map<Integer, Long> cardCounts =
                Arrays.stream(cards)
                        .boxed()
                        .collect(groupingBy(identity(), counting()));
        return switch (cardCounts.size()) {
            case 1 -> FIVE_OF_A_KIND;
            case 2 -> cardCounts.containsValue(4L) ? FOUR_OF_A_KIND : FULL_HOUSE;
            case 3 -> cardCounts.containsValue(3L) ? THREE_OF_A_KIND : TWO_PAIR;
            case 4 -> ONE_PAIR;
            default -> HIGH_CARD;
        };
    }

    @Override
    public int compareTo(@NotNull Hand other) {
        return COMPARATOR.compare(this, other);
    }


    private int getCardValue(int cardNr) {
        return cards[cardNr];
    }

    enum HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }
}
