package day7;

import common.AbstractObjectBasedOnInput;

import java.util.List;
import java.util.function.Function;

public final class CamelCardGame extends AbstractObjectBasedOnInput<Long> {
    public CamelCardGame(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Long run() {
        return calculateTotalWinnings(line -> new TheHand(line, TheCard::new, new NormalHandTypeCalculator(new CardsParser(line, TheCard::new).value())));
    }

    private Long calculateTotalWinnings(Function<String, Hand> constructor) {
        final var sortedHands = this.textLines.stream()
                .map(constructor)
                .sorted()
                .toList();

        long result = 0;

        for (int i = sortedHands.size() - 1; i >= 0; i--) {
            final var rank = sortedHands.size() - i;
            final var theHand = sortedHands.get(i);
            result += theHand.winning(rank);
        }

        return result;
    }

    @Override
    public Long alternateRun() {
        return calculateTotalWinnings(line -> new TheHand(line, PossibleJokerCard::new, new JokerHandTypeCalculator(new CardsParser(line, PossibleJokerCard::new).value())));
    }
}
