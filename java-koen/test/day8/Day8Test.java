package day8;

import common.AbstractTest;
import common.AdventOfCodeTest;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 8,
        example1 = "6",
        example2 = "6",
        input1 = "18827",
        input2 = "0",
        text = "The number of steps required to reach ZZZ are ",
        valueType = Long.class,
        alternateText = "The number of steps it takes before you're only on nodes that end with Z are ")
public class Day8Test extends AbstractTest<LabeledNetwork, Long> {
    @Override
    protected Function<List<String>, LabeledNetwork> constructor() {
        return LabeledNetwork::new;
    }
}
