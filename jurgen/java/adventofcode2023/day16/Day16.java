package adventofcode2023.day16;

import adventofcode2023.common.CollectionUtils;
import adventofcode2023.common.Input;
import adventofcode2023.common.model.Coordinate;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day16.Day16.Direction.*;
import static adventofcode2023.day16.Day16Input.EXAMPLE;
import static adventofcode2023.day16.Day16Input.INPUT;

class Day16 {
    public static void main(String[] args) {
        runnerFor(Day16.class)
                .execute("Puzzle 1", Day16::countEnergizedTiles).withInput(EXAMPLE).expect(46)
                .execute("Puzzle 1", Day16::countEnergizedTiles).withInput(INPUT).expect(7046)
                .execute("Puzzle 2", Day16::maxNumberOfEnergizedTiles).withInput(EXAMPLE).expect(51)
                .execute("Puzzle 2", Day16::maxNumberOfEnergizedTiles).withInput(INPUT).expect(7313)
                .build()
                .run();
    }

    private static int countEnergizedTiles(Input contraptionData) {
        Contraption contraption = parseContraption(contraptionData);
        return countEnergizedTiles(contraption, new Beam(RIGHT, new Coordinate(0, 0)));
    }

    private static int maxNumberOfEnergizedTiles(Input contraptionData) {
        Contraption contraption = parseContraption(contraptionData);
        return CollectionUtils.streamOfAll(
                        IntStream.range(0, contraption.getxSize()).mapToObj(x -> new Coordinate(x, 0)).map(co -> () -> countEnergizedTiles(contraption, new Beam(DOWN, co))),
                        IntStream.range(0, contraption.getxSize()).mapToObj(x -> new Coordinate(x, contraption.getySize() - 1)).map(co -> () -> countEnergizedTiles(contraption, new Beam(UP, co))),
                        IntStream.range(0, contraption.getySize()).mapToObj(y -> new Coordinate(0, y)).map(co -> () -> countEnergizedTiles(contraption, new Beam(RIGHT, co))),
                        IntStream.range(0, contraption.getySize()).mapToObj(y -> new Coordinate(contraption.getxSize() - 1, y)).map(co -> (Supplier<Integer>) () -> countEnergizedTiles(contraption, new Beam(LEFT, co))))
                .parallel()
                .mapToInt(Supplier::get)
                .max()
                .orElseThrow();
    }

    @NotNull
    private static Contraption parseContraption(Input contraptionData) {
        String[] contraptionDataLines = contraptionData.lines();
        Map<Coordinate, String> tiles = new HashMap<>();
        for (int row = 0; row < contraptionDataLines.length; row++) {
            String[] rowTiles = contraptionDataLines[row].split("");
            for (int column = 0; column < rowTiles.length; column++) {
                Coordinate tileCoordinate = new Coordinate(column, row);
                tiles.put(tileCoordinate, rowTiles[column]);
            }
        }
        return new Contraption(tiles);
    }

    private static int countEnergizedTiles(Contraption contraption, Beam startBeam) {
        Map<Coordinate, List<Direction>> visitedTiles = new HashMap<>();

        Queue<Beam> beams = new ArrayDeque<>();
        beams.add(startBeam);

        while (!beams.isEmpty()) {
            Beam beam = beams.poll();
//            System.out.println(beam);

            // if tile was already visited by a beam from the same direction as the current one results will be the same
            // so no need to calculate again
            if (!visitedTiles.containsKey(beam.coordinate)
                    || !visitedTiles.get(beam.coordinate).contains(beam.direction)) {
                visitedTiles.computeIfAbsent(beam.coordinate, x -> new ArrayList<>()).add(beam.direction);

                String tileItem = contraption.getTile(beam.coordinate);
                if (tileItem.equals(".")
                        || (tileItem.equals("-") && HORIZONTAL.contains(beam.direction))
                        || (tileItem.equals("|") && VERTICAL.contains(beam.direction))) {
                    Coordinate nextCoordinate = applyDirectionToCoordinate(beam.direction, beam.coordinate);
                    if (contraption.isValidCoordinate(nextCoordinate))
                        beams.add(new Beam(beam.direction, nextCoordinate));
                } else if (tileItem.equals("|") || tileItem.equals("-")) {
                    Set<Direction> nextDirections = HORIZONTAL.contains(beam.direction)
                            ? VERTICAL
                            : HORIZONTAL;
                    for (Direction dir : nextDirections) {
                        Coordinate nextCoordinate = applyDirectionToCoordinate(dir, beam.coordinate);
                        if (contraption.isValidCoordinate(nextCoordinate)) {
                            beams.add(new Beam(dir, nextCoordinate));
                        }
                    }
                } else {
                    Map<String, Map<Direction, Direction>> mirrorReflections = Map.of(
                            "/", Map.of(
                                    DOWN, LEFT,
                                    LEFT, DOWN,
                                    UP, RIGHT,
                                    RIGHT, UP),
                            "\\", Map.of(
                                    DOWN, RIGHT,
                                    LEFT, UP,
                                    UP, LEFT,
                                    RIGHT, DOWN));
                    Direction nextDirection = mirrorReflections
                            .get(tileItem)
                            .get(beam.direction);
                    Coordinate nextCoordinate = applyDirectionToCoordinate(nextDirection, beam.coordinate);
                    if (contraption.isValidCoordinate(nextCoordinate)) {
                        beams.add(new Beam(nextDirection, nextCoordinate));
                    }
                }
            }
        }

//        for (int y = 0; y < contraptionDataLines.length; y++) {
//            for (int x = 0; x < contraptionDataLines[0].length(); x++) {
//                if(visitedTiles.containsKey(new Coordinate(x,y)))
//                    System.out.print("#");
//                else
//                    System.out.print(".");
//            }
//            System.out.println();
//        }

        return visitedTiles.size();
    }

    private static class Contraption {

        private final Map<Coordinate, String> tiles;
        private final Predicate<Coordinate> validCoordinatePredicate;
        private final int xSize;
        private final int ySize;

        Contraption(Map<Coordinate, String> tiles) {
            this.tiles = Map.copyOf(tiles);
            int maxX = 0;
            int maxY = 0;
            for (Coordinate coordinate : tiles.keySet()) {
                maxX = Math.max(maxX, coordinate.x());
                maxY = Math.max(maxY, coordinate.y());
            }
            this.xSize = maxX + 1;
            this.ySize = maxY + 1;
            this.validCoordinatePredicate = coordinate ->
                    coordinate.x() >= 0
                            && coordinate.x() < xSize
                            && coordinate.y() >= 0
                            && coordinate.y() < ySize;
        }

        String getTile(Coordinate coordinate) {
            return tiles.get(coordinate);
        }

        boolean isValidCoordinate(Coordinate coordinate) {
            return validCoordinatePredicate.test(coordinate);
        }

        int getxSize() {
            return xSize;
        }

        int getySize() {
            return ySize;
        }
    }

    private static Coordinate applyDirectionToCoordinate(Direction direction, Coordinate coordinate) {
        return switch (direction) {
            case UP -> new Coordinate(coordinate.x(), coordinate.y() - 1);
            case DOWN -> new Coordinate(coordinate.x(), coordinate.y() + 1);
            case LEFT -> new Coordinate(coordinate.x() - 1, coordinate.y());
            case RIGHT -> new Coordinate(coordinate.x() + 1, coordinate.y());
        };
    }

    private record Beam(Direction direction, Coordinate coordinate) {
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;

        static final Set<Direction> HORIZONTAL = Set.of(LEFT, RIGHT);
        static final Set<Direction> VERTICAL = Set.of(UP, DOWN);
    }
}
