package day3;

import java.util.Collection;

public interface PartNumber {

    int value();
    boolean isAdjacentToOneOf(Collection<Symbol> symbols);

    boolean isAdjacentTo(Symbol symbol);
}
