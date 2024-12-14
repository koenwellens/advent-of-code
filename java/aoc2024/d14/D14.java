package aoc2024.d14;

import aoc2024.common.Input;
import aoc2024.common.Runner;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static aoc2024.common.Runner.Run.r;
import static aoc2024.common.Runner.run;
import static java.lang.Integer.parseInt;

public class D14 {
    public static void main(String[] args) {
        run("Safety factor", D14::totalSafetyFactor,
                r("example.txt", Runner.p(11, 7), 12),
                r("input.txt", Runner.p(101, 103), 225552000));
        // skipped part 2, didn't like looking for an undefined "Christmas tree"
        System.err.println("Part 2 skipped");
    }

    private static Integer totalSafetyFactor(Path path, List<Object> parameters) {
        int xSize = (int) parameters.getFirst();
        int ySize = (int) parameters.getLast();
        int[] quadrantCounts = parseRobotInfo(path)
                .map(performMoves(xSize, ySize, 100)) // perform 100 moves (seconds)
                .filter(pos -> pos[0] != xSize / 2 && pos[1] != ySize / 2) // filter out those on quadrant separation lines
                .map(pos -> (pos[0] < xSize / 2 ? 0 : 1) + (pos[1] < ySize / 2 ? 0 : 2)) // map position to quadrant
                .reduce(new int[4],  // count robots per quadrant
                        (quadrantCount, quadrant) -> {
                            quadrantCount[quadrant]++;
                            return quadrantCount;
                        },
                        (_, _) -> {
                            throw new IllegalArgumentException();
                        });
        return quadrantCounts[0] * quadrantCounts[1] * quadrantCounts[2] * quadrantCounts[3];
    }

    private static Function<int[][], int[]> performMoves(int xSize, int ySize, int moveCount) {
        return positionAndMove -> new int[]{
                (positionAndMove[0][0] + moveCount * (positionAndMove[1][0] + xSize)) % xSize,
                (positionAndMove[0][1] + moveCount * (positionAndMove[1][1] + ySize)) % ySize};
    }

    private static Stream<int[][]> parseRobotInfo(Path path) {
        return Input.streamInputLines(path)
                .map(line -> line.split("p=|,\\s?| v="))
                .map(strings -> new int[][]{
                        new int[]{parseInt(strings[1]), parseInt(strings[2])},
                        new int[]{parseInt(strings[3]), parseInt(strings[4])}});
    }

}
