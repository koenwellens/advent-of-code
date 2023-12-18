package day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public final class ImprovedSpringRow implements Row {

    private final String conditions;
    private final List<Integer> groupSizes;
    private final long totalNumberOfDamagedSprings;

    public ImprovedSpringRow(final String line) {
        this(line, 1); // TODO 5
    }

    public ImprovedSpringRow(final String line, final int timesToUnfold) {
        this(line.split(" ")[0].trim(), new UnfoldedGroupSizeParser(line, timesToUnfold).value());
    }

    public ImprovedSpringRow(final String conditions, final List<Integer> groupSizes) {
        this.conditions = conditions;
        this.groupSizes = groupSizes;
        this.totalNumberOfDamagedSprings = this.groupSizes.stream()
                .mapToLong(i -> i)
                .sum();
    }

    @Override
    public long possibleArrangements() {
        final var noDots = Arrays.stream(this.conditions.split("\\.")).filter(s -> !s.isEmpty()).toList();
        if (noDots.size() == groupSizes.size()) {
            var result = 1L;
            for (int i = 0; i < groupSizes.size(); i++) {
                result *= possibleCombinations(noDots.get(i), groupSizes.get(i));
            }
            return result;
        } else if(noDots.size() < groupSizes.size()) {
            var result = 1L;
            for (int i = 0; i < noDots.size(); i++) {
                // compute how many groups fit in the first string
                // compute the number of ways those groups fit in the first string
                //
                final var reqCounts = getRequiredCounts(noDots.get(i), groupSizes);
                System.out.println("Required counts for '" + noDots.get(i) + "':");
                System.out.println(reqCounts);

            }
        }
        System.out.println(noDots);
        System.out.println(groupSizes);
        return 0L;
    }

    private List<Integer> getRequiredCounts(String s, List<Integer> associatedNumbers) {
        List<Integer> counts = new ArrayList<>();
        int currentCount = 0;

        for (char c : s.toCharArray()) {
            if (c == '#') {
                currentCount++;
            } else if (currentCount > 0) {
                counts.add(currentCount);
                currentCount = 0;
            }
        }

        if (currentCount > 0) {
            counts.add(currentCount);
        }

        List<Integer> requiredCounts = new ArrayList<>();
        int totalSize = 0;

        for (int count : counts) {
            totalSize += count;
            requiredCounts.add(count);
            if (totalSize >= associatedNumbers.size()) {
                break;
            }
        }

        return requiredCounts.subList(0, associatedNumbers.size());
    }

    private long possibleCombinations(final String s, final int size) {
        int n = s.length();

        long[][] waysToFormConsecutiveHashtagsWithTheFirstCharacters = new long[n + 1][size + 1];

        // Base case: there is only one way to form 0 consecutive '#'
        for (int i = 0; i <= n; i++) {
            waysToFormConsecutiveHashtagsWithTheFirstCharacters[i][0] = 1;
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int j = 1; j <= size; j++) {
                if (s.charAt(i) == '#') {
                    // If the current character is '#', update dp[i][j] based on the next state
                    waysToFormConsecutiveHashtagsWithTheFirstCharacters[i][j] = waysToFormConsecutiveHashtagsWithTheFirstCharacters[i + 1][j - 1];
                } else {
                    // If the current character is '?', consider both possibilities: replace with '#' or skip
                    waysToFormConsecutiveHashtagsWithTheFirstCharacters[i][j] = waysToFormConsecutiveHashtagsWithTheFirstCharacters[i + 1][j - 1] + waysToFormConsecutiveHashtagsWithTheFirstCharacters[i + 1][j];
                }
            }
        }

        return waysToFormConsecutiveHashtagsWithTheFirstCharacters[0][size];
    }
}
