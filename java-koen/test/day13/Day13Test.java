package day13;

import common.AbstractTest;
import common.AdventOfCodeTest;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 13,
        example1 = "405",
        example2 = "0",
        input1 = "0",
        input2 = "0",
        text = "The number you get after summarizing all of your notes is ",
        valueType = Long.class)
public class Day13Test extends AbstractTest<ValleyOfMirrors, Long> {
    @Override
    protected Function<List<String>, ValleyOfMirrors> constructor() {
        return ValleyOfMirrors::new;
    }
}
