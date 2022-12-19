import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day16Test {
    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day16.class)
                .forExample().part1().shouldBe(1651)
                .forInput().part1().shouldBe(2183)
                .forExample().part2().shouldBe(1707)
//                .forInput().part2().shouldBe(12051287042458L)
                .generateTests();
    }
}
