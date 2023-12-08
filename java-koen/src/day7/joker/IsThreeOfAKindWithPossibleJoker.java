package day7.joker;

import day7.AbstractIsHandType;

import java.util.Map;

public final class IsThreeOfAKindWithPossibleJoker extends AbstractIsHandType {
    private static final String JOKER = "J";

    @Override
    protected boolean test(final Map<String, Integer> numberOfCards) {
        return (numberOfCards.size() == 3
                && numberOfCards.values().stream().allMatch(l -> l == 3 || l == 1)
                && !numberOfCards.containsKey(JOKER)
        ) || (
                numberOfCards.size() == 4
                        && numberOfCards.getOrDefault(JOKER, 0) == 1
                        && numberOfCards.entrySet().stream().filter(e -> !e.getKey().equals(JOKER)).map(Map.Entry::getValue).allMatch(l -> l == 2 || l == 1)
        ) || (
                numberOfCards.size() == 4
                        && numberOfCards.getOrDefault(JOKER, 0) == 2
                        && numberOfCards.entrySet().stream().filter(e -> !e.getKey().equals(JOKER)).map(Map.Entry::getValue).allMatch(l -> l == 1)
        );
    }
}
