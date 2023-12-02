package day1;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class IsDigit implements Predicate<String> {

    private static final Pattern IS_NUMBER = Pattern.compile("\\d");

    @Override
    public boolean test(final String s) {
        return IS_NUMBER.matcher(s).matches();
    }
}
