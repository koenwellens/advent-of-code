package day12;

import common.Parser;

import java.util.List;

import static java.util.Arrays.stream;

public final class GroupSizeParser implements Parser<List<Integer>> {

    private final String line;

    public GroupSizeParser(final String line) {
        this.line = line;
    }

    @Override
    public List<Integer> value() {
        final var conditions = line.split(" ")[1].trim();

        return stream(conditions.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
    }
}
