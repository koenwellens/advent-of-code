package aoc2024.d02;

import java.nio.file.Path;

import static aoc2024.common.Input.streamInputLines;
import static aoc2024.common.Runner.run;

class D02 {

    public static void main(String[] args) {
        run("The number of safe reports", D02::countSafeReport, "example.txt", 2L);
        run("The number of safe reports", D02::countSafeReport, "input.txt", 359L);
        run("The number of safe reports with damper", D02::countSafeReportWithDamper, "example.txt", 4L);
        run("The number of safe reports with damper", D02::countSafeReportWithDamper, "input.txt", 418L);
    }

    private static Long countSafeReport(Path path) {
        return streamInputLines(path)
                .map(report -> report.split(" "))
                .filter(levelStrings -> isSafe(levelStrings, false))
                .count();
    }

    private static Long countSafeReportWithDamper(Path path) {
        return streamInputLines(path)
                .map(report -> report.split(" "))
                .filter(levelStrings -> isSafe(levelStrings, true))
                .count();
    }

    private static boolean isSafe(String[] levelStrings, boolean allowDamper) {
        if (isSafe(levelStrings))
            return true;

        if (!allowDamper)
            return false;

        for (int i = 0; i < levelStrings.length; i++) {
            String[] dampedLevelStrings = new String[levelStrings.length - 1];
            int offset = 0;
            for (int li = 0; li < levelStrings.length; li++) {
                if (li == i) {
                    offset = -1;
                } else
                    dampedLevelStrings[li + offset] = levelStrings[li];
            }

            if (isSafe(dampedLevelStrings))
                return true;
        }

        return false;
    }

    private static boolean isSafe(String[] levelStrings) {
        boolean first = true;
        int previousLevel = 0;
        int sign = 0;
        for (String levelString : levelStrings) {
            int level = Integer.parseInt(levelString);
            if (first) {
                previousLevel = level;
                first = false;
            } else {
                int diff = level - previousLevel;
                if (diff == 0) return false;
                if (diff < -3 || diff > 3) return false;
                if (sign == 0) {
                    sign = diff;
                } else {
                    if (sign > 0 && diff < 0) return false;
                    if (sign < 0 && diff > 0) return false;
                }
                previousLevel = level;
            }
        }
        return true;
    }
}
