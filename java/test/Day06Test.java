import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day06Test {

    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day06.class)
                .forExample(1).part1().shouldBe(7)
                .forExample(2).part1().shouldBe(5)
                .forExample(3).part1().shouldBe(6)
                .forExample(4).part1().shouldBe(10)
                .forExample(5).part1().shouldBe(11)
                .forInput().part1().shouldBe(1757).forExample(1).part1().shouldBe(7)
                .forExample(1).part2().shouldBe(19)
                .forExample(2).part2().shouldBe(23)
                .forExample(3).part2().shouldBe(23)
                .forExample(4).part2().shouldBe(29)
                .forExample(5).part2().shouldBe(26)
                .forInput().part2().shouldBe(2950)
                .generateTests();
    }

}