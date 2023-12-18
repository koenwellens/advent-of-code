package day13;

import common.Parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public final class PatternParser implements Parser<Collection<List<String>>> {

    private final List<String> lines;

    public PatternParser(final List<String> lines) {
        this.lines = lines;
    }

    @Override
    public Collection<List<String>> value() {
        var result = new HashSet<List<String>>();
        var temp = new ArrayList<String>();
        for (final var line : lines) {
            if (line.trim().isEmpty()) {
                result.add(temp);
                temp = new ArrayList<>();
            } else {
                temp.add(line);
            }
        }
        result.add(temp);

        return result;
    }
}
