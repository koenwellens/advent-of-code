package aoc2024.common;

import java.nio.file.Path;
import java.util.function.Function;

public class Runner {
    public static <T> void run(String message, Function<Path, T> solution, String fileName, T expectedResult) {
        Path inputFilePath = calculatePath(solution, fileName);
        T result = solution.apply(inputFilePath);
        printResultMessage(message, fileName, expectedResult, result);
    }

    private static <T> void printResultMessage(String message, String fileName, T expectedResult, T result) {
        String icon = result.equals(expectedResult) ? "✅" : "❌";
        System.out.printf("%s for %s is: %s expected: %s %s%n", message, fileName, result, expectedResult, icon);
    }

    private static <T> Path calculatePath(Function<Path, T> solution, String fileName) {
        String[] split = solution.getClass().getPackage().getName().split("\\.");
        return Path.of("./java", split).resolve(fileName);
    }
}