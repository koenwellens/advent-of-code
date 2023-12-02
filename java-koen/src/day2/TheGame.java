package day2;

import java.util.Collection;

import static day2.Cube.BLUE;
import static day2.Cube.GREEN;
import static day2.Cube.RED;

public final class TheGame implements Game {

    private final int theId;
    private final Collection<CubeSet> cubeSets;

    public TheGame(final String text) {
        this(new GameId(text).value(), new GameCubeSets(text).value());
    }

    public TheGame(final int id, final Collection<CubeSet> cubeSets) {
        this.theId = id;
        this.cubeSets = cubeSets;
    }

    @Override
    public int id() {
        return theId;
    }

    @Override
    public boolean canBeTakenFrom(final Bag bag) {
        return cubeSets.stream()
                .allMatch(bag::isPossible);
    }

    @Override
    public int minimumNumberOfCubesNeeded(final Cube cube) {
        return cubeSets.stream()
                .mapToInt(cubeSet -> cubeSet.numberOfCubes(cube))
                .reduce(0, Math::max);
    }

    @Override
    public int power() {
        return minimumNumberOfCubesNeeded(RED)
                * minimumNumberOfCubesNeeded(GREEN)
                * minimumNumberOfCubesNeeded(BLUE);
    }
}
