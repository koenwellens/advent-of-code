import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day11Test {

    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day11.class)
                .forExample().part1().shouldBe(10605L)
                .forInput().part1().shouldBe(55944L)
                .forExample().part2().shouldBe(2713310158L)
                .forInput().part2().shouldBe(15117269860L)
                .generateTests();
    }

}
