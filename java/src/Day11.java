import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

public class Day11 implements DaySolution<Long> {

    @Override
    public Long part1(Path inputFilePath) {
        return monkeyBusiness(inputFilePath, 20, true);
    }

    @Override
    public Long part2(Path inputFilePath) {
        return monkeyBusiness(inputFilePath, 500, false);
    }

    private long monkeyBusiness(Path inputFilePath, int numberOfRounds, boolean worryLevelDropsBetweenInspections) {
        try (Stream<String> lines = lines(inputFilePath)) {
            Grouper<String> monkeyGrouper = Grouper.groupByItemCount(6);
            AtomicLong reductionFactor = new AtomicLong(1);
            List<Monkey> monkeys = lines
                    .filter(s -> !s.isEmpty())
                    .map(monkeyGrouper::add)
                    .filter(Grouper.Group::isComplete)
                    .map(groupedLines -> parseMonkey(groupedLines.items(), reductionFactor))
                    .toList();

            for (int round = 1; round <= numberOfRounds; round++) {
                for (Monkey monkey : monkeys) {
                    while (!monkey.items.isEmpty()) {
                        long itemWorryLevel = monkey.inspectItem();
                        long newItemWorryLevel = monkey.worryLevelOperation.apply(itemWorryLevel);
                        if (worryLevelDropsBetweenInspections)
                            newItemWorryLevel /= 3;
                        if (newItemWorryLevel % monkey.worryLevelDivider == 0)
                            monkeys.get(monkey.nextMonkeyIfDivisible).receive(newItemWorryLevel);
                        else
                            monkeys.get(monkey.nextMonkeyIfNotDivisible).receive(newItemWorryLevel);
                    }
                }
            }

            long result = monkeys.stream()
                    .map(Monkey::inspectionCounter)
                    .map(AtomicLong::get)
                    .sorted(Comparator.reverseOrder())
                    .limit(2)
                    .reduce(1L, (a, b) -> a * b);

            System.out.println("Product of number of inspections done by two most active monkeys is " + result);

            return result;
        }
    }

    private Monkey parseMonkey(List<String> monkeyLines, AtomicLong reductionFactor) {
        String monkeyIdLine = monkeyLines.get(0);
        int monkeyNr = Integer.parseInt(monkeyIdLine.substring(7, monkeyIdLine.length() - 1));

        LinkedList<Long> items = Arrays.stream(monkeyLines.get(1).substring(18).split(", "))
                .map(Long::parseLong)
                .collect(toCollection(LinkedList::new));

        String[] operationInfo = monkeyLines.get(2).substring(23).split(" ");
        UnaryOperator<Long> worryLevelOperation = new UnaryOperator<>() {
            BinaryOperator<Long> operation = operationInfo[0].equals("+") ? Long::sum : (o1, o2) -> o1 * o2;
            UnaryOperator<Long> getSecondArgument = worryLevel -> operationInfo[1].equals("old") ? worryLevel : Integer.parseInt(operationInfo[1]);

            @Override
            public Long apply(Long worryLevel) {
                return operation.apply(worryLevel, getSecondArgument.apply(worryLevel));
            }
        };

        int worryLevelDivider = Integer.parseInt(monkeyLines.get(3).substring(21));
        reductionFactor.accumulateAndGet(worryLevelDivider, (a, b) -> a * b);

        int nextMonkeyIfDivisible = Integer.parseInt(monkeyLines.get(4).substring(29));
        int nextMonkeyIfNotDivisible = Integer.parseInt(monkeyLines.get(5).substring(30));

        return new Monkey(monkeyNr, items, worryLevelOperation, worryLevelDivider, nextMonkeyIfDivisible, nextMonkeyIfNotDivisible, new AtomicLong(), reductionFactor);
    }

    private record Monkey(
            int nr,
            LinkedList<Long> items,
            UnaryOperator<Long> worryLevelOperation,
            int worryLevelDivider,
            int nextMonkeyIfDivisible,
            int nextMonkeyIfNotDivisible,
            AtomicLong inspectionCounter,
            AtomicLong reductionFactor) {

        void receive(long item) {
            long moddedItem = item % reductionFactor.get();
            items.add(moddedItem);
        }

        public long inspectItem() {
            inspectionCounter.incrementAndGet();
            return items.removeFirst();
        }
    }
}
