package aoc2024.d03;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static aoc2024.common.Input.readString;
import static aoc2024.common.Runner.run;
import static java.lang.Integer.parseInt;

class D03 {

    public static void main(String[] args) {
        run("Sum of multiplications", D03::sumOfMultiplications, "example1.txt", 161L);
        run("Sum of multiplications", D03::sumOfMultiplications, "input.txt", 178886550L);
        run("Sum of multiplications", D03::sumOfMultiplicationsWithToggle, "example2.txt", 48L);
        run("Sum of multiplications", D03::sumOfMultiplicationsWithToggle, "input.txt", 87163705L);
    }

    private static Long sumOfMultiplications(Path path) {
        String corrupted = readString(path);
        return sumOfMultiplications(corrupted);
    }

    private static long sumOfMultiplications(String corrupted) {
        Pattern mulPattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher mulMatcher = mulPattern.matcher(corrupted);
        long sumOfMultiplications = 0;
        while (mulMatcher.find()) {
            int i1 = parseInt(mulMatcher.group(1));
            int i2 = parseInt(mulMatcher.group(2));
            sumOfMultiplications += (long) i1 * i2;
        }
        return sumOfMultiplications;
    }

    private static Long sumOfMultiplicationsWithToggle(Path path) {
        Pattern togglePattern = Pattern.compile("(do\\(\\)|don't\\(\\))(.*?)");

        // add implicit do() at start
        String corruptedMemoryData = "do()" + readString(path);

        long sumOfMultiplications = 0;
        int fromIndex = 0;
        while (corruptedMemoryData.indexOf("do()", fromIndex) != -1) {
            int start = corruptedMemoryData.indexOf("do()", fromIndex);
            int end = corruptedMemoryData.indexOf("don't()", start);
            if (end == -1) {
                end = corruptedMemoryData.length();
            }

            String enabledMemoryData = corruptedMemoryData.substring(start, end);
            sumOfMultiplications += sumOfMultiplications(enabledMemoryData);

            fromIndex = end;
        }

        return sumOfMultiplications;
    }

}
