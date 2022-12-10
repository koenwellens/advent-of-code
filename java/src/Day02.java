import java.nio.file.Path;
import java.util.stream.Stream;

public class Day02 implements DaySolution<Integer> {

    /*
     *     Rock      X = A = 0
     *     Paper     Y = B = 1
     *     Scissors  Z = C = 2
     */

    @Override
    public Integer part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            Integer totalScore = lines
                    .map(this::scoreForRoundWhenSecondColumnIndicatesYourSymbol)
                    .reduce(Integer::sum)
                    .orElseThrow();
            System.out.printf("Your total score in part 1 is %s\n", totalScore);
            return totalScore;
        }
    }

    @Override
    public Integer part2(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            Integer totalScore = lines
                    .map(this::scoreForRoundWhenSecondColumnIndicatesYourResult)
                    .reduce(Integer::sum)
                    .orElseThrow();
            System.out.printf("Your total score in part 2 is %s\n", totalScore);
            return totalScore;
        }
    }

    private Integer scoreForRoundWhenSecondColumnIndicatesYourSymbol(String roundString) {
        int adv = roundString.charAt(0) - 'A';
        int mine = roundString.charAt(2) - 'X';

        int score = roundScore(adv, mine);

//        System.err.printf("Score for %s is %s\n", roundString, score);
        return score;
    }

    private Integer scoreForRoundWhenSecondColumnIndicatesYourResult(String roundString) {
        int adv = roundString.charAt(0) - 'A';
        int mine = (adv + roundString.charAt(2) - 'X' - 1 + 3) % 3;

        int score = roundScore(adv, mine);

//        System.err.printf("Score for %s is %s\n", roundString, score);
        return score;
    }

    private int roundScore(int adv, int mine) {
        int score = mine + 1;

        if ((mine - adv + 3) % 3 == 1)
            score += 6;

        if ((mine - adv + 3) % 3 == 0)
            score += 3;

        return score;
    }

}
