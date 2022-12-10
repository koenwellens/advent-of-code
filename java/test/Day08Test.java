import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day08Test {

    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day08.class)
                .forExample().part1().shouldBe(21)
                .forInput().part1().shouldBe(1733)
                .forExample().part2().shouldBe(8)
                .forInput().part2().shouldBe(284648)
                .generateTests();
    }

}
