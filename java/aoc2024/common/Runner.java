package aoc2024.common;

import java.nio.file.Path;
import java.util.function.Function;

public class Runner {
    public static <T> void run(String message, Function<Path, T> solution, String fileName, T expectedResult) {
        String[] split = solution.getClass().getPackage().getName().split("\\.");
        Path path = Path.of("./src", split).resolve(fileName);
        T result = solution.apply(path);
        String icon = result.equals(expectedResult) ? "✅" : "❌";
        System.out.printf("%s for %s is: %s expected: %s %s%n", message, fileName, result, expectedResult, icon);
    }
}