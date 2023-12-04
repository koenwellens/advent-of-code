package day4;

import java.util.Collection;

public interface Card {

    int id();

    int points();

    Collection<Card> prizes(final Collection<Card> originalCards);

    CopyCard createCopy();
}
