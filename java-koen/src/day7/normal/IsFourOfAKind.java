package day7.normal;

import day7.AbstractIsHandType;

import java.util.Map;

public final class IsFourOfAKind extends AbstractIsHandType {
    @Override
    protected boolean test(final Map<String, Integer> numberOfCards) {
        return numberOfCards.size() == 2
                && numberOfCards.values().stream().allMatch(l -> l == 1 || l == 4);
    }
}
