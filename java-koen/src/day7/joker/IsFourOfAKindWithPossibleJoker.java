package day7.joker;

import day7.AbstractIsHandType;

import java.util.Map;

public final class IsFourOfAKindWithPossibleJoker extends AbstractIsHandType {
    private static final String JOKER = "J";

    @Override
    protected boolean test(final Map<String, Integer> numberOfCards) {
        return (numberOfCards.size() == 2 && numberOfCards.values().stream().allMatch(l -> l == 1 || l == 4) && !numberOfCards.containsKey(JOKER))
                || (numberOfCards.size() == 3 && numberOfCards.containsKey(JOKER) && numberOfCards.entrySet().stream().anyMatch(entry -> !JOKER.equals(entry.getKey()) && 1 == entry.getValue()));
    }
}
