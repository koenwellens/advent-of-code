package adventofcode2023.day05;

import adventofcode2023.common.Input;

import java.util.*;

class Day05 {
    public static void main(String[] args) {
        System.out.println("Puzzle 1 example: " + lowestLocationNumberForSeedValues(Day05Input.EXAMPLE)); // expected: 35
        System.out.println("Puzzle 1 input: " + lowestLocationNumberForSeedValues(Day05Input.INPUT)); // expected: 331445006
        System.out.println("Puzzle 2 example: " + lowestLocationNumberForSeedRanges(Day05Input.EXAMPLE)); // expected: 46
        System.out.println("Puzzle 2 input: " + lowestLocationNumberForSeedRanges(Day05Input.INPUT)); // expected: 6472060
    }

    private static long lowestLocationNumberForSeedValues(Input almanacInput) {
        return findLowestLocationByIndividualMapping(almanacInput);
    }

    private static long lowestLocationNumberForSeedRanges(Input almanacInput) {
        return findLowestLocationByRangeMapping(almanacInput);
    }

    private static long findLowestLocationByRangeMapping(Input almanacInput) {
        String[] almanacLines = almanacInput.lines();

        // line 0: chop of 'seeds: ' header, then split and parse seed numbers in the rest of the line
        String valueType = "seed";
        List<Range> valueRanges = parseSeedsLineRanges(almanacLines[0]);

        // skipping seeds line (0) and separator line
        int lineIndex = 2;
        while (!"location".equals(valueType)) {
            // get source and destination of mapping from header mappingLine
            String[] mappingData = almanacLines[lineIndex++].split(" ")[0].split("-");
            assert valueType.equals(mappingData[0]); // source should match previous destination/valueType
            valueType = mappingData[2]; // then set new valueType to new destination

            // process mapping lines until end of mapping (= empty mappingLine)
            List<Range> sourceRanges = new ArrayList<>(valueRanges);
            List<Range> mappedRanges = new ArrayList<>();
            String mappingLine;
            while (lineIndex < almanacLines.length && !(mappingLine = almanacLines[lineIndex++]).isEmpty()) {
                // get mapping data for current line
                long[] parsedMapping = Arrays.stream(mappingLine.split(" ")).mapToLong(Long::parseLong).toArray();
                assert parsedMapping.length == 3;
                Range mappingSourceRange = new Range(parsedMapping[1], parsedMapping[1] + parsedMapping[2] - 1);
                long mappingShift = parsedMapping[0] - mappingSourceRange.start;

                for (int i = 0; i < sourceRanges.size(); i++) {
                    if (sourceRanges.get(i) != null) {
                        Intersection intersect = sourceRanges.get(i).intersect(mappingSourceRange);
                        if (intersect.overlap != null)
                            mappedRanges.add(intersect.overlap.shift(mappingShift));
                        if (intersect.nonOverlappingRanges.length > 0) {
                            sourceRanges.set(i, intersect.nonOverlappingRanges[0]);
                            if (intersect.nonOverlappingRanges.length == 2)
                                sourceRanges.add(intersect.nonOverlappingRanges[1]);
                        } else {
                            sourceRanges.set(i, null);
                        }
                    }
                }
            }

            // source ranges that were not mapped are kept as-is
            mappedRanges.addAll(sourceRanges);

            // prepare next iteration
            valueRanges = mappedRanges;
        }

        return valueRanges.stream()
                .filter(Objects::nonNull)
                .mapToLong(Range::start)
                .min()
                .orElseThrow();
    }

