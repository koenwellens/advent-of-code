package aoc2024.d17;

import aoc2024.common.Runner;
import aoc2024.common.Tuple2;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static aoc2024.common.Input.streamInputLines;
import static aoc2024.common.Runner.run;

public class D17 {
    public static void main(String[] args) {
        int x = 034543;
        System.out.println(16316994351714L); // 130.535.954.813.712

        System.out.println(14680);
//        run("Program output", D17::programOutput,
//                Runner.Run.r("example.txt", "4,6,3,5,6,3,5,2,1,0"),
//                Runner.Run.r("input.txt", "4,1,5,3,1,5,3,5,7"));
        run("Value A for which program output is program itself", D17::valueForRegisterAForWhichProgramOutputIsCopyOfProgram,
                Runner.Run.r("example2.txt", 117440L)
                , Runner.Run.r("input.txt", 0)
        );
    }

    private static String programOutput(Path path) {
        Tuple2<Map<String, Long>, int[]> parseResult = parse(path);

        Machine machine = new Machine(parseResult.a);
        machine.execute(parseResult.b);

        return machine.output.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    private static long valueForRegisterAForWhichProgramOutputIsCopyOfProgram(Path path) {
        Tuple2<Map<String, Long>, int[]> parseResult = parse(path);
        System.out.println("start");

        // program output = register A / 8  =>  convert to oct  => reverse digit order => comma separated
        // => ⚠️ works for example, not for input

        // 1. remove comma's
        //   -> done by parsing into int[]
        System.out.println(Arrays.stream(parseResult.b).boxed().map(Object::toString).collect(Collectors.joining("")));

        // 2. reverse order
        int[] reversed = new int[parseResult.b.length];
        for (int i = 0; i < parseResult.b.length; i++) {
            reversed[i] = parseResult.b[parseResult.b.length - 1 - i];
        }
        System.out.println(Arrays.stream(reversed).boxed().map(Object::toString).collect(Collectors.joining("")));

        // 3. parse as oct number
        String octalNumberString = Arrays.stream(reversed)
                .boxed()
                .map(Object::toString)
                .collect(Collectors.joining(""));
        long number = Long.parseLong(octalNumberString, 8);
        System.out.println(number);


        // 4. multiply by 8
        long aVal = number * 8;
        System.out.println(aVal);


        // 5. test
        HashMap<String, Long> objectObjectHashMap = new HashMap<>(parseResult.a);
        objectObjectHashMap.put("A", aVal);
        Machine machine = new Machine(objectObjectHashMap);
        machine.execute(parseResult.b);
        System.out.println(machine.output.stream().map(Object::toString).collect(Collectors.joining(",")));



        return aVal;
    }

    private static Tuple2<Map<String, Long>, int[]> parse(Path path) {
        Map<String, Long> registers = new HashMap<>();
        int[] program = streamInputLines(path)
                .peek(line -> {
                    if (line.startsWith("Register")) {
                        String registerName = line.substring(9, 10);
                        long registerValue = Long.parseLong(line.substring(12));
                        registers.put(registerName, registerValue);
                    }
                })
                .filter(line -> line.startsWith("Program"))
                .map(line -> line.substring(9))
                .flatMap(line -> Arrays.stream(line.split(",")))
                .mapToInt(Integer::parseInt)
                .toArray();
        return Tuple2.of(registers, program);
    }

    static class Machine {
        private final Map<String, Long> registers = new HashMap<>();
        private final AtomicInteger pointerHolder = new AtomicInteger();
        private final ArrayList<Long> output = new ArrayList<>();

        public Machine(Map<String, Long> registers) {
            this.registers.putAll(registers);
        }

        void execute(int[] program) {
            while (pointerHolder.get() < program.length) {
                int pointer = pointerHolder.get();
                int instr = program[pointer];
                int operand = program[pointer + 1];
                boolean advancePointer = switch (instr) {
                    case 0 -> execute0(operand);
                    case 1 -> execute1(operand);
                    case 2 -> execute2(operand);
                    case 3 -> execute3(operand);
                    case 4 -> execute4(operand);
                    case 5 -> execute5(operand);
                    case 6 -> execute6(operand);
                    case 7 -> execute7(operand);
                    default -> throw new IllegalStateException("Unexpected instr: " + instr);
                };
                if (advancePointer) {
                    pointerHolder.updateAndGet(p -> p + 2);
                }
            }
        }

        private boolean execute0(int operand) {
            registers.compute("A", (_, a) -> a >> comboOperand(operand));
            return true;
        }

        private boolean execute1(int operand) {
            registers.compute("B", (_, b) -> b ^ literalOperand(operand));
            return true;
        }

        private boolean execute2(int operand) {
            registers.put("B", comboOperand(operand) % 8);
            return true;
        }

        private boolean execute3(int operand) {
            if (registers.get("A") != 0) {
                pointerHolder.set((int) literalOperand(operand));
                return false;
            }
            return true;
        }

        private boolean execute4(int operand) {
            registers.compute("B", (_, b) -> b ^ registers.get("C"));
            return true;
        }

        private boolean execute5(int operand) {
            output(comboOperand(operand) % 8);
            return true;
        }

        private boolean execute6(int operand) {
            registers.put("B", registers.get("A") >> comboOperand(operand));
            return true;
        }

        private boolean execute7(int operand) {
            registers.put("C", registers.get("A") >> comboOperand(operand));
            return true;
        }

        private void output(long value) {
            output.add(value);
        }

        private long comboOperand(int operand) {
            return switch (operand) {
                case int op when op < 4 -> operand;
                case 4 -> registers.get("A");
                case 5 -> registers.get("B");
                case 6 -> registers.get("C");
                default -> throw new IllegalArgumentException("Invalid operand: " + operand);
            };
        }

        private long literalOperand(int operand) {
            return operand;
        }
    }
}
