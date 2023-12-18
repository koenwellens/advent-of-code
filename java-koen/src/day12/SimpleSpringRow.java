package day12;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static day12.Condition.DAMAGED;
import static day12.Condition.OPERATIONAL;
import static java.util.Collections.emptySet;

public final class SimpleSpringRow implements Row {

    private final List<Condition> conditions;
    private final List<Integer> groupSizes;
    private final long totalNumberOfDamagedSprings;

    public SimpleSpringRow(final String line) {
        this(line, 1);
    }

    public SimpleSpringRow(final String line, final int timesToUnfold) {
        this(new UnfoldedConditionParser(line, timesToUnfold).value(), new UnfoldedGroupSizeParser(line, 1).value());
    }

    public SimpleSpringRow(final List<Condition> conditions, final List<Integer> groupSizes) {
        this.conditions = conditions;
        this.groupSizes = groupSizes;
        this.totalNumberOfDamagedSprings = this.groupSizes.stream()
                .mapToLong(i -> i)
                .sum();
    }

    @Override
    public long possibleArrangements() {
        final var arrangements = createArrangements(conditions, 0, new ArrayList<>());

        return arrangements.stream()
                .filter(Arrangement::isValid)
                .count();
    }

    private Set<Arrangement> createArrangements(List<Condition> conditions, int index, List<Condition> currentList) {
        if (currentList.stream().filter(c -> DAMAGED == c).count() > this.totalNumberOfDamagedSprings) {
            return emptySet();
        }

        if (index == conditions.size()) {
            return Set.of(new TheArrangement(currentList, groupSizes));
        }

        switch (conditions.get(index)) {
            case UNKNOWN -> {
                final var arrangementsWithDot = createArrangements(conditions, index + 1, addAndReturn(currentList, OPERATIONAL));
                final var arrangementsWithHash = createArrangements(conditions, index + 1, addAndReturn(currentList, DAMAGED));
                final var result = new HashSet<>(arrangementsWithDot);
                result.addAll(arrangementsWithHash);
                return result;
            }
            case DAMAGED -> {
                return createArrangements(conditions, index + 1, addAndReturn(currentList, DAMAGED));
            }
            case OPERATIONAL -> {
                return createArrangements(conditions, index + 1, addAndReturn(currentList, OPERATIONAL));
            }
        }

        return emptySet();
    }

    private List<Condition> addAndReturn(List<Condition> currentList, Condition condition) {
        final var result = new ArrayList<>(currentList);
        result.add(condition);
        return result;
    }
}
