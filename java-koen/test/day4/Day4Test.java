package day4;

import common.AbstractTest;
import common.AdventOfCodeTest;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 4,
        example1 = "13",
        example2 = "30",
        input1 = "19135",
        input2 = "5704953",
        text = "The large pile of colorful cards are worth this many points: ",
        alternateText = "You end up with this many total scratchcards: ")
public class Day4Test extends AbstractTest<PileOfColorfulCards, Integer> {

    @Override
    protected Function<List<String>, PileOfColorfulCards> constructor() {
        return PileOfColorfulCards::new;
    }
}