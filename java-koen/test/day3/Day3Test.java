package day3;

import common.AbstractTest;
import common.AdventOfCodeTest;
import day2.GameRecord;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 3,
        example1 = "4361",
        example2 = "467835",
        input1 = "539433",
        input2 = "75847567",
        text = "The sum of all of the part numbers in the engine schematic is ",
        alternateText = "The sum of all of the gear ratios in your engine schematic is ")
public class Day3Test extends AbstractTest<EngineSchematic, Integer> {

    @Override
    protected Function<List<String>, EngineSchematic> constructor() {
        return EngineSchematic::new;
    }
}