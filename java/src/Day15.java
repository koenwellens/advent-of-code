import common.Coordinate;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.max;
import static java.lang.Integer.parseInt;
import static java.util.Comparator.comparingInt;

public class Day15 implements DaySolution<Long> {

    private static final Pattern COORDINATE_PATTERN = Pattern.compile("Sensor at x=(.+?), y=(.+?): closest beacon is at x=(.+?), y=(.+?)");
    private static final BinaryOperator<LinkedList<int[]>> LEAVE_ME_ALONE_DONT_NEED_PARALLEL_STREAM = (l, m) -> l;

    @Override
    public Long part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            List<Sensor> sensors = lines.map(this::parseSensorData).toList();
            int targetRow = sensors.size() == 14 ? 10 : 2000000; // example input contains 14 sensors, real input 33
            int numberOfCoveredCellsInRow = sensors.stream()
                    .flatMap(sensor -> sensor.coveredXCoordinatesRangeInRow(targetRow).stream())
//                    .peek(x -> x.ifPresentOrElse(y -> System.out.println(Arrays.toString(y)), () -> System.out.println("-")))
                    .sorted(comparingInt(range -> range[0]))
//                    .peek(x -> System.out.println(Arrays.toString(x)))
                    .reduce(new LinkedList<>(), this::mergeOverlappingRanges, LEAVE_ME_ALONE_DONT_NEED_PARALLEL_STREAM).stream()
//                    .peek(x -> System.out.println("reduced" + Arrays.toString(x)))
                    .mapToInt(this::rangeLength)
                    .sum();

            int result = (int) (numberOfCoveredCellsInRow - sensors.stream()
                    .flatMap(sensor -> Stream.of(sensor.sensorCoordinate, sensor.closestBeacondCoordinate))
                    .filter(co -> co.y() == targetRow)
                    .distinct()
                    .count());

            System.out.printf("Line %s contains %s positions that cannot hold a beacon%n", targetRow, result);

            return (long) result;
        }
    }

    @Override
    public Long part2(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            List<Sensor> sensors = lines.map(this::parseSensorData).toList();
            int maxCoordinate = sensors.size() == 14 ? 20 : 4000000; // example input contains 14 sensors, real input 33

            LinkedList<int[]> rangesNotCoveringFullRow = IntStream.rangeClosed(0, maxCoordinate)
                    .parallel()
                    .mapToObj(row ->
                            sensors.stream()
                                    .flatMap(sensor -> sensor.coveredXCoordinatesRangeInRow(row).stream().map(coveredRange -> new int[]{coveredRange[0], coveredRange[1], row}))
                                    .filter(rangeAndRowCoordinate -> rangeAndRowCoordinate[0] <= maxCoordinate && rangeAndRowCoordinate[1] >= 0)
                                    .map(rangeAndRowCoordinate -> new int[]{Math.max(rangeAndRowCoordinate[0], 0), Math.min(rangeAndRowCoordinate[1], maxCoordinate), rangeAndRowCoordinate[2]})
                                    .sorted(comparingInt(rangeAndRowCoordinate -> rangeAndRowCoordinate[0]))
                                    .reduce(new LinkedList<>(), this::mergeOverlappingRanges, LEAVE_ME_ALONE_DONT_NEED_PARALLEL_STREAM))
                    .filter(ranges -> ranges.size() > 1)
                    .findAny()
                    .orElseThrow();

            System.out.printf("Row not fully covered: %s%n", rangesNotCoveringFullRow.stream().map(Arrays::toString).collect(Collectors.joining(",")));

            int uncoveredXCoordinate = rangesNotCoveringFullRow.get(0)[1] + 1;
            int uncoveredYCoordinate = rangesNotCoveringFullRow.get(0)[2];

            long tuningFrequency = (long) uncoveredXCoordinate * 4000000 + uncoveredYCoordinate;
            System.out.printf("Only position that is not covered by sensors is %s. The tuning frequency for this distress beacon is %s",
                    new Coordinate(uncoveredXCoordinate, uncoveredYCoordinate),
                    tuningFrequency);

            return tuningFrequency;
        }
    }

    private int rangeLength(int[] range) {
        return range[1] - range[0] + 1;
    }

    @NotNull
    private LinkedList<int[]> mergeOverlappingRanges(LinkedList<int[]> joinedRanges, int[] range) {
        if (joinedRanges.isEmpty())
            joinedRanges.add(range);
        else if (range[0] - joinedRanges.getLast()[1] > 1)
            joinedRanges.add(range);
        else
            joinedRanges.getLast()[1] = Math.max(joinedRanges.getLast()[1], range[1]);
        return joinedRanges;
    }

    private Sensor parseSensorData(String beaconData) {
        Matcher matcher = COORDINATE_PATTERN.matcher(beaconData);
        if (!matcher.matches())
            throw new RuntimeException();

        return new Sensor(
                new Coordinate(
                        parseInt(matcher.group(1)),
                        parseInt(matcher.group(2))),
                new Coordinate(
                        parseInt(matcher.group(3)),
                        parseInt(matcher.group(4))));
    }

    private static class Sensor {
        private final Coordinate sensorCoordinate;
        private final Coordinate closestBeacondCoordinate;
        private final int coveredManhattanDistance;

        public Sensor(Coordinate sensorCoordinate, Coordinate closestBeacondCoordinate) {
            this.sensorCoordinate = sensorCoordinate;
            this.closestBeacondCoordinate = closestBeacondCoordinate;
            this.coveredManhattanDistance = sensorCoordinate.diffVectorTo(closestBeacondCoordinate).manhattanDistance();
        }

        Optional<int[]> coveredXCoordinatesRangeInRow(int rowCoordinate) {
            int distanceToRow = Math.abs(sensorCoordinate.y() - rowCoordinate);

            if (distanceToRow > coveredManhattanDistance)
                return Optional.empty();

            int lateralCoverageInRow = coveredManhattanDistance - distanceToRow;
            return Optional.of(new int[]{
                    sensorCoordinate.x() - lateralCoverageInRow,
                    sensorCoordinate.x() + lateralCoverageInRow,});
        }
    }

}
