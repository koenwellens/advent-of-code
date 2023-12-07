package adventofcode2023.common;

import java.util.function.BinaryOperator;
import java.util.function.LongBinaryOperator;

public final class LongOperations {
    public static BinaryOperator<Long> MULTIPLY = (a, b) -> a * b;
    public static LongBinaryOperator L_MULTIPLY = (a, b) -> a * b;
}
