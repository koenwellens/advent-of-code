import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.joining;

public class Day10 implements DaySolution<Integer> {

    public static final int SCREEN_LINES = 6;
    public static final int SCREEN_LINE_LENGTH = 40;

    @Override
    public Integer part1(Path inputFilePath) {
        AtomicReference<SystemState> systemStateRef = new AtomicReference<>(new SystemState(0, 1, false));
        try (Stream<String> lines = lines(inputFilePath)) {
            int result = lines.flatMap(this::parseOperation)
                    .map(op -> systemStateRef.updateAndGet(op::apply))
                    .filter(systemState -> systemState.cycleRunning)
                    .filter(systemState -> systemState.cycle <= 220 && (systemState.cycle - 20) % SCREEN_LINE_LENGTH == 0)
                    .mapToInt(systemState -> systemState.cycle * systemState.x)
                    .sum();

            System.out.println("Sum of selected signal strengths is " + result);

            return result;
        }
    }

    public String part2String(Path inputFilePath) {
        AtomicReference<SystemState> systemStateRef = new AtomicReference<>(new SystemState(0, 1, false));
        String[][] screen = new String[SCREEN_LINES][SCREEN_LINE_LENGTH];
        try (Stream<String> lines = lines(inputFilePath)) {
            lines.flatMap(this::parseOperation)
                    .map(op -> systemStateRef.updateAndGet(op::apply))
                    .filter(systemState -> systemState.cycleRunning)
                    .forEach(systemState -> {
                        int zeroBasedCycle = systemState.cycle() - 1;
                        int crtLine = zeroBasedCycle / 40;
                        int crtXPosition = zeroBasedCycle % 40;
                        screen[crtLine][crtXPosition] = Math.abs(crtXPosition - systemState.x) < 2 ? "#" : ".";
                    });

            System.out.println("The screen contents is ");
            String screenRender = render(screen);
            System.out.println(screenRender);

            return screenRender;
        }
    }

    String render(String[][] screenPixels) {
        return Arrays.stream(screenPixels)
                .map(linePixels -> Arrays.stream(linePixels).map(p -> p == null ? " " : p).collect(joining()))
                .collect(joining("\n"));
    }

    private Stream<Function<SystemState, SystemState>> parseOperation(String s) {
        if (s.startsWith("addx")) {
            return Stream.of(
                    SystemState::tick,
                    SystemState::tick,
                    systemState -> systemState.addx(parseInt(s.substring(5))));
        }
        if (s.startsWith("noop")) {
            return Stream.of(
                    SystemState::tick,
                    SystemState::noop);
        }
        throw new IllegalArgumentException(s);
    }

    record SystemState(int cycle, int x, boolean cycleRunning) {
        SystemState tick() {
            return new SystemState(cycle + 1, x, true);
        }

        SystemState noop() {
            return new SystemState(cycle, x, false);
        }

        SystemState addx(int value) {
            return new SystemState(cycle, x + value, false);
        }
    }
}
