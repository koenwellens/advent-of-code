import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day15Test {
    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day15.class)
                .forExample().part1().shouldBe(26L)
                .forInput().part1().shouldBe(4725496L)
                .forExample().part2().shouldBe(56000011L)
                .forInput().part2().shouldBe(12051287042458L)
                .generateTests();
    }
}
