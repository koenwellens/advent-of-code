package aoc2024.d08;

import aoc2024.common.Input;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static aoc2024.common.Runner.run;

public class D08 {
    public static void main(String[] args) {
        run("Number of unique antinode locations", D08::countNumberOfUniqueAntinodeLocations, "example.txt", 14);
        run("Number of unique antinode locations", D08::countNumberOfUniqueAntinodeLocations, "input.txt", 308);
        run("Number of unique antinode locations with harmonics", D08::countNumberOfUniqueAntinodeLocationsWithHarmonics, "example.txt", 34);
        run("Number of unique antinode locations with harmonics", D08::countNumberOfUniqueAntinodeLocationsWithHarmonics, "input.txt", 1147);
    }

    private static int countNumberOfUniqueAntinodeLocations(Path path) {
        return countNumberOfUniqueAntinodeLocations(path, false);
    }

    private static int countNumberOfUniqueAntinodeLocationsWithHarmonics(Path path) {
        return countNumberOfUniqueAntinodeLocations(path, true);
    }

    private static int countNumberOfUniqueAntinodeLocations(Path path, boolean harmonic) {
        Map<Character, List<Location>> antennaLocationsByFrequency = new HashMap<>();
        AtomicInteger rowCount = new AtomicInteger(0);
        AtomicInteger colCount = new AtomicInteger(0);
        Input.streamInputLines(path)
                .forEach(row -> {
                    int rowNumber = rowCount.getAndIncrement();
                    colCount.set(row.length());
                    for (int i = 0; i < row.length(); i++) {
                        if ((row.codePointAt(i) >= 'A' && row.codePointAt(i) <= 'Z')
                                || (row.codePointAt(i) >= 'a' && row.codePointAt(i) <= 'z')
                                || row.codePointAt(i) >= '0' && row.codePointAt(i) <= '9') {
                            antennaLocationsByFrequency.computeIfAbsent(row.charAt(i), k -> new ArrayList<>())
                                    .add(new Location(rowNumber, i));
                        }
                    }
                });

        Set<Location> antiNodeLocations = new HashSet<>();
        antennaLocationsByFrequency.values()
                .stream()
                .filter(nodeLocations -> nodeLocations.size() > 1)
                .forEach(nodeLocations -> {
                    for (int n = 0; n < nodeLocations.size() - 1; n++) {
                        for (int m = n + 1; m < nodeLocations.size(); m++) {
                            Location nodeN = nodeLocations.get(n);
                            Location nodeM = nodeLocations.get(m);

                            if (harmonic) {
                                antiNodeLocations.add(nodeN);
                                antiNodeLocations.add(nodeM);
                            }

                            int diffX = nodeN.x - nodeM.x;
                            int diffY = nodeN.y - nodeM.y;

                            for (int i = 1; harmonic || i < 2; i++) {
                                int antiNodeX = nodeN.x + i * diffX;
                                int antiNodeY = nodeN.y + i * diffY;
                                if (antiNodeX >= 0 && antiNodeX < rowCount.get()
                                        && antiNodeY >= 0 && antiNodeY < colCount.get()) {
                                    antiNodeLocations.add(new Location(antiNodeX, antiNodeY));
                                } else
                                    break;
                            }

                            for (int i = -1; harmonic || i > -2; i--) {
                                int antiNodeX = nodeM.x + i * diffX;
                                int antiNodeY = nodeM.y + i * diffY;
                                if (antiNodeX >= 0 && antiNodeX < rowCount.get()
                                        && antiNodeY >= 0 && antiNodeY < colCount.get()) {
                                    antiNodeLocations.add(new Location(antiNodeX, antiNodeY));
                                } else
                                    break;
                            }

                        }
                    }
                });

        return antiNodeLocations.size();
    }

    private static void addAntiNodeLocations(List<Location> nodeLocations, Set<Location> antinodeCollector) {
        if (nodeLocations.size() < 2) {
            throw new IllegalArgumentException("Number of node locations is too small");
        }
        if (nodeLocations.size() == 2) {

        }
    }

    record Location(int x, int y) {
    }
}
