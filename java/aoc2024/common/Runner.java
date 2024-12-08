package aoc2024.common;

import java.nio.file.Path;
import java.util.function.Function;

public class Runner {


    public static <T> void run(String message, Function<Path, T> solution,
                               String fileName1, T expectedResult1,
                               String fileName2, T expectedResult2) {
        run(message, solution, fileName1, expectedResult1);
        run(message, solution, fileName2, expectedResult2);
    }

    public static <T> void run(String message, Function<Path, T> solution, String fileName, T expectedResult) {
        Path inputFilePath = calculatePath(solution, fileName);
        long startTime = System.currentTimeMillis();
        T result = solution.apply(inputFilePath);
        long endTime = System.currentTimeMillis();
        printResultMessage(message, fileName, expectedResult, result, endTime - startTime);
    }

    private static <T> void printResultMessage(String message, String fileName, T expectedResult, T result, long duration) {
        String icon = result.equals(expectedResult) ? "✅" : "❌";
        System.out.printf("%s for %s is %s  --  expected: %s %s  --  (%sms)%n", message, fileName, result, expectedResult, icon, duration);
    }

    private static <T> Path calculatePath(Function<Path, T> solution, String fileName) {
        String[] split = solution.getClass().getPackage().getName().split("\\.");
        return Path.of("./java", split).resolve(fileName);
    }
}