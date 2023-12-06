package day6;

import common.AbstractObjectBasedOnInput;

import java.util.List;

public final class BoatRace extends AbstractObjectBasedOnInput<Long> {

    public BoatRace(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Long run() {
        final var races = new RaceParser(this.textLines).value();

        return races.stream()
                .mapToLong(Race::waysOfWinning)
                .reduce((l, r) -> l * r)
                .orElseThrow();
    }

    @Override
    public Long alternateRun() {
        final var races = new RaceParser(this.textLines).value();

        return races.stream()
                .mapToLong(Race::waysOfWinning)
                .reduce((l, r) -> l * r)
                .orElseThrow();
    }
}
