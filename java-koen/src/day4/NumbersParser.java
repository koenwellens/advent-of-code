package day4;

import java.util.function.Function;

public final class NumbersParser extends AbstractListOfNumbersParser<Number> {

    public NumbersParser(final String line) {
        super(line);
    }

    @Override
    protected String partOfTheNumbersArray(final String[] numbersArray) {
        return numbersArray[1];
    }

    @Override
    protected Function<String, Number> constructor() {
        return PotentialWinningNumber::new;
    }
}
