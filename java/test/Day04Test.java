import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day04Test {

    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day04.class)
                .forExample().part1().shouldBe(2)
                .forInput().part1().shouldBe(503)
                .forExample().part2().shouldBe(4)
                .forInput().part2().shouldBe(827)
                .generateTests();
    }
}
