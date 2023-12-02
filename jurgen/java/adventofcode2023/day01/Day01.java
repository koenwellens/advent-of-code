package adventofcode2023.day01;

import java.util.function.Function;
import java.util.function.Supplier;

import static adventofcode2023.day01.InputData.*;

class Day01 {

    public static void main(String[] args) {
        System.out.println("Puzzle 1 example: " + sumOfCalibrationValues(Day01::calibrationValue, PART_1_EXAMPLE)); // expected: 142
        System.out.println("Puzzle 1 input: " + sumOfCalibrationValues(Day01::calibrationValue, INPUT)); // expected: 56108
        System.out.println("Puzzle 2 example: " + sumOfCalibrationValues(Day01::improvedCalibrationValue, PART_2_EXAMPLE)); // expected: 281
        System.out.println("Puzzle 2 input: " + sumOfCalibrationValues(Day01::improvedCalibrationValue, INPUT)); // expected: 55652
    }

    private static final boolean DEBUG = false;

    private static final Number[] NUMBERS = new Number[]{
            new Number("one", 1),
            new Number("two", 2),
            new Number("three", 3),
            new Number("four", 4),
            new Number("five", 5),
            new Number("six", 6),
            new Number("seven", 7),
            new Number("eight", 8),
            new Number("nine", 9),
            new Number("1", 1),
            new Number("2", 2),
            new Number("3", 3),
            new Number("4", 4),
            new Number("5", 5),
            new Number("6", 6),
            new Number("7", 7),
            new Number("8", 8),
            new Number("9", 9)};

    private static int sumOfCalibrationValues(Function<String, Integer> lineCalibrationValueParser, String calibrationDocument) {
        int sum = 0;
        for (String s : calibrationDocument.split("\n")) {
            sum += lineCalibrationValueParser.apply(s);
        }
        return sum;
    }

    private static int calibrationValue(String calibrationLine) {
        char firstDigit = 0;
        char lastDigit = 0;
        for (char character : calibrationLine.toCharArray()) {
            if (Character.isDigit(character)) {
                if (firstDigit == 0) {
                    firstDigit = character;
                }
                lastDigit = character;
            }
        }
        int value = (firstDigit - '0') * 10 + (lastDigit - '0');
        debugLog(() -> value + " <- " + calibrationLine);
        return value;
    }

    private static int improvedCalibrationValue(String calibrationLine) {
        int firstDigit = 0;
        int firstDigitIndex = -1;
        int lastDigit = 0;
        int lastDigitIndex = -1;

        for (Number number : NUMBERS) {
            int firstNumberIndex = calibrationLine.indexOf(number.display());
            if (firstNumberIndex != -1) {
                if (firstDigitIndex == -1 || firstNumberIndex < firstDigitIndex) {
                    firstDigitIndex = firstNumberIndex;
                    firstDigit = number.value();
                }
            }

            int lastNumberIndex = calibrationLine.lastIndexOf(number.display());
            if (lastNumberIndex != -1) {
                if (lastDigitIndex == -1 || lastNumberIndex > lastDigitIndex) {
                    lastDigitIndex = lastNumberIndex;
                    lastDigit = number.value();
                }
            }
        }

        int value = firstDigit * 10 + lastDigit;
        debugLog(() -> value + " <- " + calibrationLine);

        return value;
    }

    private static void debugLog(Supplier<String> messageSupplier) {
        if (DEBUG) {
            System.out.println("    | " + messageSupplier.get());
        }
    }

}