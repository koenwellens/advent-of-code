package day4;

import java.util.function.Function;

public final class WinningNumbersParser extends AbstractListOfNumbersParser<WinningNumber> {

    public WinningNumbersParser(final String line) {
        super(line);
    }

    @Override
    protected String partOfTheNumbersArray(final String[] numbersArray) {
        return numbersArray[0];
    }

    @Override
    protected Function<String, WinningNumber> constructor() {
        return TheWinningNumber::new;
    }
}
