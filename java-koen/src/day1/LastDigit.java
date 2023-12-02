package day1;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class LastDigit implements DigitValue {

    private final List<String> characters;

    public LastDigit(final String str) {
        this(Arrays.asList(str.split("")));
    }

    private LastDigit(final List<String> characters) {
        Collections.reverse(characters);
        this.characters = characters;
    }

    @Override
    public String value() {
        return new FirstDigit(this.characters).value();
    }
}
