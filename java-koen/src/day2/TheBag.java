package day2;

import java.util.Arrays;
import java.util.Map;

import static day2.Cube.BLUE;
import static day2.Cube.GREEN;
import static day2.Cube.RED;
import static java.util.Arrays.stream;

public final class TheBag implements Bag {

    private final Map<Cube, Integer> cubes;

    public TheBag(final int red, final int green, final int blue) {
        this(Map.of(RED, red, GREEN, green, BLUE, blue));
    }

    private TheBag(final Map<Cube, Integer> cubes) {
        this.cubes = cubes;
    }

    @Override
    public boolean isPossible(final Game game) {
        return game.canBeTakenFrom(this);
    }

    @Override
    public boolean isPossible(final CubeSet cubeSet) {
        return stream(Cube.values())
                .allMatch(cube -> cubes.getOrDefault(cube, 0) >= cubeSet.numberOfCubes(cube));
    }
}
