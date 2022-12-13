import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day13Test {
    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day13.class)
                .forExample().part1().shouldBe(13)
                .forInput().part1().shouldBe(5717)
                .forExample().part2().shouldBe(140)
                .forInput().part2().shouldBe(25935)
                .generateTests();
    }
}
