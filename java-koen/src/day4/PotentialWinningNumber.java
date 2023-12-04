package day4;

import java.util.Collection;

import static java.lang.Integer.parseInt;

public final class PotentialWinningNumber implements Number {

    private final int number;

    public PotentialWinningNumber(final String number) {
        this(parseInt(number.trim()));
    }

    private PotentialWinningNumber(final int number) {
        this.number = number;
    }

    @Override
    public boolean isOneOf(final Collection<WinningNumber> winningNumbers) {
        return winningNumbers.stream().anyMatch(wn -> wn.is(this.number));
    }
}
