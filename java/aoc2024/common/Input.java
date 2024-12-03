package aoc2024.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Input {
    public static Stream<String> streamInputLines(Path path) {
        try {
            return Files.readAllLines(path)
                    .stream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readString(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}