package day13;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public final class ThePattern implements Pattern {

    private final List<String> rows;
    private final List<String> columns;

    public ThePattern(final List<String> lines) {
        this.rows = lines;
        this.columns = new ColumnPatternParser(lines).value();
    }

    @Override
    public long totalReflection() {
        final var numberOfColumnReflections = numberOfReflections(this.columns);
        final var numberOfRowReflections = numberOfReflections(this.rows);
        System.out.println(numberOfColumnReflections);
        System.out.println(numberOfRowReflections);

        return numberOfColumnReflections + 100 * numberOfRowReflections;
    }

    private long numberOfReflections(final List<String> rows) {
        Map<Integer, List<Integer>> reflectedRows = range(0, rows.size())
                .boxed()
                .collect(toMap(identity(), i -> new ArrayList<>()));

        for (int i = 0; i < rows.size(); i++) {
            for (int j = i + 1; j < rows.size(); j++) {
                if (rows.get(i).equals(rows.get(j))) {
                    reflectedRows.get(i).add(j);
                }
            }
        }

        double reflectionIndex = -1;
        for (int i = 0; i < rows.size(); i++) {
            final var possibleReflections = reflectedRows.get(i);
            if (possibleReflections.size() > 0) {
                if (possibleReflections.size() > 1) {
                    System.out.println("Uh oh");
                } else {
                    var reflectionFound = true;
                    for (int j = 0; i + j < rows.size(); j++) {
                        try {
                            if (reflectedRows.get(i + j).get(0) != rows.size() - 1 - j) {
                                reflectionFound = false;
                                break;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            break;
                        }
                    }

                    if(reflectionFound) {
                        reflectionIndex = ((double) i + (double) possibleReflections.get(0)) / 2;
                    }
                }
            }
        }

        var result = 0L;
        while (result < reflectionIndex) {
            result++;
        }

        System.out.println(reflectedRows);
        return result;
    }
}
