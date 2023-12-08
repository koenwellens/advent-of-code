package day7;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

public abstract class AbstractIsHandType implements Predicate<Collection<Card>> {

    protected abstract boolean test(Map<String, Integer> numberOfCards);

    @Override
    public boolean test(final Collection<Card> cards) {
        final var allCards = cards.stream().map(Card::label).collect(Collectors.joining());

        final var numberOfCards = new HashMap<String, Integer>();
        for (int i = 0; i < allCards.length(); i++) {
            final var key = valueOf(allCards.charAt(i));
            numberOfCards.put(key, numberOfCards.getOrDefault(key, 0) + 1);
        }

        return test(numberOfCards);
    }
}
