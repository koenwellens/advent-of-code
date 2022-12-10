import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day05Test {

    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day05.class)
                .forExample().part1().shouldBe("CMZ")
                .forInput().part1().shouldBe("RLFNRTNFB")
                .forExample().part2().shouldBe("MCD")
                .forInput().part2().shouldBe("MHQTLJRLB")
                .generateTests();
    }

}
