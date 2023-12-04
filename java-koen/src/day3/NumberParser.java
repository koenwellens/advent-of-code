package day3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

public final class NumberParser implements Parser<Collection<PotentialPartNumber>> {
    private final String line;
    private final int row;

    public NumberParser(final String line, final int row) {
        this.line = line;
        this.row = row;
    }

    @Override
    public Collection<PotentialPartNumber> value() {
        final var pattern = Pattern.compile("(\\d+)");
        final var matcher = pattern.matcher(this.line);

        final var result = new ArrayList<PotentialPartNumber>();
        while (matcher.find()) {
            final var foundValue = matcher.group(0);
            result.add(new PotentialPartNumber(foundValue, this.row, line.indexOf(foundValue)));
        }

        return result;
    }
}
