package aoc2024.common;

import java.util.HashSet;
import java.util.Set;

public class Collections {
    public static <T> Set<T> union(Set<T> ts, T t) {
        Set<T> result = new HashSet<>(ts);
        result.add(t);
        return Set.copyOf(result);
    }

    public record Tuple2<A, B>(A first, B second) {
    }
}
