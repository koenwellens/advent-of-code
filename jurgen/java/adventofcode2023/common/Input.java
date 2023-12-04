package adventofcode2023.common;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Input {

    public static Input of(String dataString) {
        return new Input(dataString);
    }

    private final String rawData;

    private Input(String rawData) {
        this.rawData = rawData;
    }

    public String[] lines() {
        return rawData.split("\n");
    }

    public Stream<String> streamLines() {
        return Arrays.stream(lines());
    }

    public <T> Stream<T> streamLines(Function<String, T> lineParser) {
        return streamLines().map(lineParser);
    }

    public IntStream streamLinesToInt(Function<String, Integer> lineParser) {
        return streamLines(lineParser).mapToInt(i -> i);
    }

    public LongStream streamLinesToLong(Function<String, Long> lineParser) {
        return streamLines(lineParser).mapToLong(i -> i);
    }
}
