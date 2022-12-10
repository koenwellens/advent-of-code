import kotlin.NotImplementedError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface DaySolution<R> {
    default R part1(Path inputFilePath) {
        throw new NotImplementedError("Part 1 not implemented");
    }

    default R part2(Path inputFilePath) {
        throw new NotImplementedError("Part 2 not implemented");
    }

    default int getDayNr() {
        return Integer.parseInt(getClass().getSimpleName().substring(3, 5));
    }


    default Stream<String> lines(Path path) {
        try {
            return Files.lines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
