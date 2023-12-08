package day7.normal;

import day7.AbstractIsHandType;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public final class IsThreeOfAKind extends AbstractIsHandType {
    @Override
    protected boolean test(final Map<String, Integer> numberOfCards) {
        return numberOfCards.size() == 3
                && numberOfCards.values().stream().allMatch(l -> l == 3 || l == 1);
    }
}
