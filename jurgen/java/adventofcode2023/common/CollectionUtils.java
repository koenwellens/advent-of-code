package adventofcode2023.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public final class CollectionUtils {
    @SafeVarargs
    public static <T> Stream<T> streamOfAll(Collection<? extends T>... collections) {
        return Arrays.stream(collections)
                .flatMap(Collection::stream);
    }

    @SafeVarargs
    public static <T> Set<T> unionOf(Set<? extends T>... sets) {
        HashSet<T> union = new HashSet<>();
        for (Set<? extends T> set : sets) {
            union.addAll(set);
        }
        return union;
    }
}
