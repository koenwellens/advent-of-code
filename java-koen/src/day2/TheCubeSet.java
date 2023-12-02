package day2;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static day2.Cube.BLUE;
import static day2.Cube.GREEN;
import static day2.Cube.RED;

public final class TheCubeSet implements CubeSet {

    private final Map<Cube, Integer> cubes;

    public TheCubeSet(final String text) {
        this(new NumberOfRedCubes(text).value(),
                new NumberOfGreenCubes(text).value(),
                new NumberOfBlueCubes(text).value());
    }

    public TheCubeSet(final int red, final int green, final int blue) {
        this(Map.of(RED, red, GREEN, green, BLUE, blue));
    }

    private TheCubeSet(final Map<Cube, Integer> cubes) {
        this.cubes = cubes;
    }

    @Override
    public int numberOfCubes(final Cube cube) {
        return cubes.getOrDefault(cube, 0);
    }
}
