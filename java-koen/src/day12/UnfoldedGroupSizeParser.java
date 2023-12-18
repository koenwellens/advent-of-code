package day12;

import common.Parser;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public final class UnfoldedGroupSizeParser implements Parser<List<Integer>> {

    private final String line;
    private final int timesToUnfold;

    public UnfoldedGroupSizeParser(final String line,
                                   final int timesToUnfold) {
        this.line = line;
        this.timesToUnfold = timesToUnfold;
    }

    @Override
    public List<Integer> value() {
        final var groupSizes = line.split(" ")[1].trim();
        final var unfoldedGroupSizes = IntStream.range(0, timesToUnfold).mapToObj(i -> groupSizes).collect(joining(","));

        return stream(unfoldedGroupSizes.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
    }
}
