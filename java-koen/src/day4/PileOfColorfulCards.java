package day4;

import common.AbstractObjectBasedOnInput;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public final class PileOfColorfulCards extends AbstractObjectBasedOnInput<Integer> {
    public PileOfColorfulCards(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Integer run() {
        return theOriginalCards()
                .stream()
                .mapToInt(Card::points)
                .sum();
    }

    @Override
    public Integer alternateRun() {
        final var cards = theOriginalCards();
        final var setOfCards = new SetOfCards(cards);

        while (setOfCards.hasCardsToHandle()) {
            final var card = setOfCards.aCardToHandle();
            setOfCards.handleCard(card);
        }

        return setOfCards.numberOfScratchCardsHandled();
    }

    private Collection<Card> theOriginalCards() {
        return this.textLines.stream().map(OriginalCard::new).collect(toUnmodifiableList());
    }
}
