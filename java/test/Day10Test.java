import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class Day10Test {

    @TestFactory
    Stream<DynamicTest> standardTests() {
        return TestGenerator
                .generateTestsForClass(Day10.class)
                .forExample().part1().shouldBe(13140)
                .forInput().part1().shouldBe(11220)
                .forExample().part2(Day10::part2String).shouldBe("""
                        ##..##..##..##..##..##..##..##..##..##..
                        ###...###...###...###...###...###...###.
                        ####....####....####....####....####....
                        #####.....#####.....#####.....#####.....
                        ######......######......######......####
                        #######.......#######.......#######.....""")
                .forInput().part2(Day10::part2String).shouldBe("""
                        ###..####.###...##....##.####.#....#..#.
                        #..#....#.#..#.#..#....#.#....#....#.#..
                        ###....#..#..#.#..#....#.###..#....##...
                        #..#..#...###..####....#.#....#....#.#..
                        #..#.#....#....#..#.#..#.#....#....#.#..
                        ###..####.#....#..#..##..####.####.#..#.""") // spelling letters BZPAJELK
                .generateTests();
    }

}
