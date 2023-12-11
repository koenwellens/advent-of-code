package day9;

import common.AbstractTest;
import common.AdventOfCodeTest;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 9,
        example1 = "114",
        example2 = "2",
        input1 = "2174807968",
        input2 = "1208",
        text = "The sum of the extrapolated values is ",
        valueType = Long.class)
public class Day9Test extends AbstractTest<OASISReport, Long> {
    @Override
    protected Function<List<String>, OASISReport> constructor() {
        return OASISReport::new;
    }
}
