package aoc2024.d07;

import aoc2024.common.Input;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BinaryOperator;

import static aoc2024.common.Runner.run;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOfRange;

public class D07 {

    public static void main(String[] args) {
//        run("Total calibration result", D07::totalCalibrationResultWithNoConcat, "example.txt", 3749L);
//        run("Total calibration result", D07::totalCalibrationResultWithNoConcat, "input.txt", 6083020304036L);
        run("Total calibration result", D07::totalCalibrationResultWithConcat, "example.txt", 11387L);
        run("Total calibration result", D07::totalCalibrationResultWithConcat, "input.txt", 11387L);

    }

    private static Long totalCalibrationResultWithNoConcat(Path path) {
        return totalCalibrationResult(path, false);
    }

    private static Long totalCalibrationResultWithConcat(Path path) {
        return totalCalibrationResult(path, true);
    }

    private static Long totalCalibrationResult(Path path, boolean includeConcat) {
        AtomicLong result = new AtomicLong();
        Input.streamInputLines(path)
                .parallel()
                .map(line -> {
                    String[] split = line.split(": | ");
                    long[] values = new long[split.length];
                    for (int i = 0; i < split.length; i++) {
                        values[i] = Long.parseLong(split[i]);
                    }
                    return values;
                })
                .forEach(values -> {
                    System.err.println(Arrays.toString(values));
                    long[] operands = copyOfRange(values, 1, values.length);
                    if (canCombine(values[0], operands, includeConcat))
                        result.addAndGet(values[0]);
                });

        return result.get();
    }

    private static boolean canCombine(long requiredResult, long[] operands, boolean includeConcat) {
//        System.out.printf("%s = %s%n", requiredResult, Arrays.toString(operands));
        if (operands.length == 1)
            return operands[0] == requiredResult;
        if (operands[0] >= requiredResult)
            return false;

        long[] remainingOperands = copyOfRange(operands, 1, operands.length);
        return canCombine(requiredResult, executeOperation((x, y) -> x * y, operands), includeConcat)
                || canCombine(requiredResult, executeOperation(Long::sum, operands), includeConcat)
                || (includeConcat && concatOperands(requiredResult, operands));
    }

    private static long[] executeOperation(BinaryOperator<Long> operation, long[] operands) {
        long[] newOperands = copyOfRange(operands, 1, operands.length);
        newOperands[0] = operation.apply(operands[0], operands[1]);
        return newOperands;
    }

    private static boolean concatOperands(long requiredResult, long[] operands) {
        boolean combinationFound = false;
        for (int i = 0; !combinationFound && i < operands.length - 1; i++) {
            long combinedOperand = concatOperands(operands[i], operands[i + 1]);
            if (combinedOperand > requiredResult)
                continue;

            long[] newOperands = Arrays.copyOf(operands, operands.length - 1);
            newOperands[i] = combinedOperand;
            arraycopy(operands, i + 2, newOperands, i + 1, newOperands.length - (i + 1));
            combinationFound = canCombine(requiredResult, newOperands, true);
        }
        return combinationFound;
    }

    private static long concatOperands(long operand1, long operand2) {
        long combined = operand1;
        long rest = operand2;
        while (rest > 0) {
            combined = combined * 10;
            rest = rest / 10;
        }
        return combined + operand2;
    }

}
