package aoc2024.d04;

import java.nio.file.Path;

import static aoc2024.common.Input.streamInputLines;
import static aoc2024.common.Runner.run;

class D04 {

    public static void main(String[] args) {
        run("XMAS word count", D04::xmasWordSearch, "example.txt", 18L);
        run("XMAS word count", D04::xmasWordSearch, "input.txt", 2571L);
        run("X-MAS cross count", D04::xmasCrossSearch, "example.txt", 9L);
        run("X-MAS cross count", D04::xmasCrossSearch, "input.txt", 9L);
    }

    private static Long xmasWordSearch(Path path) {
        String[][] puzzle = readPuzzle(path);
        long xmasCount = 0;
        for (int line = 0; line < puzzle.length; line++) {
            for (int column = 0; column < puzzle[line].length; column++) {
                if (puzzle[line][column].equals("X")) {
                    xmasCount += searchXmasWordStartingFromX(puzzle, line, column);
                }
            }
        }
        return xmasCount;
    }

    private static Long xmasCrossSearch(Path path) {
        String[][] puzzle = readPuzzle(path);
        long xmasCrossCount = 0;
        for (int line = 1; line < puzzle.length - 1; line++) {
            for (int column = 1; column < puzzle[line].length - 1; column++) {
                if (puzzle[line][column].equals("A")) {
                    xmasCrossCount += searchXmasCrossStartingFromAInTheCenter(puzzle, line, column);
                }
            }
        }
        return xmasCrossCount;
    }

    private static String[][] readPuzzle(Path path) {
        return streamInputLines(path)
                .map(line -> line.split(""))
                .toArray(String[][]::new);
    }

    private static long searchXmasWordStartingFromX(String[][] puzzle, int line, int column) {
        final int wordLength = "XMAS".length();
        final int[][] directions = new int[][]{
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
        long xmasCount = 0;
        for (int[] direction : directions) {
            if (line + (wordLength - 1) * direction[0] < 0
                    || line + (wordLength - 1) * direction[0] >= puzzle.length
                    || column + (wordLength - 1) * direction[1] < 0
                    || column + (wordLength - 1) * direction[1] >= puzzle[line].length)
                continue;

            if (puzzle[line + direction[0]][column + direction[1]].equals("M")
                    && puzzle[line + 2 * direction[0]][column + 2 * direction[1]].equals("A")
                    && puzzle[line + 3 * direction[0]][column + 3 * direction[1]].equals("S")) {
                xmasCount++;
            }
        }

        return xmasCount;
    }

    private static long searchXmasCrossStartingFromAInTheCenter(String[][] puzzle, int line, int column) {
        long xmasCrossCount = 0;

        int productOfMAndSBytes = 77 * 83;
        if (puzzle[line - 1][column - 1].getBytes()[0] * puzzle[line + 1][column + 1].getBytes()[0] == productOfMAndSBytes
                && puzzle[line + 1][column - 1].getBytes()[0] * puzzle[line - 1][column + 1].getBytes()[0] == productOfMAndSBytes)
            xmasCrossCount++;

        return xmasCrossCount;
    }

}
