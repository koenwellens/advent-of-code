package day2;

import java.util.Collection;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toUnmodifiableList;

public final class GameCubeSets extends AbstractTextParser<Collection<CubeSet>> {

    public GameCubeSets(final String text) {
        super(text);
    }

    public Collection<CubeSet> value() {
        final var allSubsets = this.text.split(": ")[1];
        return stream(allSubsets.split("; ")).map(TheCubeSet::new).collect(toUnmodifiableList());
    }
}
