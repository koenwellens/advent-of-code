package common;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CollectionTools {

    @NotNull
    public static <T> List<T> copyWithoutElement(List<T> list, T element) {
        LinkedList<T> copy = new LinkedList<>(list);
        copy.remove(element);
        return List.copyOf(copy);
    }

    @NotNull
    public static <T> List<T> copyWithoutElements(List<T> list, Stream<T> elements) {
        LinkedList<T> copy = new LinkedList<>(list);
        elements.forEach(copy::remove);
        return List.copyOf(copy);
    }

    public static <K, V> Map<K, V> copyWith(Map<K, V> combo, K key, V value) {
        HashMap<K, V> next = new HashMap<>(combo);
        next.put(key, value);
        return Map.copyOf(next);
    }

}
