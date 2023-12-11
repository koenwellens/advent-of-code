package day9;

public interface Sequence {

    boolean ends();

    Sequence next();

    Long extrapolateRight(Long value);

    Long extrapolateLeft(Long value);
}
