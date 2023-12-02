package day1;

import java.util.Arrays;
import java.util.List;

public final class FirstDigit implements DigitValue {

    private final List<String> characters;

    public FirstDigit(final String str) {
        this(Arrays.asList(str.split("")));
    }

    public FirstDigit(final List<String> characters) {
        this.characters = characters;
    }

    @Override
    public String value() {
        return characters.stream()
                .filter(new IsDigit())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No digits in the string '" + String.join("", characters) + "'"));
    }
}
