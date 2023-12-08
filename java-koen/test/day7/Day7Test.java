package day7;

import common.AbstractTest;
import common.AdventOfCodeTest;
import day6.BoatRace;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 7,
        example1 = "6440",
        example2 = "5905",
        input1 = "247961593",
        input2 = "248750699",
        text = "The total winnings are ",
        valueType = Long.class,
        alternateText = "Using the new joker rule, the new total winnings are ")
public class Day7Test extends AbstractTest<CamelCardGame, Long> {

    @Override
    protected Function<List<String>, CamelCardGame> constructor() {
        return CamelCardGame::new;
    }
}
