package aoc2024.d15;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static aoc2024.common.Input.streamInputLines;
import static aoc2024.common.Runner.Run.r;
import static aoc2024.common.Runner.run;
import static java.util.Arrays.stream;

public class D15 {
    public static void main(String[] args) {
        run("Sum of GPS locations", D15::sumOfGPSLocations,
                r("small-example.txt", 2028),
                r("large-example.txt", 10092),
                r("input.txt", 1478649)
        );
        run("Sum of GPS locations with larger boxes", D15::sumOfGPSLocationsWithLargerBoxes,
                r("small-example.txt", 2028),
                r("large-example.txt", 9021)
//                r("input.txt", 1478649)
        );
    }

    private static final Map<String, int[]> directions = Map.of(
            "<", new int[]{0, -1},
            ">", new int[]{0, 1},
            "^", new int[]{-1, 0},
            "v", new int[]{1, 0}
    );

    private static int sumOfGPSLocations(Path path) {
        AtomicInteger rowCount = new AtomicInteger(0);
        int[] robotLocation = new int[2];
        String[][] positions = streamInputLines(path)
                .filter(line -> line.contains("#"))
                .peek(line -> {
                    int row = rowCount.getAndIncrement();
                    if (line.contains("@")) {
                        robotLocation[0] = row;
                        robotLocation[1] = line.indexOf("@");
                    }
                })
                .map(line -> line.split(""))
                .toArray(String[][]::new);

        streamInputLines(path)
                .filter(line -> !line.contains("#") && !line.isEmpty())
                .flatMap(line -> stream(line.split("")))
                .forEach(move -> {
                    int[] diff = directions.get(move);
                    if (doMove(robotLocation, diff, positions)) {
                        robotLocation[0] += diff[0];
                        robotLocation[1] += diff[1];
                    }
                });

        print(positions);

        int sumOfGPSLocations = 0;
        for (int row = 0; row < positions.length; row++) {
            for (int col = 0; col < positions[row].length; col++) {
                if (positions[row][col].equals("O"))
                    sumOfGPSLocations += 100 * row + col;
            }
        }

        return sumOfGPSLocations;
    }

    private static int sumOfGPSLocationsWithLargerBoxes(Path path) {
        AtomicInteger rowCount = new AtomicInteger(0);
        int[] robotLocation = new int[2];
        String[][] positions = streamInputLines(path)
                .filter(line -> line.contains("#"))
                .map(line -> line.split(""))
                .map(originalLine -> {
                    int row = rowCount.getAndIncrement();
                    String[] expandedLine = new String[originalLine.length * 2];
                    for (int i = 0; i < originalLine.length; i++) {
                        switch (originalLine[i]) {
                            case "@" -> {
                                expandedLine[2 * i] = originalLine[i];
                                expandedLine[2 * i + 1] = ".";
                                robotLocation[0] = row;
                                robotLocation[1] = i * 2;
                            }
                            case "O" -> {
                                expandedLine[2 * i] = "[";
                                expandedLine[2 * i + 1] = "]";
                            }
                            default -> {
                                expandedLine[2 * i] = originalLine[i];
                                expandedLine[2 * i + 1] = originalLine[i];
                            }
                        }
                    }
                    return expandedLine;
                })
                .toArray(String[][]::new);

        print(positions);

        streamInputLines(path)
                .filter(line -> !line.contains("#") && !line.isEmpty())
                .flatMap(line -> stream(line.split("")))
                .forEach(move -> {
                    int[] diff = directions.get(move);
                    if (doMove(robotLocation, diff, positions)) {
                        robotLocation[0] += diff[0];
                        robotLocation[1] += diff[1];
                    }
                });

        print(positions);

        int sumOfGPSLocations = 0;
//        for (int row = 0; row < positions.length; row++) {
//            for (int col = 0; col < positions[row].length; col++) {
//                if (positions[row][col].equals("O"))
//                    sumOfGPSLocations += 100 * row + col;
//            }
//        }

        return sumOfGPSLocations;
    }

    private static void print(String[][] positions) {
        for (int row = 0; row < positions.length; row++) {
            for (int col = 0; col < positions[row].length; col++) {
                System.out.print(positions[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static boolean doMove(int[] location, int[] diff, String[][] positions) {
        String charAtPosition = positions[location[0]][location[1]];
        return switch (charAtPosition) {
            case "#" -> false;
            case "." -> true;
            case "@", "O" -> {
                int[] nextLocation = {location[0] + diff[0], location[1] + diff[1]};
                if (doMove(nextLocation, diff, positions)) {
                    positions[nextLocation[0]][nextLocation[1]] = charAtPosition;
                    positions[location[0]][location[1]] = ".";
                    yield true;
                }
                yield false;
            }
            default -> throw new IllegalArgumentException();
        };
    }
}
