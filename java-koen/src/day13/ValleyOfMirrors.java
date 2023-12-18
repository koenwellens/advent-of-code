package day13;

import common.AbstractObjectBasedOnInput;

import java.util.List;

public final class ValleyOfMirrors extends AbstractObjectBasedOnInput<Long> {

    public ValleyOfMirrors(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Long run() {
        return new PatternParser(textLines).value()
                .stream()
                .map(ThePattern::new)
                .mapToLong(Pattern::totalReflection)
                .sum();
    }

    @Override
    public Long alternateRun() {
        return null;
    }
}
