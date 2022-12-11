import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day07Test {

    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day07.class)
                .forExample().part1().shouldBe(95437L)
                .forInput().part1().shouldBe(1334506L)
                .forExample().part2().shouldBe(24933642L)
                .forInput().part2().shouldBe(7421137L)
                .generateTests();
    }

}
