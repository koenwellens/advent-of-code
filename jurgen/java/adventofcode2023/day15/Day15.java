package adventofcode2023.day15;


import adventofcode2023.common.Input;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day15.Day15Input.EXAMPLE;
import static adventofcode2023.day15.Day15Input.INPUT;

class Day15 {
    public static void main(String[] args) {
        runnerFor(Day15.class)
                .execute("Puzzle 1", Day15::sumOfInstructionHashes).withInput(EXAMPLE).expect(1320)
                .execute("Puzzle 1", Day15::sumOfInstructionHashes).withInput(INPUT).expect(520500)
                .execute("Puzzle 2", Day15::lensesFocussingPower).withInput(EXAMPLE).expect(145)
                .execute("Puzzle 2", Day15::lensesFocussingPower).withInput(INPUT).expect(213097)
                .build()
                .run();

    }

    private static int sumOfInstructionHashes(Input initSequenceData) {
        return Arrays.stream(initSequenceData.rawData.split(","))
                .mapToInt(Day15::hash)
                .sum();
    }

    private static Object lensesFocussingPower(Input initSequenceData) {
        Map<Integer, List<Lens>> boxContents = IntStream.range(0, 256)
                .boxed()
                .collect(Collectors.toMap(
                        Function.identity(),
                        id -> new ArrayList<>()));

        Arrays.stream(initSequenceData.rawData.split(","))
                .map(String::trim)
                .forEach(instruction -> {
                    if (instruction.contains("=")) {
                        String[] spltInstruction = instruction.split("=");

                        String label = spltInstruction[0];
                        int focalLength = Integer.parseInt(spltInstruction[1]);
                        int boxNr = hash(label);

                        List<Lens> boxLenses = boxContents.get(boxNr);
                        boolean foundLabel = false;
                        for (int i = 0; i < boxLenses.size(); i++) {
                            if (boxLenses.get(i) != null && boxLenses.get(i).label.equals(label)) {
                                boxLenses.set(i, new Lens(label, focalLength));
                                foundLabel = true;
                            }
                        }

                        if (!foundLabel)
                            boxLenses.add(new Lens(label, focalLength));
                    } else if (instruction.endsWith("-")) {
                        String label = instruction.substring(0, instruction.indexOf("-"));
                        int boxNr = hash(label);
                        List<Lens> boxLenses = boxContents.get(boxNr);
                        for (int i = 0; i < boxLenses.size(); i++) {
                            if (boxLenses.get(i) != null && boxLenses.get(i).label.equals(label)) {
                                boxLenses.set(i, null);
                            }
                        }

                    } else {
                        throw new IllegalArgumentException();
                    }

//                    System.out.printf("After \"%s\":%n", instruction);
//                    boxContents.keySet().stream()
//                            .sorted()
//                            .forEach(nr -> {
//                                List<Lens> lenses = boxContents.get(nr);
//                                if (lenses.stream().anyMatch(Objects::nonNull))
//                                    System.out.printf(
//                                            "Box %s: %s%n",
//                                            nr,
//                                            lenses.stream()
//                                                    .filter(Objects::nonNull)
//                                                    .map(lens -> String.format("[%s %s]", lens.label, lens.focalLength))
//                                                    .collect(Collectors.joining(" ")));
//                            });
                });

        return boxContents.entrySet().stream()
                .mapToInt((entry) -> {
                    if (entry.getValue().isEmpty())
                        return 0;

                    AtomicInteger lensSlot = new AtomicInteger();
                    return entry.getValue().stream()
                            .filter(Objects::nonNull)
                            .mapToInt(lens -> (entry.getKey() + 1) * lensSlot.incrementAndGet() * lens.focalLength)
                            .sum();
                })
                .sum();
    }

    private static int hash(String text) {
        int value = 0;
        for (char ch : text.trim().toCharArray()) {
            if (ch != '\n') {
                value += ch;
                value *= 17;
                value %= 256;
            }
        }
        return value;
    }

    private record Lens(String label, int focalLength) {
    }
}
