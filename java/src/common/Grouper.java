package common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Grouper<T> {
    private final Predicate<Group<T>> groupCompletePredicate;
    private Group<T> currentGroup;

    public static <I> Grouper<I> groupBy(Predicate<Group<I>> groupCompletePredicate) {
        return new Grouper<>(groupCompletePredicate);
    }

    public Grouper(Predicate<Group<T>> groupCompletePredicate) {
        this.groupCompletePredicate = groupCompletePredicate;
    }

    public static <T> Grouper<T> groupByItemCount(int count) {
        return groupBy(g -> g.items.size() == count);
    }

    public Group<T> add(T string) {
        if (currentGroup == null || groupCompletePredicate.test(currentGroup)) {
            currentGroup = new Group<>(groupCompletePredicate);
        }

        currentGroup.add(string);

        return currentGroup;
    }

    public static class Group<T> {

        private final List<T> items = new ArrayList<>();
        private final Predicate<Group<T>> groupCompletePredicate;

        public Group(Predicate<Group<T>> groupCompletePredicate) {
            this.groupCompletePredicate = groupCompletePredicate;
        }

        void add(T string) {
            items.add(string);
        }

        public List<T> items() {
            return items;
        }

        public boolean isComplete() {
            return groupCompletePredicate.test(this);
        }
    }
}
