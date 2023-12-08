package day7;

import common.Parser;

public final class BidParser implements Parser<Long> {

    private final String text;

    public BidParser(final String text) {
        this.text = text;
    }

    @Override
    public Long value() {
        final var bid = text.split(" ")[1];

        return Long.valueOf(bid);
    }
}
