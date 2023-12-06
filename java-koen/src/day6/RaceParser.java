package day6;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.IntStream.range;

public final class RaceParser implements Parser<Collection<Race>> {

    private final List<String> textLines;

    public RaceParser(final List<String> textLines) {
        this.textLines = textLines;
    }

    @Override
    public Collection<Race> value() {
        final var times = this.textLines.get(0).split("\\s+");
        final var distances = this.textLines.get(1).split("\\s+");

        return range(1, times.length)
                .mapToObj(i -> new TheRace(times[i].trim(), distances[i].trim()))
                .collect(toUnmodifiableList());
    }
}
