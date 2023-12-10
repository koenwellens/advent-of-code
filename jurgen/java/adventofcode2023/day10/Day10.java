package adventofcode2023.day10;

import adventofcode2023.common.Input;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day10.Day10.Direction.*;
import static adventofcode2023.day10.Day10Input.*;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

class Day10 {
    public static void main(String[] args) {
        runnerFor(Day10.class)
                .execute("Puzzle 1", Day10::longestDistanceFromLoopStartNode).withInput(SQUARE_LOOP_EXAMPLE).expect(4)
                .execute("Puzzle 1", Day10::longestDistanceFromLoopStartNode).withInput(COMPLEX_LOOP_EXAMPLE).expect(8)
                .execute("Puzzle 1", Day10::longestDistanceFromLoopStartNode).withInput(INPUT).expect(7107)
                .execute("Puzzle 2", Day10::numberOfTilesEnclosedByLoop).withInput(ENCLOSURE_SIMPLE_EXAMPLE_1).expect(4)
                .execute("Puzzle 2", Day10::numberOfTilesEnclosedByLoop).withInput(ENCLOSURE_SIMPLE_EXAMPLE_2).expect(4)
                .execute("Puzzle 2", Day10::numberOfTilesEnclosedByLoop).withInput(ENCLOSURE_LARGER_EXAMPLE).expect(8)
                .execute("Puzzle 2", Day10::numberOfTilesEnclosedByLoop).withInput(INPUT).expect(281)
                .build()
                .run();
    }

    private static int longestDistanceFromLoopStartNode(Input pipesInput) {
        PipeMapInfo pipeMapInfo = parsePipeMapInfo(pipesInput);

        AtomicInteger distance = new AtomicInteger();
        Map<Pipe, Integer> pipeDistancesToStart = new HashMap<>();

        pipeDistancesToStart.put(pipeMapInfo.getLoopStartNodePipe(), distance.get());
        Pipe[] currentPipes = new Pipe[]{pipeMapInfo.getLoopStartNodePipe()};

        while (currentPipes.length != 0) {
            distance.incrementAndGet();
            currentPipes = Arrays.stream(currentPipes)
                    .flatMap(pipe -> Arrays.stream(pipe.symbol.directions()).map(dir -> pipeMapInfo.getPipeAt(dir.applyTo(pipe.coordinate))))
                    .filter(Objects::nonNull)
                    .filter(pipe -> !pipeDistancesToStart.containsKey(pipe))
                    .peek(pipe -> pipeDistancesToStart.put(pipe, distance.get()))
                    .toArray(Pipe[]::new);
        }

        return pipeDistancesToStart.values().stream().mapToInt(i -> i).max().orElseThrow();
    }

    private static int numberOfTilesEnclosedByLoop(Input pipesInput) {
        PipeMapInfo pipeMapInfo = parsePipeMapInfo(pipesInput);

        // walk the entire grid one horizontal line at a time
        // each line starts out as being "outside" the loop
        // when passing a pipe use pipe details to decide when we are entering the inside of the loop and when we are outside
        // when encountering a non-pipe coordinate, count it as being inside when the state says it's inside
        int enclosedCount = 0;
        for (int y = pipeMapInfo.minY; y < pipeMapInfo.maxY; y++) {
            boolean inside = false;
            String triggeringSymbol = null;
            for (int x = pipeMapInfo.minX; x <= pipeMapInfo.maxX; x++) {
                Coordinate coordinate = new Coordinate(x, y);
                Pipe pipeAtCoordinate = pipeMapInfo.getPipeAt(coordinate);
                if (pipeAtCoordinate == null) {
                    if (inside)
                        enclosedCount++;
                } else {
                    // horizontal pipes "-" can be ignored
                    switch (pipeAtCoordinate.symbol.string) {
                        case "|" -> inside = !inside;
                        // F,L are followed by horizontal pipes and eventually a 7 or J
                        // the final one (7,J) decides what happens further on, depending on the state before the sequence (inside) and the sequence triggering symbol
                        // so remember the state before (inside) and the symbol starting the encountered pipe sequence
                        case "F", "L" -> triggeringSymbol = pipeAtCoordinate.symbol.string;
                        // F---7 -> insideState after 7 is same as before F
                        // L---7 -> insideState after 7 is opposite of that before L
                        case "7" -> {
                            if ("F".equals(triggeringSymbol)) {
                                triggeringSymbol = null;
                            } else if ("L".equals(triggeringSymbol)) {
                                triggeringSymbol = null;
                                inside = !inside;
                            } else
                                throw new IllegalStateException(triggeringSymbol);
                        }
                        // F---J -> insideState after J is opposite of that before F
                        // L---J -> insideState after J is same as before L
                        case "J" -> {
                            if ("F".equals(triggeringSymbol)) {
                                triggeringSymbol = null;
                                inside = !inside;
                            } else if ("L".equals(triggeringSymbol)) {
                                triggeringSymbol = null;
                            } else
                                throw new IllegalStateException(triggeringSymbol);
                        }
                    }
                }
            }
        }

        return enclosedCount;
    }

