package aoc2024.common;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Runner {

    public static <T> void run(String message, Function<Path, T> solution, Run<T>... runs) {
        for (Run<T> run : runs) {
            run(message, solution, run.fileName, run.expectedResult);
        }
    }

    public static <T> void run(String message, BiFunction<Path, List<Object>, T> solution, Run<T>... runs) {
        for (Run<T> run : runs) {
            run(message, solution, run.fileName, run.params, run.expectedResult);
        }
    }

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

    public static <T> void run(String message, BiFunction<Path, List<Object>, T> solution, String fileName, List<Object> params, T expectedResult) {
        Path inputFilePath = calculatePath(solution, fileName);
        long startTime = System.currentTimeMillis();
        T result = solution.apply(inputFilePath, params);
        long endTime = System.currentTimeMillis();
        printResultMessage(message, fileName, expectedResult, result, endTime - startTime);
    }

    public record Run<T>(String fileName, T expectedResult, List<Object> params) {
        public static <T> Run<T> r(String fileName, T expectedResult) {
            return new Run<>(fileName, expectedResult, List.of());
        }

        public static <T> Run<T> r(String fileName, List<Object> params, T expectedResult) {
            return new Run<>(fileName, expectedResult, params);
        }
    }

    public static List<Object> p(Object... params) {
        return List.of(params);
    }

    private static <T> void printResultMessage(String message, String fileName, T expectedResult, T result, long duration) {
        String icon = result.equals(expectedResult) ? "✅" : "❌";
        System.out.printf("%s for %s is %s  --  expected: %s %s  --  (%sms)%n", message, fileName, result, expectedResult, icon, duration);
    }

    private static <T> Path calculatePath(Object solution, String fileName) {
        String[] split = solution.getClass().getPackage().getName().split("\\.");
        return Path.of("./java", split).resolve(fileName);
    }
}