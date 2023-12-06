package day6;

public final class TheBoat implements Boat {

    public long computeDistance(final long durationOfButtonPress, final long durationOfMovement) {
        return durationOfButtonPress * durationOfMovement;
    }
}
