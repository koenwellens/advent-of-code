package day4;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

public final class SetOfCards implements CardSet {

    private final Map<Integer, Integer> cards;
    private final Collection<Card> originalCards;
    private final Map<Integer, Integer> handledCards;

    public SetOfCards(final Collection<Card> cards) {
        this.cards = cards.stream().collect(toMap(Card::id, c -> 1));
        this.originalCards = cards.stream().toList();
        this.handledCards = new HashMap<>();
    }

    @Override
    public Card aCardToHandle() {
        return this.originalCards.stream()
                .filter(card -> card.id() == potentialCardToBeHandled().orElseThrow())
                .findAny()
                .orElseThrow();
    }

    private Optional<Integer> potentialCardToBeHandled() {
        return cards.keySet().stream().filter(id -> cards.get(id) > 0).findAny();
    }

    @Override
    public boolean hasCardsToHandle() {
        return potentialCardToBeHandled().isPresent();
    }

    @Override
    public void handleCard(final Card card) {
        final var prizes = card.prizes(originalCards);
        final var numberOfCurrentCards = cards.get(card.id());

        this.handledCards.put(card.id(), this.handledCards.getOrDefault(card.id(), 0) + numberOfCurrentCards);
        prizes.forEach(prize -> this.cards.merge(prize.id(), numberOfCurrentCards, Integer::sum));
        this.cards.put(card.id(), 0);
    }

    @Override
    public int numberOfScratchCardsHandled() {
        return this.handledCards.values().stream().mapToInt(Integer::valueOf).sum();
    }
}
