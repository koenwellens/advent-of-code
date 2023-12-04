package day3;

import java.util.function.Predicate;

public final class IsNumber implements Predicate<String> {
    @Override
    public boolean test(final String s) {
        return s.matches("\\d+");
    }
}
