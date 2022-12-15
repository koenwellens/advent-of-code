import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day14Test {
    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day14.class)
                .forExample().part1().shouldBe(24)
                .forInput().part1().shouldBe(1061)
                .forExample().part2().shouldBe(93)
                .forInput().part2().shouldBe(25055)
                .generateTests();
    }
}
