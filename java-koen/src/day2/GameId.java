package day2;

import static java.lang.Integer.parseInt;

public final class GameId extends AbstractTextParser<Integer> {

    private static final int PREFIX_LENGTH = "Game ".length();

    public GameId(final String text) {
        super(text);
    }

    @Override
    public Integer value() {
        final var gameId = text.split(":")[0]
                .substring(PREFIX_LENGTH);

        return parseInt(gameId);
    }
}
