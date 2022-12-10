import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Grouper {
    private final Predicate<Group> groupCompletePredicate;
    private Group currentGroup;

    public static Grouper groupBy(Predicate<Group> groupCompletePredicate) {
        return new Grouper(groupCompletePredicate);
    }

    public Grouper(Predicate<Group> groupCompletePredicate) {
        this.groupCompletePredicate = groupCompletePredicate;
    }

    public static Grouper groupByItemCount(int count) {
        return groupBy(g -> g.items.size() == count);
    }

    public Group add(String string) {
        if (currentGroup == null || groupCompletePredicate.test(currentGroup)) {
            currentGroup = new Group();
        }

        currentGroup.add(string);

        return currentGroup;
    }

    static class Group {

        private final List<String> items = new ArrayList<>();

        void add(String string) {
            items.add(string);
        }

        public List<String> items() {
            return items;
        }
    }
}
