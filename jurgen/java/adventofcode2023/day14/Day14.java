package adventofcode2023.day14;

import adventofcode2023.common.Input;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day14.Day14Input.EXAMPLE;
import static adventofcode2023.day14.Day14Input.INPUT;

class Day14 {

    public static void main(String[] args) {
        runnerFor(Day14.class)
                .execute("Puzzle 1", Day14::totalLoadOnNorthernBeams).withInput(EXAMPLE).expect(136)
                .execute("Puzzle 1", Day14::totalLoadOnNorthernBeams).withInput(INPUT).expect(111979)
                .execute("Puzzle 2", Day14::totalLoadOnNorthernBeamsAfterSpinCycles).withInput(EXAMPLE).expect(64) //brute force takes ~1800s
                .execute("Puzzle 2", Day14::totalLoadOnNorthernBeamsAfterSpinCycles).withInput(INPUT).expect(102055) // brute force would take about 130hours!!!
                .build()
                .run();
    }

    private static int totalLoadOnNorthernBeams(Input platformInput) {
        String[][] platform = Arrays.stream(platformInput.lines())
                .map(row -> row.split(""))
                .toArray(String[][]::new);

        for (int row = 1; row < platform.length; row++) {
            for (int column = 0; column < platform[row].length; column++) {
                if (platform[row][column].equals("O")) {
                    int targetRow = row;
                    while (targetRow - 1 >= 0 && platform[targetRow - 1][column].equals(".")) {
                        targetRow--;
                    }
                    platform[row][column] = ".";
                    platform[targetRow][column] = "O";
                }
            }
        }

        return calculateLoad(platform);
    }

    private static int totalLoadOnNorthernBeamsAfterSpinCycles(Input platformInput) {
        String[][] platform = Arrays.stream(platformInput.lines())
                .map(row -> row.split(""))
                .toArray(String[][]::new);

        // storage for already calculated platform statuses
        Map<String, Integer> platformStatusHashToCycle = new HashMap<>();
        Map<Integer, Integer> loadByCycle = new HashMap<>();
        platformStatusHashToCycle.put(md5Sum(platform), 0);

        // tilt platform
        int max_cycles = 1_000_000_000;
        for (int cycle = 1; cycle <= max_cycles; cycle++) {
            // NORTH
            for (int row = 1; row < platform.length; row++) {
                for (int column = 0; column < platform[row].length; column++) {
                    if (platform[row][column].equals("O")) {
                        int targetRow = row;
                        while (targetRow - 1 >= 0 && platform[targetRow - 1][column].equals(".")) {
                            targetRow--;
                        }
                        platform[row][column] = ".";
                        platform[targetRow][column] = "O";
                    }
                }
            }
            // WEST
            for (int column = 1; column < platform[0].length; column++) {
                for (int row = 0; row < platform.length; row++) {
                    if (platform[row][column].equals("O")) {
                        int targetColumn = column;
                        while (targetColumn - 1 >= 0 && platform[row][targetColumn - 1].equals(".")) {
                            targetColumn--;
                        }
                        platform[row][column] = ".";
                        platform[row][targetColumn] = "O";
                    }
                }
            }
            // SOUTH
            for (int row = platform.length - 2; row >= 0; row--) {
                for (int column = 0; column < platform[row].length; column++) {
                    if (platform[row][column].equals("O")) {
                        int targetRow = row;
                        while (targetRow + 1 < platform.length && platform[targetRow + 1][column].equals(".")) {
                            targetRow++;
                        }
                        platform[row][column] = ".";
                        platform[targetRow][column] = "O";
                    }
                }
            }
            // EAST
            for (int column = platform[0].length - 2; column >= 0; column--) {
                for (int row = 0; row < platform.length; row++) {
                    if (platform[row][column].equals("O")) {
                        int targetColumn = column;
                        while (targetColumn + 1 < platform[0].length && platform[row][targetColumn + 1].equals(".")) {
                            targetColumn++;
                        }
                        platform[row][column] = ".";
                        platform[row][targetColumn] = "O";
                    }
                }
            }

            //
            String platformHash = md5Sum(platform);
            if (platformStatusHashToCycle.containsKey(platformHash)) {
                // if we encountered this platform state before, figure out the period length and which offset to the current cycle the last cycle has within the period
                Integer previousOccurrenceCycle = platformStatusHashToCycle.get(platformHash);
                int cyclePeriodLength = cycle - previousOccurrenceCycle;
                int cycleOffset = (max_cycles - previousOccurrenceCycle) % cyclePeriodLength;
                // load in the end is the same as the load of the state with the same offset within the period
                return loadByCycle.get(previousOccurrenceCycle + cycleOffset);
            } else {
                // if state not encountered yet, store state(hash)->cycle and cycle->load mappings
                platformStatusHashToCycle.put(platformHash, cycle);
                loadByCycle.put(cycle, calculateLoad(platform));
            }
        }

        throw new IllegalStateException();
    }

    private static String md5Sum(String[][] platform) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            for (String[] strings : platform) {
                for (String string : strings) {
                    md5.update(string.getBytes());
                }
            }
            return String.format("%032x", new BigInteger(1, md5.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static int calculateLoad(String[][] platform) {
        int load = 0;
        for (int row = 0; row < platform.length; row++) {
            int rowLoad = 0;
            for (int column = 0; column < platform[row].length; column++) {
                if (platform[row][column].equals("O")) {
                    rowLoad += platform.length - row;
                }
            }
            load += rowLoad;
        }
        return load;
    }

}
