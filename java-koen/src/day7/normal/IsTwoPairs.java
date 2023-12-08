package day7.normal;

import day7.AbstractIsHandType;

import java.util.Map;

public final class IsTwoPairs extends AbstractIsHandType {
    @Override
    protected boolean test(final Map<String, Integer> numberOfCards) {
        return numberOfCards.size() == 3
                && numberOfCards.values().stream().allMatch(l -> l == 2 || l == 1);
    }
}
