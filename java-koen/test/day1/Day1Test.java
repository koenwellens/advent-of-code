package day1;

import common.AbstractTest;
import common.AdventOfCodeTest;

import java.util.List;
import java.util.function.Function;

@AdventOfCodeTest(
        day = 1,
        example1 = "142",
        example2 = "281",
        input1 = "55816",
        input2 = "54980",
        text = "The sum of calibration values is ")
public class Day1Test extends AbstractTest<CalibrationDocument, Integer> {

    @Override
    protected Function<List<String>, CalibrationDocument> constructor() {
        return CalibrationDocument::new;
    }
}