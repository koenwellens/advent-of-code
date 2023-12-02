package common;

import java.util.List;
import java.util.function.Function;

public final class ActualInput<S, T extends AbstractObjectBasedOnInput<S>> extends AbstractInput<S, T> {

    public ActualInput(final int day, final String name, final Function<List<String>,T> constructor) {
        super("input/koen/day" + day + "/" + name +  ".txt", constructor);
    }

    @Override
    public T get() {
        return constructor.apply(fileContent());
    }
}