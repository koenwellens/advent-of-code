package day9;

import java.util.ArrayList;
import java.util.List;

public final class TheSequence implements Sequence {
    private final List<Long> numbers;

    public TheSequence(final List<Long> numbers) {
        this.numbers = numbers;
    }

    @Override
    public boolean ends() {
        return numbers.stream().allMatch(n -> n == 0L);
    }

    @Override
    public Sequence next() {
        final var newNumbers = new ArrayList<Long>();
        var previous = 0;
        for (var current = 1; current < numbers.size(); current++) {
            newNumbers.add(numbers.get(current) - numbers.get(previous));
            previous = current;
        }

        return new TheSequence(newNumbers);
    }

    @Override
    public Long extrapolateRight(final Long value) {
        return numbers.get(numbers.size() - 1) + value;
    }

    @Override
    public Long extrapolateLeft(final Long value) {
        return numbers.get(0) - value;
    }
}
