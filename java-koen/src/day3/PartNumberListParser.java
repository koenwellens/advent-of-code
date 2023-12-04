package day3;

import java.util.List;

public final class PartNumberListParser extends AbstractListParser<PotentialPartNumber, NumberParser> {

    public PartNumberListParser(final List<String> textLines) {
        super(textLines);
    }

    @Override
    protected NumberParser parser(final String line, final int row) {
        return new NumberParser(line, row);
    }
}
