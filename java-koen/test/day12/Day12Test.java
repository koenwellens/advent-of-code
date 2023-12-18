package day12;

import common.AbstractTest;
import common.AdventOfCodeTest;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 12,
        example1 = "21",
        example2 = "525152",
        input1 = "7118",
        input2 = "0",
        text = "The sum of all the arrangements is ",
        valueType = Long.class)
public class Day12Test extends AbstractTest<HotSprings, Long> {
    @Override
    protected Function<List<String>, HotSprings> constructor() {
        return HotSprings::new;
    }
}
