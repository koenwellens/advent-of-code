package day2;

import common.AbstractObjectBasedOnInput;

import java.util.List;

public final class GameRecord extends AbstractObjectBasedOnInput<Integer> {

    public GameRecord(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Integer run() {
        final var bag = new TheBag(12, 13, 14);

        return this.textLines.stream()
                .map(TheGame::new).toList()
                .stream()
                .filter(bag::isPossible)
                .mapToInt(Game::id)
                .sum();
    }

    @Override
    public Integer alternateRun() {
        return this.textLines.stream()
                .map(TheGame::new).toList()
                .stream()
                .mapToInt(Game::power)
                .sum();
    }
}
