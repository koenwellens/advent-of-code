import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day12Test {

    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day12.class)
                .forExample().part1().shouldBe(31)
                .forInput().part1().shouldBe(-1)
//                .forExample().part2().shouldBe(-1)
//                .forInput().part2().shouldBe(-1)
                .generateTests();
    }

}