    @NotNull
    private static PipeMapInfo parsePipeMapInfo(Input pipesInput) {
        AtomicInteger yCoordinateGenerator = new AtomicInteger();
        AtomicReference<Pipe> startNode = new AtomicReference<>();
        Map<Coordinate, Pipe> pipes = pipesInput
                .streamLinesFlatMapped(lineOfPipes -> parsePipes(lineOfPipes, yCoordinateGenerator.getAndIncrement()))
                .peek(pipe -> {
                    if (pipe.symbol == null) {
                        if (startNode.get() != null)
                            throw new IllegalStateException("");
                        startNode.set(pipe);
                    }
                })
                .collect(toMap(
                        pipe -> pipe.coordinate,
                        pipe -> pipe));

        // replace start node
        Pipe startNodePipe = startNode.get();
        Set<Direction> directions = Arrays.stream(values())
                .filter(direction -> Optional.ofNullable(pipes.get(direction.applyTo(startNodePipe.coordinate))).map(pipe -> pipe.pointsTo(startNodePipe.coordinate)).orElse(false))
                .collect(toSet());
        PipeSymbol startNodePipeSymbol = pipeSymbolMap.values()
                .stream()
                .filter(pipeSymbol -> directions.equals(Set.of(pipeSymbol.direction1, pipeSymbol.direction2)))
                .findFirst()
                .orElseThrow();
        Pipe actualStartNodePipe = new Pipe(startNodePipeSymbol, startNodePipe.coordinate);
        pipes.put(actualStartNodePipe.coordinate, actualStartNodePipe);

        // determine loop pipes
        Set<Pipe> loopPipes = new HashSet<>();
        loopPipes.add(actualStartNodePipe);
        AtomicReference<Pipe> previousPipe = new AtomicReference<>();
        AtomicReference<Pipe> currentPipe = new AtomicReference<>(actualStartNodePipe);
        do {
            Pipe nextPipe;
            if (previousPipe.get() == null) {
                Coordinate nextPipeCoordinate = currentPipe.get().symbol.direction1.applyTo(currentPipe.get().coordinate);
                nextPipe = pipes.get(nextPipeCoordinate);
            } else {
                List<Pipe> list = Arrays.stream(currentPipe.get().symbol.directions())
                        .map(dir -> dir.applyTo(currentPipe.get().coordinate))
                        .filter(coordinate -> !previousPipe.get().coordinate.equals(coordinate))
                        .map(pipes::get)
                        .toList();

                if (list.size() != 1)
                    throw new IllegalStateException();

                nextPipe = list.getFirst();
            }

            loopPipes.add(nextPipe);

            previousPipe.set(currentPipe.get());
            currentPipe.set(nextPipe);
        } while (!currentPipe.get().equals(actualStartNodePipe));

        return new PipeMapInfo(loopPipes, actualStartNodePipe.coordinate);
    }

    private static Stream<Pipe> parsePipes(String lineOfPipes, int yCoordinate) {
        AtomicInteger xCoordinateGenerator = new AtomicInteger(-1);
        return Arrays.stream(lineOfPipes.split(""))
                .peek(pipeString -> xCoordinateGenerator.getAndIncrement())
                .filter(pipeString -> !pipeString.equals(".")) // discard ground
                .map(pipeString ->
                        new Pipe(
                                pipeSymbolMap.get(pipeString), // could be null for "S" node
                                new Coordinate(
                                        xCoordinateGenerator.get(),
                                        yCoordinate)));
    }

    record Pipe(PipeSymbol symbol, Coordinate coordinate) {

        boolean pointsTo(Coordinate coordinate) {
            return Set.of(
                            symbol.direction1.applyTo(this.coordinate),
                            symbol.direction2.applyTo(this.coordinate))
                    .contains(coordinate);
        }
    }

    record Coordinate(int x, int y) {
    }

    enum Direction {

        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);
        final int deltaX;
        final int deltaY;

        Direction(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

        Coordinate applyTo(Coordinate coordinate) {
            return new Coordinate(coordinate.x + deltaX, coordinate.y + deltaY);
        }
    }

    record PipeSymbol(String string, Direction direction1, Direction direction2) {
        Direction[] directions() {
            return new Direction[]{direction1, direction2};
        }
    }

    private static final Map<String, PipeSymbol> pipeSymbolMap = Stream.of(
                    new PipeSymbol("|", UP, DOWN),
                    new PipeSymbol("-", LEFT, RIGHT),
                    new PipeSymbol("L", UP, RIGHT),
                    new PipeSymbol("J", UP, LEFT),
                    new PipeSymbol("F", DOWN, RIGHT),
                    new PipeSymbol("7", LEFT, DOWN))
            .collect(Collectors.toMap(
                    PipeSymbol::string,
                    Function.identity()));

    static final class PipeMapInfo {
        private final Map<Coordinate, Pipe> pipes;
        private final Coordinate loopStartNode;
        private final int minX, maxX, minY, maxY;

        PipeMapInfo(Set<Pipe> pipes, Coordinate loopStartNode) {
            this.pipes = pipes.stream()
                    .collect(toMap(Pipe::coordinate, Function.identity()));
            this.loopStartNode = loopStartNode;
            AtomicInteger minX = new AtomicInteger(loopStartNode.x);
            AtomicInteger maxX = new AtomicInteger(loopStartNode.x);
            AtomicInteger minY = new AtomicInteger(loopStartNode.y);
            AtomicInteger maxY = new AtomicInteger(loopStartNode.y);
            this.pipes.keySet()
                    .forEach(pipe -> {
                        minX.getAndAccumulate(pipe.x(), Math::min);
                        maxX.getAndAccumulate(pipe.x(), Math::max);
                        minY.getAndAccumulate(pipe.y(), Math::min);
                        maxY.getAndAccumulate(pipe.y(), Math::max);
                    });
            this.minX = minX.get();
            this.maxX = maxX.get();
            this.minY = minY.get();
            this.maxY = maxY.get();
        }

        Pipe getLoopStartNodePipe() {
            return pipes.get(loopStartNode);
        }

        Pipe getPipeAt(Coordinate coordinate) {
            return pipes.get(coordinate);
        }

    }
}
