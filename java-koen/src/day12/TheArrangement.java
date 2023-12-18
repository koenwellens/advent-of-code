package day12;

import java.util.List;

import static day12.Condition.DAMAGED;
import static day12.Condition.OPERATIONAL;

public final class TheArrangement implements Arrangement {
    private final List<Condition> conditions;
    private final List<Integer> groupSizes;

    public TheArrangement(final List<Condition> conditions, final List<Integer> groupSizes) {
        this.conditions = conditions;
        this.groupSizes = groupSizes;
    }

    @Override
    public boolean isValid() {
        var groupIndex = 0;
        var conditionIndex = 0;
        do {
            if (groupIndex == groupSizes.size()) {
                for (int ci = conditionIndex; ci < conditions.size(); ci++) {
                    if (conditions.get(ci) != OPERATIONAL) {
                        return false;
                    }
                }
            }

            final var condition = conditions.get(conditionIndex);
            if (condition == DAMAGED) {
                final var groupSize = groupSizes.get(groupIndex);
                for (int newIdx = 1; newIdx < groupSize; newIdx++) {
                    if (conditionIndex + newIdx == conditions.size() || conditions.get(conditionIndex + newIdx) != DAMAGED) {
                        return false;
                    }
                }

                if (conditionIndex + groupSize < conditions.size() && conditions.get(conditionIndex + groupSize) == DAMAGED) {
                    return false;
                }

                groupIndex++;
                conditionIndex += groupSize - 1;
            }
            conditionIndex++;
        } while (conditionIndex < conditions.size());

        return groupIndex == groupSizes.size();
    }

    @Override
    public String toString() {
        return conditions.toString();
    }
}
