package day7;

import day7.joker.IsFiveOfAKindWithPossibleJoker;
import day7.joker.IsFourOfAKindWithPossibleJoker;
import day7.joker.IsFullHouseWithPossibleJoker;
import day7.joker.IsHighCardWithPossibleJoker;
import day7.joker.IsOnePairWithPossibleJoker;
import day7.joker.IsThreeOfAKindWithPossibleJoker;
import day7.joker.IsTwoPairsWithPossibleJoker;
import day7.normal.IsFiveOfAKind;
import day7.normal.IsFourOfAKind;
import day7.normal.IsFullHouse;
import day7.normal.IsHighCard;
import day7.normal.IsOnePair;
import day7.normal.IsThreeOfAKind;
import day7.normal.IsTwoPairs;

import java.util.Collection;
import java.util.function.Predicate;

public enum HandType {

    FIVE_OF_A_KIND(new IsFiveOfAKind(), new IsFiveOfAKindWithPossibleJoker()),
    FOUR_OF_A_KIND(new IsFourOfAKind(), new IsFourOfAKindWithPossibleJoker()),
    FULL_HOUSE(new IsFullHouse(), new IsFullHouseWithPossibleJoker()),
    THREE_OF_A_KIND(new IsThreeOfAKind(), new IsThreeOfAKindWithPossibleJoker()),
    TWO_PAIRS(new IsTwoPairs(), new IsTwoPairsWithPossibleJoker()),
    ONE_PAIR(new IsOnePair(), new IsOnePairWithPossibleJoker()),
    HIGH_CARD(new IsHighCard(), new IsHighCardWithPossibleJoker()),
    ;

    private final Predicate<Collection<Card>> is;
    private final Predicate<Collection<Card>> isWithPossibleJoker;

    HandType(final Predicate<Collection<Card>> is, final Predicate<Collection<Card>> isWithPossibleJoker) {
        this.is = is;
        this.isWithPossibleJoker = isWithPossibleJoker;
    }

    public Predicate<Collection<Card>> matcher() {
        return is;
    }

    public Predicate<Collection<Card>> jokerMatcher() {
        return isWithPossibleJoker;
    }
}
