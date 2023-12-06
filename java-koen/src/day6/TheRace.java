package day6;

import static java.lang.Long.parseLong;

public final class TheRace implements Race {

    private final long duration;
    private final long recordDistance;
    private final Boat boat;

    public TheRace(final String duration, final String recordDistance) {
        this(parseLong(duration), parseLong(recordDistance));
    }

    public TheRace(final long duration, final long recordDistance) {
        this.duration = duration;
        this.recordDistance = recordDistance;
        this.boat = new TheBoat();
    }

    @Override
    public int waysOfWinning() {
        int result = 0;
        for (long i = 0; i < duration; i++) {
            final var distance = boat.computeDistance(i, duration - i);
            if (distance > recordDistance) {
                result++;
            }
        }
        return result;
    }
}
