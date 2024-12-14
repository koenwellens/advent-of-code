package aoc2024.d13;

import aoc2024.common.Input;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Gatherers;

import static aoc2024.common.Runner.Run.r;
import static aoc2024.common.Runner.run;
import static java.lang.Long.parseLong;

@SuppressWarnings("ALL")
public class D13 {
    public static void main(String[] args) {
        run("Fewest coins to win all prizes", D13::fewestTokensToWinAllPrizes,
                r("example.txt", 480L),
                r("input.txt", 28138L)
        );
        run("Fewest coins to win all prizes (but larger)", D13::fewestTokensToWinAllPrizesButLarger,
                r("example.txt", 875318608908L), // answer not provided
                r("input.txt", 108394825772874L)
        );
    }

    private static final int x = 0;
    private static final int y = 1;
    private static final int aCost = 3;
    private static final int bCost = 1;

    private static long fewestTokensToWinAllPrizes(Path path) {
        return fewestTokensToWinAllPrizesWithOption(path, false);
    }

    private static long fewestTokensToWinAllPrizesButLarger(Path path) {
        return fewestTokensToWinAllPrizesWithOption(path, true);
    }

    private static long fewestTokensToWinAllPrizesWithOption(Path path, boolean larger) {
        return Input.streamInputLines(path)
                .filter(line -> !line.isEmpty())
                .gather(Gatherers.windowFixed(3))
                .map(machineConfigs -> {
                    String[] aButtonInfo = machineConfigs.get(0).split("X\\+|, Y\\+");
                    String[] bButtonInfo = machineConfigs.get(1).split("X\\+|, Y\\+");
                    String[] targetInfo = machineConfigs.get(2).split("X=|, Y=");

                    long[] A = new long[]{parseLong(aButtonInfo[1]), parseLong(aButtonInfo[2])};
                    long[] B = new long[]{parseLong(bButtonInfo[1]), parseLong(bButtonInfo[2])};
                    long[] T = new long[]{parseLong(targetInfo[1]), parseLong(targetInfo[2])};
                    if (larger) {
                        T[0] += 10000000000000L;
                        T[1] += 10000000000000L;
                    }

                    // linear algebra
                    // A * Ax + B * Bx = Tx
                    // A * Ay + B * By = Ty
                    //
                    // => (multiply by Ay)   A * Ax * Ay + B * Bx * Ay = Tx * Ay
                    // => (multiply by Ax)   A * Ay * Ax + B * By * Ax = Ty * Ax
                    //
                    // => (subtract)   B * (Bx * Ay - By * Ax) = Tx * Ay - Ty * Ax
                    //
                    // => (solve B)   B = (Tx * Ay - Ty * Ax) / (Bx * Ay - By * Ax)
                    // => (solve A)   A = (Tx - B * Bx) / Ax

                    return divideIfIntegerResult(T[x] * A[y] - T[y] * A[x], B[x] * A[y] - B[y] * A[x]) // solve B
                            .flatMap(b ->
                                    divideIfIntegerResult(T[x] - b * B[x], A[x]) // solve A
                                            .map(a -> new long[]{a, b})) // and return both counts
                            .filter(counts -> counts[0] >= 0 && counts[1] >= 0)
                            .filter(counts -> larger ^ (counts[0] <= 100 && counts[1] <= 100))
                            .map(counts -> aCost * counts[0] + bCost * counts[1]) // cost
                            .orElse(0L);
                })
                .reduce(Long::sum)
                .orElseThrow();
    }

    private static Optional<Long> divideIfIntegerResult(long solveBNumerator, long solveBDenominator) {
        if (solveBNumerator % solveBDenominator > 0) {
            return Optional.empty();
        }
        return Optional.of(solveBNumerator / solveBDenominator);
    }
}
