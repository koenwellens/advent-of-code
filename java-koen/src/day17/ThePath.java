package day17;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static day17.Direction.D;
import static day17.Direction.L;
import static day17.Direction.R;
import static day17.Direction.U;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public final class ThePath implements Path {

    private static final Map<Direction, Direction> OPPOSITES = Map.of(
            L, R,
            R, L,
            U, D,
            D, U
    );

    private final List<Direction> directions;
    private final List<CityBlock> cityBlocks;
    private final long totalHeatLoss;

    public ThePath(final CityBlock firstBlock) {
        this(singletonList(firstBlock), emptyList());
    }

    private ThePath(final List<CityBlock> startingBlocks, final List<Direction> startingDirections) {
        this.cityBlocks = new ArrayList<>(startingBlocks);
        this.directions = new ArrayList<>(startingDirections);
        this.totalHeatLoss = computeTotalHeatLoss();
    }

    private long computeTotalHeatLoss() {
        final var numberOfTimesFound = new HashMap<String, Integer>();
        for (int i = 0; i < cityBlocks.size(); i++) {
            final var cityBlock = cityBlocks.get(i).toString();
            if (i == 0) {
                numberOfTimesFound.put(cityBlock, 0);
            } else {
                final var number = numberOfTimesFound.getOrDefault(cityBlock, 0);
                numberOfTimesFound.put(cityBlock, number + 1);
            }
        }

        return cityBlocks.stream()
                .distinct()
                .collect(toMap(CityBlock::toString, CityBlock::heatLoss))
                .entrySet()
                .stream()
                .mapToLong(entry -> range(1, numberOfTimesFound.get(entry.getKey()) + 1)
                        .map(i -> i * entry.getValue())
                        .mapToLong(i -> (long) i)
                        .sum())
                .sum();
    }

    @Override
    public boolean isComplete(final String endCoordinates) {
        return this.cityBlocks.getLast().toString().equals(endCoordinates);
    }

    @Override
    public Long totalHeatLoss() {
        return this.totalHeatLoss;
    }

    @Override
    public Long numberOfDirectionsTaken() {
        return (long) directions.size();
    }

    @Override
    public int compareTo(final Path o) {
        return totalHeatLoss().compareTo(o.totalHeatLoss());
    }

    @Override
    public boolean canGoTo(final Direction direction) {
        final var numberOfDirections = this.directions.size();
        if (numberOfDirections == 0) {
            return true;
        }

        if (OPPOSITES.get(this.directions.getLast()) == direction) {
            return false;
        }

        if (numberOfDirections < 3) {
            return true;
        }

        if (willCauseLoop(direction)) {
            return false;
        }

        return this.directions.get(numberOfDirections - 3) != direction
                || this.directions.get(numberOfDirections - 2) != direction
                || this.directions.get(numberOfDirections - 1) != direction;
    }

    private boolean willCauseLoop(final Direction direction) {
        return range(0, this.cityBlocks.size() - 1)
                .filter(i -> cityBlocks.get(i).equals(cityBlocks.getLast()))
                .anyMatch(index -> directions.get(index) == direction);
    }

    @Override
    public Collection<Path> continueToAllPossibleDirections(final Map<String, CityBlock> cityBlocks) {
        return this.cityBlocks.getLast().neighbours()
                .entrySet()
                .stream().filter(entry -> canGoTo(entry.getKey()))
                .map(entry -> add(entry.getKey(), cityBlocks.get(entry.getValue())))
                .toList();
    }

    public Path add(final Direction direction, final CityBlock cityBlock) {
        return new ThePath(
                addAndReturn(this.cityBlocks, cityBlock),
                addAndReturn(this.directions, direction)
        );
    }

    private <T> List<T> addAndReturn(final List<T> list, final T element) {
        final var result = new ArrayList<>(list);
        result.add(element);
        return result;
    }

    @Override
    public String toString() {
        return cityBlocks.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ThePath thePath = (ThePath) o;

        if (totalHeatLoss != thePath.totalHeatLoss) return false;
        return Objects.equals(directions, thePath.directions);
    }

    @Override
    public int hashCode() {
        int result = directions != null ? directions.hashCode() : 0;
        result = 31 * result + (int) (totalHeatLoss ^ (totalHeatLoss >>> 32));
        return result;
    }

    @Override
    public Direction lastDirection() {
        return directions.isEmpty() ? null : directions.getLast();
    }
}
