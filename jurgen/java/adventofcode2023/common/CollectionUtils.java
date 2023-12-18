package adventofcode2023.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public final class CollectionUtils {
    @SafeVarargs
    public static <T> Stream<T> streamOfAll(Collection<? extends T>... collections) {
        return Arrays.stream(collections)
                .flatMap(Collection::stream);
    }

    @SafeVarargs
    public static <T> Stream<T> streamOfAll(Stream<? extends T>... streams) {
        return Arrays.stream(streams)
                .flatMap(Function.identity());
    }

    @SafeVarargs
    public static <T> Set<T> unionOf(Set<? extends T>... sets) {
        HashSet<T> union = new HashSet<>();
        for (Set<? extends T> set : sets) {
            union.addAll(set);
        }
        return union;
    }

    @SafeVarargs
    public static <T> Set<T> intersectionOf(Set<T>... sets) {
        HashSet<T> result = new HashSet<>(sets[0]);
        for (int i = 1; i < sets.length; i++) {
            result.retainAll(sets[i]);
        }
        return result;
    }
}
