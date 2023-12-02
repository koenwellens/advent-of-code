package day2;

import static java.util.Arrays.stream;

public abstract class AbstractNumberOfCubesParser extends AbstractTextParser<Integer> {

    private static final String CUBE_DELIMITER = ", ";
    private static final String SPACE = " ";
    protected final String color;

    protected AbstractNumberOfCubesParser(final String text, final String color) {
        super(text);
        this.color = color;
    }

    @Override
    public Integer value() {
        return stream(this.text.split(CUBE_DELIMITER))
                .filter(s -> s.contains(color))
                .map(s -> s.split(SPACE))
                .map(s -> s[0])
                .mapToInt(Integer::parseInt)
                .sum();
    }
}
