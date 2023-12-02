package day1;

import java.util.Arrays;
import java.util.List;

public final class FirstNumber implements DigitValue {

    private static final List<String> DIGITS = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");
    private static final List<String> WORDS = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

    private final List<String> characters;

    public FirstNumber(final String str) {
        this(Arrays.asList(str.split("")));
    }

    public FirstNumber(final List<String> characters) {
        this.characters = characters;
    }

    @Override
    public String value() {
        var i = 0;
        while (i < characters.size()) {
            for (final var digit : DIGITS) {
                if (digit.equals(characters.get(i))) {
                    return digit;
                }
            }

            for (final var word : WORDS) {
                if (characters.size() - i >= word.length()
                        && word.equals(String.join("", characters.subList(i, i + word.length())))) {
                    return DIGITS.get(WORDS.indexOf(word));
                }
            }

            i++;
        }

        throw new RuntimeException("No digits in the string '" + String.join("", characters) + "'");
    }
}
