import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.util.Arrays.fill;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.IntStream.range;

public class Day09 implements DaySolution<Long> {

    private static final Map<String, int[]> DIRECTION_VECTORS = Map.of(
            "R", new int[]{1, 0},
            "L", new int[]{-1, 0},
            "U", new int[]{0, 1},
            "D", new int[]{0, -1});
    private static final int[] NO_MOVE_VECTOR = {0, 0};

    @Override
    public Long part1(Path inputFilePath) {
        return moveRope(2, inputFilePath);
    }

    @Override
    public Long part2(Path inputFilePath) {
        return moveRope(10, inputFilePath);
    }

    private long moveRope(int ropeLength, Path ropeMovementsInputFilePath) {
        int[][] ropePositions = new int[ropeLength][2];
        fill(ropePositions, new int[]{0, 0});

        // realisatie: elke keer Tail zich moet verplaatsen neemt ie de plaats van Head in - to verify
        try (Stream<String> lines = lines(ropeMovementsInputFilePath)) {
            long uniqueTailPositionsCount = lines
                    .flatMap(this::parseToDirectionVectors)
                    .map(headDirectionVector -> {
                        int[] nextNodeDirectionVector = headDirectionVector;
                        for (int i = 0; i < ropeLength; i++) {
                            ropePositions[i] = new int[]{ropePositions[i][0] + nextNodeDirectionVector[0], ropePositions[i][1] + nextNodeDirectionVector[1]};
                            if (i + 1 < ropeLength) {
                                int[] nextNodeDiffVector = new int[]{ropePositions[i][0] - ropePositions[i + 1][0], ropePositions[i][1] - ropePositions[i + 1][1]};
                                if (abs(nextNodeDiffVector[0]) > 1 || abs(nextNodeDiffVector[1]) > 1)
                                    nextNodeDirectionVector = new int[]{
                                            nextNodeDiffVector[0] == 0 ? 0 : abs(nextNodeDiffVector[0]) == 1 ? nextNodeDiffVector[0] : nextNodeDiffVector[0] / abs(nextNodeDiffVector[0]),
                                            nextNodeDiffVector[1] == 0 ? 0 : abs(nextNodeDiffVector[1]) == 1 ? nextNodeDiffVector[1] : nextNodeDiffVector[1] / abs(nextNodeDiffVector[1])};
                                else nextNodeDirectionVector = NO_MOVE_VECTOR;
                            }
                        }
                        return ropePositions[ropeLength - 1];
                    })
                    .collect(toCollection(() -> new TreeSet<>(Arrays::compare)))
                    .size();

            System.out.println("Tail occupied " + uniqueTailPositionsCount + " unique positions");

            return uniqueTailPositionsCount;
        }
    }

    private Stream<int[]> parseToDirectionVectors(String line) {
        String[] commandData = line.split(" ");
        return range(0, Integer.parseInt(commandData[1]))
                .mapToObj(i -> DIRECTION_VECTORS.get(commandData[0]));
    }

}
