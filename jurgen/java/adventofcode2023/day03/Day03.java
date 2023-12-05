package adventofcode2023.day03;

import adventofcode2023.common.Input;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static adventofcode2023.common.CollectionUtils.streamOfAll;
import static adventofcode2023.common.CollectionUtils.unionOf;
import static adventofcode2023.common.LongOperations.MULTIPLY;
import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day03.Day03Input.EXAMPLE;
import static adventofcode2023.day03.Day03Input.INPUT;
import static java.util.stream.Collectors.toSet;

class Day03 {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final Pattern SYMBOL_PATTERN = Pattern.compile("[^\\d.]");
    private static final Pattern GEAR_SYMBOL_PATTERN = Pattern.compile("\\*");

    public static void main(String[] args) {
        runnerFor(Day03.class)
                .execute("Puzzle 1", Day03::sumOfPartNumbers).withInput(Day03Input.EXAMPLE).expect(4361L)
                .execute("Puzzle 1", Day03::sumOfPartNumbers).withInput(Day03Input.INPUT).expect(517021L)
                .execute("Puzzle 2", Day03::sumOfGearRatios).withInput(EXAMPLE).expect(467835L)
                .execute("Puzzle 2", Day03::sumOfGearRatios).withInput(Day03Input.INPUT).expect(81296995L)
                .build()
                .run();
    }

    private static long sumOfPartNumbers(Input engineSchematicData) {
        Set<Integer> indexesAffectedByPreviousLineSymbols = new HashSet<>();
        Set<EngineSchematicNumber> previousLineNumbers = new HashSet<>();
        Set<EngineSchematicNumber> allNumberParts = new HashSet<>();

        String[] engineSchematicDataLines = engineSchematicData.lines();
        for (int line = 0; line < engineSchematicDataLines.length; line++) {
            // parse current line symbols and map to range of affected indexes
            // a symbol detected at index i affects indexes i-1 to i+1
            Set<Integer> indexesAffectedByCurrentLineSymbols = SYMBOL_PATTERN
                    .matcher(engineSchematicDataLines[line])
                    .results()
                    .map(MatchResult::start)
                    .flatMap(Day03::affectedIndexRange)
                    .collect(toSet());

            Set<EngineSchematicNumber> currentLineNumbers = parseNumbers(engineSchematicDataLines[line], line);

            // select all numbers of the previous and current line that occupy an index that is affected by symbols on the current or previous line
            // first and last lines only contain numbers in both example and input, so no need to look at the next line
            Set<Integer> indexesAffectedByCurrentAndPreviousLineSymbols = unionOf(indexesAffectedByPreviousLineSymbols, indexesAffectedByCurrentLineSymbols);
            streamOfAll(currentLineNumbers, previousLineNumbers)
                    .filter(number -> number.indexRange().stream().anyMatch(indexesAffectedByCurrentAndPreviousLineSymbols::contains))
                    .forEach(allNumberParts::add);

            // prepare next iteration
            previousLineNumbers = currentLineNumbers;
            indexesAffectedByPreviousLineSymbols = indexesAffectedByCurrentLineSymbols;
        }

        return allNumberParts.stream()
                .mapToLong(EngineSchematicNumber::value)
                .sum();
    }

    private static long sumOfGearRatios(Input engineSchematicData) {
        Set<GearSymbol> gearWindowLine1GearSymbols = null;
        Set<EngineSchematicNumber> gearWindowLine0Numbers = null;
        Set<EngineSchematicNumber> gearWindowLine1Numbers = null;
        long sumOfGearRatios = 0;

        String[] engineSchematicDataLines = engineSchematicData.lines();
        for (int line = 0; line < engineSchematicDataLines.length; line++) {
            // parse current line gear symbols and determine range of affected indexes
            // a symbol detected at index i affects indexes i-1 to i+1
            Set<GearSymbol> gearWindowLine2GearSymbols = GEAR_SYMBOL_PATTERN
                    .matcher(engineSchematicDataLines[line])
                    .results()
                    .map(Day03::gearSymbolInstance)
                    .collect(toSet());

            // consider current line as last line (#2) of a 3 line window
            // only continue if the window is complete (line 0 is not null)
            Set<EngineSchematicNumber> gearWindowLine2Numbers = parseNumbers(engineSchematicDataLines[line], line);
            if (gearWindowLine0Numbers != null) {
                Set<EngineSchematicNumber> allWindowEngineSchematicNumbers = unionOf(gearWindowLine0Numbers, gearWindowLine1Numbers, gearWindowLine2Numbers);
                // match gear symbols of middle line (#1) with all adjacent numbers in the window (lines #0-#2)
                //
                long sumOfGearRatiosForWindowLine1 = gearWindowLine1GearSymbols.stream()
                        .map(gearSymbol -> allWindowEngineSchematicNumbers.stream().filter(gearSymbol::isAdjacentTo).collect(toSet()))
                        .filter(engineSchematicNumbers -> engineSchematicNumbers.size() == 2)
                        .mapToLong(values -> values.stream().map(EngineSchematicNumber::value).reduce(MULTIPLY).orElseThrow())
                        .sum();
                sumOfGearRatios += sumOfGearRatiosForWindowLine1;
            }

            // prepare next iteration
            gearWindowLine0Numbers = gearWindowLine1Numbers;
            gearWindowLine1Numbers = gearWindowLine2Numbers;
            gearWindowLine1GearSymbols = gearWindowLine2GearSymbols;
        }

        return sumOfGearRatios;
    }

    /**
     * parse current line numbers
     * keep value, line number (needed to ensure single entry in Set of all numbers to consider at the end) and occupied index range
     */
    @NotNull
    private static Set<EngineSchematicNumber> parseNumbers(String engineSchematicDataLine, int currentLine) {
        return NUMBER_PATTERN.matcher(engineSchematicDataLine)
                .results()
                .map(matchResult -> new EngineSchematicNumber(matchResult, currentLine))
                .collect(toSet());
    }

    private static GearSymbol gearSymbolInstance(MatchResult matchResult) {
        return new GearSymbol(affectedIndexRange(matchResult.start()).collect(toSet()));
    }

    // Calculate range of affected indexes
    // a symbol detected at index i affects indexes i-1 to i+1
    // WARNING does not verify if range is within input data range
    @NotNull
    private static Stream<Integer> affectedIndexRange(Integer index) {
        return IntStream.rangeClosed(index - 1, index + 1).boxed();
    }

}
