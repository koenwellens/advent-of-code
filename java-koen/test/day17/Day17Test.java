package day17;

import common.AbstractTest;
import common.AdventOfCodeTest;
import day13.ValleyOfMirrors;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 17,
        example1 = "102",
        example2 = "0",
        input1 = "0",
        input2 = "0",
        text = "The number you get after summarizing all of your notes is ",
        valueType = Long.class)
public class Day17Test extends AbstractTest<CityMap, Long> {
    @Override
    protected Function<List<String>, CityMap> constructor() {
        return CityMap::new;
    }
}
