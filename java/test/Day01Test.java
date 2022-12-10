import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day01Test {

    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day01.class)
                .forExample().part1().shouldBe(24000)
                .forInput().part1().shouldBe(70720)
                .forExample().part2().shouldBe(45000)
                .forInput().part2().shouldBe(207148)
                .generateTests();
    }
}
