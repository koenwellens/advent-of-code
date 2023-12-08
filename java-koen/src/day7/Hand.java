package day7;

import java.util.List;

public interface Hand extends Comparable<Hand> {

    HandType type();

    List<Card> cards();

    long winning(int rank);
}
