package common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractInput<S, T extends AbstractObjectBasedOnInput<S>> implements Input<T> {

    private final String fileName;
    protected final Function<List<String>, T> constructor;

    public AbstractInput(final String fileName, final Function<List<String>, T> constructor) {
        this.fileName = fileName;
        this.constructor = constructor;
    }

    protected List<String> fileContent() {
        try {
            return Files.readAllLines(Paths.get(this.fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
