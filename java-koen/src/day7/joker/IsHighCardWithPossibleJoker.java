package day7.joker;

import day7.AbstractIsHandType;

import java.util.Map;

public final class IsHighCardWithPossibleJoker extends AbstractIsHandType {
    private static final String JOKER = "J";
    @Override
    protected boolean test(final Map<String, Integer> numberOfCards) {
        return numberOfCards.size() == 5 && !numberOfCards.containsKey(JOKER);
    }
}
