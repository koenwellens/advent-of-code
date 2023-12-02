package day1;

import java.util.Arrays;
import java.util.List;

public final class LastNumber implements DigitValue {

    private static final List<String> DIGITS = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");
    private static final List<String> WORDS = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
    private final List<String> characters;

    public LastNumber(final String str) {
        this(Arrays.asList(str.split("")));
    }

    private LastNumber(final List<String> characters) {
        this.characters = characters;
    }

    @Override
    public String value() {
        var i = characters.size() - 1;
        while (i >= 0) {
            for (final var digit : DIGITS) {
                if (digit.equals(characters.get(i))) {
                    return digit;
                }
            }

            for (var w = 0; w < WORDS.size(); w++) {
                final var word = WORDS.get(w);
                if (i >= word.length()
                        && word.equals(String.join("", characters.subList(i + 1 - word.length(), i + 1)))) {
                    return DIGITS.get(w);
                }
            }

            i--;
        }

        throw new RuntimeException("No digits in the string '" + String.join("", characters) + "'");
    }
}
