package day11;

import common.Parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;

public final class RealGalaxiesParser implements Parser<Collection<Galaxy>> {

    private final List<String> textLines;
    private final int galaxiesDistance;

    public RealGalaxiesParser(final List<String> textLines, final int galaxiesDistance) {
        this.textLines = textLines;
        this.galaxiesDistance = galaxiesDistance;
    }

    @Override
    public List<Galaxy> value() {
        final var rowsWithGalaxies = new HashSet<Integer>();
        final var columnsWithGalaxies = new HashSet<Integer>();
        doForAll((row, column) -> {
            if ('#' == textLines.get(row).charAt(column)) {
                rowsWithGalaxies.add(row);
                columnsWithGalaxies.add(column);
            }
        });

        final var rowsWithoutGalaxies = new HashSet<Integer>();
        final var columnsWithoutGalaxies = new HashSet<Integer>();
        doForAll((row, column) -> {
            if (!rowsWithGalaxies.contains(row)) {
                rowsWithoutGalaxies.add(row);
            }

            if (!columnsWithGalaxies.contains(column)) {
                columnsWithoutGalaxies.add(column);
            }
        });

        final var result = new ArrayList<Galaxy>();
        doForAll((row, column) -> {
            if ('#' == textLines.get(row).charAt(column)) {
                var actualRow = compute(rowsWithoutGalaxies, row);
                var actualColumn = compute(columnsWithoutGalaxies, column);
                result.add(new TheGalaxy(actualColumn, actualRow));
            }
        });

        return result;
    }

    private void doForAll(BiConsumer<Integer, Integer> coordinatesConsumer) {
        for (var row = 0; row < textLines.size(); row++) {
            for (int column = 0; column < textLines.get(row).length(); column++) {
                coordinatesConsumer.accept(row, column);
            }
        }
    }

    private long compute(final HashSet<Integer> rowsOrColumnsWithoutGalaxies, final long rowOrColumn) {
        var result = rowOrColumn;
        for (final int rowsWithoutGalaxy : rowsOrColumnsWithoutGalaxies) {
            if (rowsWithoutGalaxy < rowOrColumn) {
                result += (galaxiesDistance - 1);
            }
        }
        return result;
    }
}
