package aoc2024.common;

public class Tuple2<A, B> {
    public final A a;
    public final B b;

    private Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static <A, B> Tuple2 of(A a, B b) {
        return new Tuple2(a, b);
    }
}
