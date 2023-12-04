package adventofcode2023.day03;

import java.util.List;
import java.util.regex.MatchResult;

import static java.lang.Long.parseLong;
import static java.util.stream.IntStream.rangeClosed;

record EngineSchematicNumber(long value,
                             int line,
                             List<Integer> indexRange) {

    EngineSchematicNumber(MatchResult matchResult, int lineNumber) {
        this(
                parseLong(matchResult.group()),
                lineNumber,
                rangeClosed(
                        matchResult.start(),
                        matchResult.start() + ("" + parseLong(matchResult.group())).length() - 1).boxed().toList());
    }

}
