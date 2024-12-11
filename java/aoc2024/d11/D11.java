package aoc2024.d11;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static aoc2024.common.Input.readString;
import static aoc2024.common.Runner.run;

public class D11 {

    public static void main(String[] args) {
        run("Number of stones after blinking 25 times", D11::numberOfStonesAfterBlinking25Times,
                "example.txt", 55312L
                , "input.txt", 198075L
        );
        run("Number of stones after blinking 75 times", D11::numberOfStonesAfterBlinking75Times,
                "example.txt", 65601038650482L
                , "input.txt", 235571309320764L
        );
    }

    private static Long numberOfStonesAfterBlinking25Times(Path path) {
        return numberOfStonesAfterBlinkingIterations(path, 25);
    }

    private static Long numberOfStonesAfterBlinking75Times(Path path) {
        return numberOfStonesAfterBlinkingIterations(path, 75);
    }

    private static Long numberOfStonesAfterBlinkingIterations(Path path, int iterations) {
        String[] arrangement = readString(path).split(" ");
        long numberOfStones = 0;
        for (int i = 0; i < arrangement.length; i++) {
            long stone = Long.parseLong(arrangement[i]);
            numberOfStones += calculateNumberOfStones(stone, iterations, new HashMap<>());
        }
        return numberOfStones;
    }

    private static long calculateNumberOfStones(long stone, int blinks, Map<StoneBlink, Long> cache) {
        if (blinks == 0)
            return 1;

        StoneBlink stoneBlink = new StoneBlink(stone, blinks);
        if (cache.containsKey(stoneBlink))
            return cache.get(stoneBlink);

        long numberOfStones;
        if (stone == 0) {
            numberOfStones = calculateNumberOfStones(1, blinks - 1, cache);
        } else {
            int length = (int) (Math.log10(stone) + 1);
            if (length % 2 == 0) {
                long factor = 1L;
                for (int i = 0; i < length / 2; i++) {
                    factor *= 10;
                }
                numberOfStones = calculateNumberOfStones(stone / factor, blinks - 1, cache)
                        + calculateNumberOfStones(stone % factor, blinks - 1, cache);
            } else {
                numberOfStones = calculateNumberOfStones(stone * 2024, blinks - 1, cache);
            }
        }

        cache.put(stoneBlink, numberOfStones);

        return numberOfStones;
    }

    public record StoneBlink(long stone, int remainingBlinks) {
    }
}
