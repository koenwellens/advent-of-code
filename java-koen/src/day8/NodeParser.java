package day8;

import common.Parser;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class NodeParser implements Parser<Map<String, Node>> {

    private final List<String> textLines;

    public NodeParser(final List<String> textLines) {
        this.textLines = textLines;
    }

    @Override
    public Map<String, Node> value() {
        return this.textLines.subList(2, textLines.size())
                .stream()
                .map(str -> str.split(" = "))
                .collect(toMap(parts -> parts[0], parts -> new TheNode(parts[1])));
    }
}
