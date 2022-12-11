import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day02Test {
    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day02.class)
                .forExample().part1().shouldBe(15)
                .forInput().part1().shouldBe(12586)
                .forExample().part2().shouldBe(12)
                .forInput().part2().shouldBe(13193)
                .generateTests();
    }
}
