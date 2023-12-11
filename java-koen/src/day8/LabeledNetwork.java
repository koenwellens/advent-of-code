package day8;

import common.AbstractObjectBasedOnInput;

import java.util.Collection;
import java.util.List;

public final class LabeledNetwork extends AbstractObjectBasedOnInput<Long> {
    public LabeledNetwork(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Long run() {
        final var instructions = new InstructionsParser(textLines).value();
        final var nodes = new NodeParser(textLines).value();

        var result = 0L;
        final var startNode = "AAA";
        final var endNode = "ZZZ";
        var index = 0;
        String next = startNode;
        do {
            final var currentNode = nodes.get(next);
            final var instruction = instructions.get((index));
            index = (index + 1) % instructions.size();
            next = instruction.value().apply(currentNode);
            result++;
        } while (!endNode.equals(next));

        return result;
    }

    @Override
    public Long alternateRun() {
        // TODO andere manier:
        // eerst berekenen
        final var instructions = new InstructionsParser(textLines).value();
        final var nodes = new NodeParser(textLines).value();
        final var startNodes = nodes.keySet().stream().filter(s -> s.endsWith("A")).toList();

        var result = 0L;
        var index = 0;
        Collection<String> next = startNodes;
        do {
            final var instruction = instructions.get((index));
            index = (index + 1) % instructions.size();
            result++;
            next = next.stream()
                    .map(nodes::get)
                    .map(instruction.value())
                    .toList();

            System.out.println(next);
        } while (!next.stream().allMatch(s -> s.endsWith("Z")));

        return result;
    }
}
