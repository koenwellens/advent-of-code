package day3;

import common.Parser;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Character.isDigit;

public final class NumberParser implements Parser<Collection<PotentialPartNumber>> {
    private final String line;
    private final int row;

    public NumberParser(final String line, final int row) {
        this.line = line;
        this.row = row;
    }

    @Override
    public Collection<PotentialPartNumber> value() {
        final var result = new ArrayList<PotentialPartNumber>();
        for (int column = 0; column < this.line.length(); column++) {
            if (isDigit(this.line.charAt(column))) {
                for (int end = column + 1; end < this.line.length(); end++) {
                    if (end == this.line.length() - 1 && isDigit(this.line.charAt(end))) {
                        result.add(new PotentialPartNumber(this.line.substring(column), this.row, column));
                    }
                    if (!isDigit(this.line.charAt(end))) {
                        result.add(new PotentialPartNumber(this.line.substring(column, end), this.row, column));
                        column += (end - column - 1);
                        break;
                    }
                }
            }
        }

        return result;
    }
}
