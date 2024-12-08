package aoc2024.d07;

import aoc2024.common.Input;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

import static aoc2024.common.Runner.run;
import static java.util.Arrays.copyOfRange;

public class D07 {

    public static void main(String[] args) {
        run("Total calibration result", D07::totalCalibrationResultWithConcatNotAllowed, "example.txt", 3749L);
        run("Total calibration result", D07::totalCalibrationResultWithConcatNotAllowed, "input.txt", 6083020304036L);
        run("Total calibration result with concatenation", D07::totalCalibrationResultWithConcatAllowed, "example.txt", 11387L);
        run("Total calibration result with concatenation", D07::totalCalibrationResultWithConcatAllowed, "input.txt", 59002246504791L);
    }

    private static Long totalCalibrationResultWithConcatNotAllowed(Path path) {
        return totalCalibrationResult(path, false);
    }

    private static Long totalCalibrationResultWithConcatAllowed(Path path) {
        return totalCalibrationResult(path, true);
    }

    private static Long totalCalibrationResult(Path path, boolean concatAllowed) {
        AtomicLong result = new AtomicLong();
        Input.streamInputLines(path)
                .map(line -> {
                    String[] split = line.split(": | ");
                    long[] values = new long[split.length];
                    for (int i = 0; i < split.length; i++) {
                        values[i] = Long.parseLong(split[i]);
                    }
                    return values;
                })
                .forEach(values -> {
                    long[] operands = copyOfRange(values, 1, values.length);
                    if (canCombine(values[0], operands, concatAllowed))
                        result.addAndGet(values[0]);
                });

        return result.get();
    }

    private static boolean canCombine(long requiredResult, long[] operands, boolean concatAllowed) {
        if (operands.length == 1)
            return operands[0] == requiredResult;

        long operand = operands[operands.length - 1];
        long[] remainingOperands = copyOfRange(operands, 0, operands.length - 1);
        return (requiredResult % operand == 0 && canCombine(requiredResult / operand, remainingOperands, concatAllowed))
                || (requiredResult - operand > 0 && canCombine(requiredResult - operand, remainingOperands, concatAllowed))
                || (concatAllowed && isConcatedAtEnd(requiredResult, operand) && canCombine(unConcat(requiredResult, operand), remainingOperands, true));
    }

    private static long unConcat(long requiredResult, long operand) {
        long unconcat = requiredResult;
        long div = operand;
        while (div > 0) {
            unconcat /= 10;
            div /= 10;
        }
        return unconcat;
    }

    private static boolean isConcatedAtEnd(long requiredResult, long operand) {
        int order = 1;
        long div = operand;
        while (div > 0) {
            order *= 10;
            div /= 10;
        }
        return requiredResult % order == operand;
    }
}
