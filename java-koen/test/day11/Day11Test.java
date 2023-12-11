package day11;

import common.AbstractTest;
import common.AdventOfCodeTest;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 11,
        example1 = "374",
        example2 = "82000210",
        input1 = "9742154",
        input2 = "411142919886",
        text = "The sum of the lengths of the shortest paths between all universes is ",
        valueType = Long.class)
public class Day11Test extends AbstractTest<Observatory, Long> {
    @Override
    protected Function<List<String>, Observatory> constructor() {
        return Observatory::new;
    }
}
