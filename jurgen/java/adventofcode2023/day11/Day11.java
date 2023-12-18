package adventofcode2023.day11;

import adventofcode2023.common.Input;
import adventofcode2023.common.model.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day11.Day11Input.EXAMPLE;
import static adventofcode2023.day11.Day11Input.INPUT;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;

class Day11 {
    public static void main(String[] args) {
        runnerFor(Day11.class)
                .execute("Puzzle 1", universeMapInput -> sumOfShortestDistancesBetweenEachPairOfGalaxies(universeMapInput, 2)).withInput(EXAMPLE).expect(374L)
                .execute("Puzzle 1", universeMapInput -> sumOfShortestDistancesBetweenEachPairOfGalaxies(universeMapInput, 2)).withInput(INPUT).expect(9947476L)
                .execute("Puzzle 2", universeMapInput -> sumOfShortestDistancesBetweenEachPairOfGalaxies(universeMapInput, 10)).withInput(EXAMPLE).expect(1030L)
                .execute("Puzzle 2", universeMapInput -> sumOfShortestDistancesBetweenEachPairOfGalaxies(universeMapInput, 100)).withInput(EXAMPLE).expect(8410L)
                .execute("Puzzle 2", universeMapInput -> sumOfShortestDistancesBetweenEachPairOfGalaxies(universeMapInput, 1000000)).withInput(INPUT).expect(519939907614L)
                .build()
                .run();
    }

    private static long sumOfShortestDistancesBetweenEachPairOfGalaxies(Input universeMapInput, int spaceExpansionFactor) {
        // parse space date
        String[] universeMapInputLines = universeMapInput.lines();
        List<Coordinate> galaxyCoordinates = new ArrayList<>();
        Set<Integer> emptyColumns = IntStream.range(0, universeMapInputLines[0].length()).boxed().collect(toSet());
        Set<Integer> emptyRows = IntStream.range(0, universeMapInputLines.length).boxed().collect(toSet());
        for (int row = 0; row < universeMapInputLines.length; row++) {
            String[] universeMapInputLineItems = universeMapInputLines[row].split("");
            for (int column = 0; column < universeMapInputLineItems.length; column++) {
                if (universeMapInputLineItems[column].equals("#")) {
                    galaxyCoordinates.add(new Coordinate(column, row));
                    emptyColumns.remove(column);
                    emptyRows.remove(row);
                }
            }
        }
        // expand space
        // expanding 1 row/column by a certain factor == adding (factor-1) rows/columns
        int spaceAdded = spaceExpansionFactor - 1;
        emptyColumns.stream()
                .sorted(comparing(Integer::intValue).reversed())
                .forEach(column -> {
                    List.copyOf(galaxyCoordinates).stream()
                            .filter(co -> co.x() > column)
                            .forEach(co -> {
                                galaxyCoordinates.remove(co);
                                galaxyCoordinates.add(new Coordinate(co.x() + spaceAdded, co.y()));
                            });
                });
        emptyRows.stream()
                .sorted(comparing(Integer::intValue).reversed())
                .forEach(row -> {
                    List.copyOf(galaxyCoordinates).stream()
                            .filter(co -> co.y() > row)
                            .forEach(co -> {
                                galaxyCoordinates.remove(co);
                                galaxyCoordinates.add(new Coordinate(co.x(), co.y() + spaceAdded));
                            });
                });

        long sumOfManhattanDistancesBetweeenEachPairOfGalaxies = 0;
        for (Coordinate galaxyCoordinate1 : galaxyCoordinates) {
            for (Coordinate galaxyCoordinate2 : galaxyCoordinates) {
                if (galaxyCoordinate1 != galaxyCoordinate2) {
                    sumOfManhattanDistancesBetweeenEachPairOfGalaxies += galaxyCoordinate1.manhattanDistanceTo(galaxyCoordinate2);
                }
            }
        }

        // divide by 2 because loop above calculates distance a-b and distance b-a
        return sumOfManhattanDistancesBetweeenEachPairOfGalaxies / 2;
    }


}
