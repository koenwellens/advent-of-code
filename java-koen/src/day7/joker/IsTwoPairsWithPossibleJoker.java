package day7.joker;

import day7.AbstractIsHandType;

import java.util.Map;

public final class IsTwoPairsWithPossibleJoker extends AbstractIsHandType {
    private static final String JOKER = "J";

    @Override
    protected boolean test(final Map<String, Integer> numberOfCards) {
        return numberOfCards.size() == 3
                && !numberOfCards.containsKey(JOKER)
                && numberOfCards.values().stream().allMatch(l -> l == 2 || l == 1);
    }
}
