package day8;

public final class TheNode implements Node {

    private final String left;
    private final String right;

    public TheNode(final String fullLine) {
        this(fullLine.substring(1, 4), fullLine.substring(5, 9));
    }

    public TheNode(final String left, final String right) {
        this.left = left.trim();
        this.right = right.trim();
    }

    @Override
    public String left() {
        return left;
    }

    @Override
    public String right() {
        return right;
    }
}
