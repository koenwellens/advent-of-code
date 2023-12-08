package day4;

import common.Parser;

import static java.lang.Integer.parseInt;

public final class CardIdParser implements Parser<Integer> {

    private static final int CARD_PREFIX = "Card ".length();
    private final String line;

    public CardIdParser(final String line) {
        this.line = line;
    }

    @Override
    public Integer value() {
        return parseInt(line.split(":")[0].substring(CARD_PREFIX).trim());
    }
}
