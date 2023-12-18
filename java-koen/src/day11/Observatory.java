package day11;

import common.AbstractObjectBasedOnInput;

import java.util.Comparator;
import java.util.List;

public final class Observatory extends AbstractObjectBasedOnInput<Long> {
    public Observatory(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Long run() {
        return computeSumOfShortestDistances(10);
    }

    private long computeSumOfShortestDistances(final int galaxiesDistance) {
        final var galaxies = new RealGalaxiesParser(textLines, galaxiesDistance).value();
        var result = 0L;
        galaxies.stream().sorted(Comparator.comparing(Galaxy::y).thenComparing(Galaxy::x)).forEach(Galaxy::print);
        for (var index = 0; index < galaxies.size(); index++) {
            for (var next = index + 1; next < galaxies.size(); next++) {
                result += galaxies.get(index).shortestDistanceTo(galaxies.get(next));
            }
        }

        return result;
    }

    @Override
    public Long alternateRun() {
        return computeSumOfShortestDistances(1000000);
    }
}
