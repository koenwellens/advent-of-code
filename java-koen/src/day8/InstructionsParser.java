package day8;

import common.Parser;

import java.util.List;

import static java.util.Arrays.stream;

public final class InstructionsParser implements Parser<List<Instruction>> {

    private final String line;

    public InstructionsParser(final List<String> textLines) {
        this(textLines.get(0));
    }

    public InstructionsParser(final String line) {
        this.line = line;
    }


    @Override
    public List<Instruction> value() {
        return stream(line.split("")).map(Instruction::valueOf).toList();
    }
}
