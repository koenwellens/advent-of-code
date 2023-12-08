package day7;

import java.util.Collection;

import static java.util.Arrays.stream;

public final class NormalHandTypeCalculator implements HandTypeCalculator {

    private final Collection<Card> cards;

    public NormalHandTypeCalculator(final Collection<Card> cards) {
        this.cards = cards;
    }


    @Override
    public HandType calculate() {
        return stream(HandType.values())
                .filter(ht -> ht.matcher().test(cards))
                .findAny()
                .orElseThrow();
    }
}
