package day7;

import java.util.List;
import java.util.function.Function;

public final class TheHand implements Hand {

    private final List<Card> cards;
    private final long bid;
    private final HandType handType;

    public TheHand(final String line, final Function<String, Card> cardCreator, final HandTypeCalculator calculator) {
        this(new CardsParser(line, cardCreator).value(), new BidParser(line).value(), calculator);
    }

    public TheHand(final List<Card> cards, final long bid, final HandTypeCalculator calculator) {
        this(cards, bid, calculator.calculate());
    }

    private TheHand(final List<Card> cards, final long bid, final HandType handType) {
        this.cards = cards;
        this.bid = bid;
        this.handType = handType;
    }

    @Override
    public HandType type() {
        return this.handType;
    }

    @Override
    public List<Card> cards() {
        return this.cards;
    }

    @Override
    public long winning(final int rank) {
        return this.bid * (long) rank;
    }

    @Override
    public int compareTo(final Hand o) {
        final var typeComparison = this.handType.compareTo(o.type());
        if (typeComparison != 0) {
            return typeComparison;
        }

        for (int index = 0; index < this.cards.size(); index++) {
            final var comparison = this.cards.get(index).compareTo(o.cards().get(index));
            if (comparison != 0) {
                return comparison;
            }
        }

        return 0;
    }
}
