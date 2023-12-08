package day7;

import common.Parser;

import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.stream;

public final class CardsParser implements Parser<List<Card>> {

    private final String line;
    private final Function<String, Card> cardCreator;

    public CardsParser(final String line, final Function<String, Card> cardCreator) {
        this.line = line;
        this.cardCreator = cardCreator;
    }

    @Override
    public List<Card> value() {
        final var cards = line.split(" ")[0].split("");

        return stream(cards)
                .map(cardCreator)
                .toList();
    }
}
