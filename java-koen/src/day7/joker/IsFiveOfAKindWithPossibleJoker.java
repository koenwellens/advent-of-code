package day7.joker;

import day7.AbstractIsHandType;

import java.util.Map;

public final class IsFiveOfAKindWithPossibleJoker extends AbstractIsHandType {

    private static final String JOKER = "J";

    @Override
    protected boolean test(final Map<String, Integer> numberOfCards) {
        return numberOfCards.size() == 1 ||
                (numberOfCards.size() == 2 && numberOfCards.containsKey(JOKER));
    }
}
