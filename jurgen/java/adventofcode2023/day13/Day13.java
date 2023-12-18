package adventofcode2023.day13;

import adventofcode2023.common.Input;

import java.util.*;

import static adventofcode2023.common.CollectionUtils.unionOf;
import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day13.Day13Input.EXAMPLE;
import static adventofcode2023.day13.Day13Input.INPUT;
import static java.lang.String.join;
import static java.util.Set.copyOf;

class Day13 {

    public static void main(String[] args) {
        runnerFor(Day13.class)
                .execute("Puzzle 1", Day13::sumOfNumberOfColumnsLeftOfMirrorAnd100TimesNumberOfRowsAboveMirror).withInput(EXAMPLE).expect(405)
                .execute("Puzzle 1", Day13::sumOfNumberOfColumnsLeftOfMirrorAnd100TimesNumberOfRowsAboveMirror).withInput(INPUT).expect(29165)
                .execute("Puzzle 2", Day13::sumOfNumberOfColumnsLeftOfMirrorAnd100TimesNumberOfRowsAboveMirrorWithSmudges).withInput(EXAMPLE).expect(400)
                .execute("Puzzle 2", Day13::sumOfNumberOfColumnsLeftOfMirrorAnd100TimesNumberOfRowsAboveMirrorWithSmudges).withInput(INPUT).expect(32192)
                .build()
                .run();
    }

    private static int sumOfNumberOfColumnsLeftOfMirrorAnd100TimesNumberOfRowsAboveMirror(Input input) {
        return parseTerrainPatterns(input).stream()
                .parallel()
                .map(Day13::calculateMirrors)
                .flatMap(Collection::stream)
                .mapToInt(Mirror::score)
                .sum();
    }

    private static int sumOfNumberOfColumnsLeftOfMirrorAnd100TimesNumberOfRowsAboveMirrorWithSmudges(Input input) {
        return parseTerrainPatterns(input).stream()
                .parallel()
                .map(terrainPattern -> {
                    // calculate reference result for map-with-smudge
                    Set<Mirror> initialMirrors = calculateMirrors(terrainPattern);

                    // brute force it by flipping each tile on the map and comparing the result to the initial result
                    for (int row = 0; row < terrainPattern.size(); row++) {
                        for (int column = 0; column < terrainPattern.get(row).length(); column++) {
                            List<String> unSmudgedTerrainPattern = new ArrayList<>(terrainPattern);
                            unSmudgedTerrainPattern.set(row, unSmudge(unSmudgedTerrainPattern.get(row), column));

                            Set<Mirror> unSmudgedMirrors = calculateMirrors(unSmudgedTerrainPattern);

                            HashSet<Mirror> newMirrorsAfterUnSmudging = new HashSet<>(unSmudgedMirrors);
                            newMirrorsAfterUnSmudging.removeAll(initialMirrors);
                            if (!newMirrorsAfterUnSmudging.isEmpty()) {
                                return newMirrorsAfterUnSmudging;
                            }
                        }
                    }

                    throw new IllegalStateException();
                })
                .flatMap(Collection::stream)
                .mapToInt(Mirror::score)
                .sum();
    }

    private static Set<Mirror> calculateMirrors(List<String> terrainPattern) {
        return unionOf(
                calculateHorizontalMirrors(terrainPattern),
                calculateVerticalMirrors(terrainPattern));
    }

    private static Set<Mirror> calculateVerticalMirrors(List<String> terrainPattern) {
        return calculateMirrorScore(transpose(terrainPattern), Direction.VERTICAL);
    }

    private static Set<Mirror> calculateHorizontalMirrors(List<String> terrainPattern) {
        return calculateMirrorScore(terrainPattern, Direction.HORIZONTAL);
    }

    private static String unSmudge(String str, int index) {
        char[] chars = str.toCharArray();
        chars[index] = chars[index] == '#' ? '.' : '#';
        return String.valueOf(chars);
    }

    private static List<String> transpose(List<String> terrainPattern) {
        int numberOfColumns = terrainPattern.getFirst().length();
        int numberOfRows = terrainPattern.size();

        // switching rows and columns
        String[][] transposedTerrainPattern = new String[numberOfColumns][numberOfRows];
        for (int row = 0; row < terrainPattern.size(); row++) {
            String terrainPatternRow = terrainPattern.get(row);
            for (int column = 0; column < numberOfColumns; column++) {
                transposedTerrainPattern[column][row] = terrainPatternRow.substring(column, column + 1);
            }
        }

        return Arrays.stream(transposedTerrainPattern)
                .map(columnTransposedToRow -> join("", columnTransposedToRow))
                .toList();
    }

    private static Set<Mirror> calculateMirrorScore(List<String> terrainPattern, Direction direction) {
        Set<Mirror> mirrors = new HashSet<>();

        for (int rowAboveMirrorIndex = 0; rowAboveMirrorIndex < terrainPattern.size() - 1; rowAboveMirrorIndex++) {
            // is the mirror closer to the top or bottom edge? mirroring only needs to be checked up to that point
            int numberOfRowsAboveMirror = rowAboveMirrorIndex + 1;
            int numberOfRowsBelowMirror = terrainPattern.size() - numberOfRowsAboveMirror;

            // starting from the mirror (between rows rowAboveMirrorIndex and rowAboveMirrorIndex+1) go outward until either edge is reached
            // each pair of rows at the same distance of the mirror must have the same row value
            boolean allRowsMirrored = true;
            for (int distanceToMirror = 0; allRowsMirrored && distanceToMirror < Math.min(numberOfRowsAboveMirror, numberOfRowsBelowMirror); distanceToMirror++) {
                allRowsMirrored = terrainPattern.get(rowAboveMirrorIndex - distanceToMirror)
                        .equals(terrainPattern.get(rowAboveMirrorIndex + 1 + distanceToMirror));
            }
            if (allRowsMirrored) {
                mirrors.add(new Mirror(direction, rowAboveMirrorIndex));
            }
        }

        return copyOf(mirrors);
    }

    private static List<List<String>> parseTerrainPatterns(Input input) {
        List<List<String>> groupedTerrainLines = new ArrayList<>();
        groupedTerrainLines.add(new ArrayList<>());
        for (String line : input.lines()) {
            if (line.isEmpty()) {
                groupedTerrainLines.add(new ArrayList<>());
            } else {
                groupedTerrainLines.getLast().add(line);
            }
        }
        return groupedTerrainLines;
    }

    private record Mirror(Direction direction, int afterIndex) {
        int score() {
            return direction == Direction.VERTICAL
                    ? afterIndex + 1
                    : 100 * (afterIndex + 1);
        }
    }

    private enum Direction {
        VERTICAL,
        HORIZONTAL
    }
}
