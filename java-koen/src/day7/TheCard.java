package day7;

import java.util.List;

public final class TheCard implements Card {

    private static final List<String> POSSIBLE_VALUES = List.of("A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2");
    private final String value;

    public TheCard(final String value) {
        this.value = value;
    }

    @Override
    public String label() {
        return this.value;
    }

    @Override
    public int compareTo(final Card o) {
        return POSSIBLE_VALUES.indexOf(this.value) - POSSIBLE_VALUES.indexOf(o.label());
    }
}
