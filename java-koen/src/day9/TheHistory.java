package day9;

import java.util.ArrayList;
import java.util.function.BiFunction;

public final class TheHistory implements History {
    private final Sequence sequence;

    public TheHistory(final String line) {
        this(new SequenceParser(line).value());
    }

    public TheHistory(final Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public Long nextValue() {
        return value(Sequence::extrapolateRight);
    }

    @Override
    public Long previousValue() {
        return value(Sequence::extrapolateLeft);
    }

    private Long value(BiFunction<Sequence, Long, Long> extrapolate) {
        final var sequences = new ArrayList<Sequence>();

        var sequence = this.sequence;
        do {
            sequences.add(sequence);
            sequence = sequence.next();
        } while (!sequence.ends());

        var value = 0L;
        for (int index = sequences.size() - 1; index >= 0; index--) {
            value = extrapolate.apply(sequences.get(index), value);
        }

        return value;
    }
}
