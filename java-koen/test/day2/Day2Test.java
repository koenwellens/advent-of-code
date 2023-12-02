package day2;

import common.AbstractTest;
import common.AdventOfCodeTest;
import day1.CalibrationDocument;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 2,
        example1 = "8",
        example2 = "2286",
        input1 = "2720",
        input2 = "71535",
        text = "The sum of the IDs of those games is ",
        alternateText = "The sum of the power of these sets is ")
public class Day2Test extends AbstractTest<GameRecord, Integer> {

    @Override
    protected Function<List<String>, GameRecord> constructor() {
        return GameRecord::new;
    }
}