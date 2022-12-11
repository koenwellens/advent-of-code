import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day03Test {

    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day03.class)
                .forExample().part1().shouldBe(157)
                .forInput().part1().shouldBe(8243)
                .forExample().part2().shouldBe(70)
                .forInput().part2().shouldBe(2631)
                .generateTests();
    }
}
