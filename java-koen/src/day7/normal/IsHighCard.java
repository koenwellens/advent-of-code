package day7.normal;

import day7.AbstractIsHandType;

import java.util.Map;

public final class IsHighCard extends AbstractIsHandType {
    @Override
    protected boolean test(final Map<String, Integer> numberOfCards) {
        return numberOfCards.size() == 5;
    }
}
