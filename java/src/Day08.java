import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class Day08 implements DaySolution<Integer> {

    @Override
    public Integer part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            int[][] treeHeights = lines.map(Day08::parseLine)
                    .toArray(int[][]::new);

            int rowCount = treeHeights.length;
            int colCount = treeHeights[0].length;
            int maxRow = rowCount - 1;
            int maxCol = colCount - 1;

            Set<String> visibleTreeCoordinates = new TreeSet<>();
            for (int row = 0; row <= maxRow; row++) {
                int col = 0;
                int maxTreeHeightForRow = -1;
                while (col <= maxCol) { // check visibility from the left in this row
                    if (col == 0 || treeHeights[row][col] > maxTreeHeightForRow) {
                        visibleTreeCoordinates.add("" + row + "," + col);
                        maxTreeHeightForRow = treeHeights[row][col];
                    }
                    col++;
                }
                col = maxCol;
                maxTreeHeightForRow = -1;
                while (col >= 0) { // check visibility from the right in this row
                    if (col == maxCol || treeHeights[row][col] > maxTreeHeightForRow) {
                        visibleTreeCoordinates.add("" + row + "," + col);
                        maxTreeHeightForRow = treeHeights[row][col];
                    }
                    col--;
                }
            }
            for (int col = 0; col <= maxCol; col++) {
                int row = 0;
                int maxTreeHeightForCol = -1;
                while (row <= maxRow) { // check visibility from the top in this col
                    if (row == 0 || treeHeights[row][col] > maxTreeHeightForCol) {
                        visibleTreeCoordinates.add("" + row + "," + col);
                        maxTreeHeightForCol = treeHeights[row][col];
                    }
                    row++;
                }
                row = maxRow;
                maxTreeHeightForCol = -1;
                while (row >= 0) { // check visibility from the bottom in this col
                    if (row == maxRow || treeHeights[row][col] > maxTreeHeightForCol) {
                        visibleTreeCoordinates.add("" + row + "," + col);
                        maxTreeHeightForCol = treeHeights[row][col];
                    }
                    row--;
                }
            }

            System.out.printf("%s trees are visible%n", visibleTreeCoordinates.size());

            return visibleTreeCoordinates.size();
        }

    }

    @Override
    public Integer part2(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            int[][] treeHeights = lines.map(Day08::parseLine)
                    .toArray(int[][]::new);

            int rowCount = treeHeights.length;
            int colCount = treeHeights[0].length;
            int maxRow = rowCount - 1;
            int maxCol = colCount - 1;


            int winnerRow = -1;
            int winnerCol = -1;
            int winnerScenicScore = -1;
            for (int rowNr = 1; rowNr < maxRow; rowNr++) {
                for (int colNr = 1; colNr < maxCol; colNr++) {
                    int colToCheck = colNr - 1;
                    int lastCheckedHeight = -1;
                    int ctrLeft = 0;
                    while (colToCheck >= 0 && lastCheckedHeight < treeHeights[rowNr][colNr]) {
                        lastCheckedHeight = treeHeights[rowNr][colToCheck];
                        ctrLeft++;
                        colToCheck--;
                    }
                    System.out.println("" + rowNr + ", " + colNr + " left " + ctrLeft);

                    colToCheck = colNr + 1;
                    lastCheckedHeight = -1;
                    int ctrRight = 0;
                    while (colToCheck <= maxCol && lastCheckedHeight < treeHeights[rowNr][colNr]) {
                        lastCheckedHeight = treeHeights[rowNr][colToCheck];
                        ctrRight++;
                        colToCheck++;
                    }
                    System.out.println("" + rowNr + ", " + colNr + " right " + ctrRight);

                    int rowToCheck = rowNr - 1;
                    lastCheckedHeight = -1;
                    int ctrUp = 0;
                    while (rowToCheck >= 0 && lastCheckedHeight < treeHeights[rowNr][colNr]) {
                        lastCheckedHeight = treeHeights[rowToCheck][colNr];
                        ctrUp++;
                        rowToCheck--;
                    }
                    System.out.println("" + rowNr + ", " + colNr + " up " + ctrUp);

                    rowToCheck = rowNr + 1;
                    lastCheckedHeight = -1;
                    int ctrDown = 0;
                    while (rowToCheck <= maxRow && lastCheckedHeight < treeHeights[rowNr][colNr]) {
                        lastCheckedHeight = treeHeights[rowToCheck][colNr];
                        ctrDown++;
                        rowToCheck++;
                    }
                    System.out.println("" + rowNr + ", " + colNr + " down " + ctrDown);

                    int scenicScore = ctrLeft * ctrRight * ctrUp * ctrDown;
                    if (scenicScore > winnerScenicScore) {
                        winnerScenicScore = scenicScore;
                        winnerRow = rowNr;
                        winnerCol = colNr;
                    }
                }
            }

            System.out.println("Most scenic place is " + winnerRow + ", " + winnerCol + " scoring " + winnerScenicScore);

            return winnerScenicScore;
        }
    }

    @NotNull
    private static int[] parseLine(String line) {
        int[] lineHeights = new int[line.length()];
        for (int i = 0; i < line.length(); i++) {
            lineHeights[i] = Integer.parseInt(line.substring(i, i + 1));
        }
        return lineHeights;
    }
}
