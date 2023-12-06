package day6;

import common.AbstractTest;
import common.AdventOfCodeTest;
import day3.EngineSchematic;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 6,
        example1 = "288",
        example2 = "71503",
        input1 = "219849",
        input2 = "29432455",
        text = "The multiplication of all of the number of ways you caould beat the record is ",
        valueType = Long.class,
        alternateText = "The number of ways can you beat the record in this one much longer race is ")
public class Day6Test extends AbstractTest<BoatRace, Long> {

    @Override
    protected Function<List<String>, BoatRace> constructor() {
        return BoatRace::new;
    }
}