    private static long findLowestLocationByIndividualMapping(Input almanacInput) {
        String[] almanacLines = almanacInput.lines();

        // line 0: chop of 'seeds: ' header, then split and parse seed numbers in the rest of the line
        String valueType = "seed";
        long[] values = parseSeedsLineNumbers(almanacLines[0]);

        // skipping seeds line (0) and separator line
        int lineIndex = 2;
        while (!"location".equals(valueType)) {
            // get source and destination of mapping from header mappingLine

            // WTF dis pattern dont work GRMBL!!!
//            Pattern mappingHeaderPattern = Pattern.compile("(\\w+)-to-(\\w+) map:");
//            Matcher matcher = mappingHeaderPattern.matcher(almanacLine);
//            assert valueType.equals(matcher.group(1)); // source should match previous destination/valueType
//            valueType = matcher.group(2); // then set new valueType to new destination

            String[] mappingData = almanacLines[lineIndex++].split(" ")[0].split("-");
            assert valueType.equals(mappingData[0]); // source should match previous destination/valueType
            valueType = mappingData[2]; // then set new valueType to new destination


            // process mapping lines until end of mapping (= empty mappingLine)
            long[] mappedValues = new long[values.length];
            Arrays.fill(mappedValues, -1L);
            String mappingLine;
            while (lineIndex < almanacLines.length && !(mappingLine = almanacLines[lineIndex++]).isEmpty()) {
                // get mapping data for current line
                long[] parsedMapping = Arrays.stream(mappingLine.split(" ")).mapToLong(Long::parseLong).toArray();
                assert parsedMapping.length == 3;
                long sourceTypeRangeStart = parsedMapping[1];
                long destinationTypeRangeStart = parsedMapping[0];
                long rangeLength = parsedMapping[2];

                // map values that are in range for this mapping
                for (int valueIndex = 0; valueIndex < mappedValues.length; valueIndex++) {
                    // try mapping if value has not yet been mapped by current mapping
                    if (mappedValues[valueIndex] == -1) {
                        // if source value is in the mapping source range
                        if (values[valueIndex] >= sourceTypeRangeStart && values[valueIndex] < sourceTypeRangeStart + rangeLength) {
                            // do mapping by applying value offset in mapping source range to destination range start
                            mappedValues[valueIndex] = destinationTypeRangeStart + (values[valueIndex] - sourceTypeRangeStart);
                        }
                    }
                }
            }

            // keep values that were not mapped by the current mapping
            for (int valueIndex = 0; valueIndex < mappedValues.length; valueIndex++) {
                if (mappedValues[valueIndex] == -1) {
                    mappedValues[valueIndex] = values[valueIndex];
                }
            }

            // prepare next iteration
            values = mappedValues;
        }

        return Arrays.stream(values)
                .min()
                .orElseThrow();
    }

    private static long[] parseSeedsLineNumbers(String seedsLine) {
        return Arrays.stream(seedsLine.substring("seeds: ".length()).split(" "))
                .mapToLong(Long::parseLong)
                .toArray();
    }

    private static List<Range> parseSeedsLineRanges(String seedsLine) {
        long[] seedsLineValues = parseSeedsLineNumbers(seedsLine);
        List<Range> seedRanges = new LinkedList<>();

        //use every two consecutive numbers parsed from the seeds line as a range (start & range length)
        for (int i = 0; i < seedsLineValues.length; i = i + 2) {
            long rangeStart = seedsLineValues[i];
            long rangeLength = seedsLineValues[i + 1];
            seedRanges.add(new Range(rangeStart, rangeStart + rangeLength - 1));
        }

        return seedRanges;
    }

    private record Range(long start, long end) {
        Intersection intersect(Range range) {
            long maxStart = Math.max(range.start, this.start);
            long minEnd = Math.min(range.end, this.end);

            if (minEnd < this.start || maxStart > this.end)
                return new Intersection(null, this);

            Range overlap = new Range(maxStart, minEnd);
            List<Range> nonOverlappingRanges = new ArrayList<>(2);
            if (overlap.start > start) {
                nonOverlappingRanges.add(new Range(start, overlap.start - 1));
            }
            if (overlap.end < end) {
                nonOverlappingRanges.add(new Range(overlap.end + 1, end));
            }
            return new Intersection(overlap, nonOverlappingRanges.toArray(new Range[0]));
        }

        public Range shift(long offset) {
            return new Range(start + offset, end + offset);
        }
    }

    private record Intersection(Range overlap, Range... nonOverlappingRanges) {
    }
}
