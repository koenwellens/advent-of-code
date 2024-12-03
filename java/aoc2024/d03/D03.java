package aoc2024.d03;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static aoc2024.common.Input.readString;
import static aoc2024.common.Runner.run;

class D03 {

    public static void main(String[] args) {
        run("Sum of multiplications", D03::sumOfMultiplications, "example.txt", 161L);
        run("Sum of multiplications", D03::sumOfMultiplications, "input.txt", 178886550L);
        run("Sum of multiplications", D03::sumOfMultiplicationsWithToggle, "example.txt", 48L);
    }

    private static Long sumOfMultiplications(Path path) {
        String corrupted = readString(path);
        Pattern mulPattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher mulMatcher = mulPattern.matcher(corrupted);
        long sumOfMultiplications = 0;
        while (mulMatcher.find()) {
            int i1 = Integer.parseInt(mulMatcher.group(1));
            int i2 = Integer.parseInt(mulMatcher.group(2));
            sumOfMultiplications += (long) i1 * i2;
        }
        return sumOfMultiplications;
    }

    private static Long sumOfMultiplicationsWithToggle(Path path) {
        Pattern mulPattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        String[] splitMemory = readString(path)
                .split("do\\(\\)|don't()");

        long sumOfMultiplications = 0;
        for (int i = 0; i < splitMemory.length; i++) {
            if (i == 0 || splitMemory[i].startsWith("()")) {
                Matcher mulMatcher = mulPattern.matcher(splitMemory[i]);
                while (mulMatcher.find()) {
                    int i1 = Integer.parseInt(mulMatcher.group(1));
                    int i2 = Integer.parseInt(mulMatcher.group(2));
                    sumOfMultiplications += (long) i1 * i2;
                }
            } else {
                System.out.println("ignored: " + splitMemory[i]);
            }
        }

        return sumOfMultiplications;
    }

}
