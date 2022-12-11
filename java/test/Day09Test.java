import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day09Test {

    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day09.class)
                .forExample(1).part1().shouldBe(13L)
                .forInput().part1().shouldBe(6498L)
                .forExample(2).part2().shouldBe(36L)
                .forInput().part2().shouldBe(2531L)
                .generateTests();
    }

}
