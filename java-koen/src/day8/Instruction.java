package day8;

import java.util.function.Function;

public enum Instruction {

    L(Node::left),
    R(Node::right),
    ;

    private final Function<Node, String> value;

    Instruction(final Function<Node, String> value) {
        this.value = value;
    }

    public Function<Node, String> value() {
        return this.value;
    }
}
