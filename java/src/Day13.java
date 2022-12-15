import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Day13 implements DaySolution<Integer> {
    @Override
    public Integer part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            Grouper<LinkedList<?>> packetGrouper = Grouper.groupByItemCount(2);
            Comparator<Object> packetComparator = new PacketComparator();
            AtomicInteger indexGenerator = new AtomicInteger();
            int sumOfIndexesInRightOrder = parsePacketList(lines)
                    .map(packetGrouper::add)
                    .filter(Grouper.Group::isComplete)
                    .map(group -> packetComparator.compare(group.items().get(0), group.items().get(1)))
//                    .peek(System.out::println)
                    .mapToInt(comparison -> indexGenerator.incrementAndGet() * (comparison == 1 ? 1 : 0))
                    .sum();

            System.out.println("The sum of the indexes of the pairs that are in the correct order is " + sumOfIndexesInRightOrder);

            return sumOfIndexesInRightOrder;
        }
    }

    @Override
    public Integer part2(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            Comparator<Object> packetComparator = new PacketComparator();
            Set<LinkedList<?>> dividerPackets = Set.of(
                    linkedListOf(linkedListOf(2)),
                    linkedListOf(linkedListOf(6)));
            AtomicInteger indexGenerator = new AtomicInteger();
            int productOfDividerPackagesIndexes = Stream.concat(parsePacketList(lines), dividerPackets.stream())
                    .sorted(packetComparator.reversed())
//                    .peek(System.out::println)
                    .mapToInt(packet -> indexGenerator.incrementAndGet() * (dividerPackets.contains(packet) ? 1 : 0))
                    .filter(v -> v != 0)
                    .reduce(1, (a, b) -> a * b);

            System.out.println("The product of the indexes of the divider packages is " + productOfDividerPackagesIndexes);

            return productOfDividerPackagesIndexes;
        }
    }

    @NotNull
    private Stream<? extends LinkedList<?>> parsePacketList(Stream<String> lines) {
        return lines
                .filter(s -> !s.isEmpty())
                .map(this::parsePacket);
    }

    private LinkedList<?> parsePacket(String packetLine) {
        StringTokenizer tokens = new StringTokenizer(packetLine.replace("", " "), " ");

        tokens.nextToken(); // skip first token as it always starts with [
        LinkedList<Object> packet = new LinkedList<>();
        parsePacketTokens(tokens, packet);

        return packet;
    }

    private void parsePacketTokens(StringTokenizer tokens, LinkedList<Object> subPacket) {
        String buffer = "";
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (token.equals("[")) {
                LinkedList<Object> newSubPacket = new LinkedList<>();
                subPacket.add(newSubPacket);
                parsePacketTokens(tokens, newSubPacket);
            } else if (Character.isDigit(token.charAt(0))) {
                buffer += token;
            } else if (token.equals(",")) {
                if (!buffer.isEmpty()) {
                    subPacket.add(Integer.parseInt(buffer));
                    buffer = "";
                }
            } else if (token.equals("]")) {
                if (!buffer.isEmpty()) {
                    subPacket.add(Integer.parseInt(buffer));
                }
                return;
            }
        }

    }

    private static class PacketComparator implements Comparator<Object> {
        @Override
        public int compare(Object p1, Object p2) {
//            System.out.println("Compare " + p1 + " vs " + p2);
            if (p1 instanceof Integer int1 && p2 instanceof Integer int2) {
                int compareInts = int1.compareTo(int2);
                if (compareInts != 0) {
                    compareInts = -1 * compareInts / Math.abs(compareInts);
                }
                return compareInts;
            } else if (p1 instanceof LinkedList<?> list1 && p2 instanceof LinkedList<?> list2) {
                if (list1.isEmpty()) {
                    return list2.isEmpty() ? 0 : 1;
                } else if (list2.isEmpty()) {
                    return -1;
                } else {
                    int compareFirstElements = compare(list1.getFirst(), list2.getFirst());
                    return compareFirstElements == 0
                            ? compare(withoutFirstElement(list1), withoutFirstElement(list2))
                            : compareFirstElements;
                }
            } else if (p1 instanceof Integer int1 && p2 instanceof LinkedList<?> list2) {
                return compare(linkedListOf(int1), list2);
            } else if (p1 instanceof LinkedList<?> list1 && p2 instanceof Integer int2) {
                return compare(list1, linkedListOf(int2));
            } else
                throw new IllegalArgumentException("item1=" + p1.getClass() + "' item2=" + p2.getClass());
        }

        private LinkedList<?> withoutFirstElement(LinkedList<?> list) {
            LinkedList<?> copy = new LinkedList<>(list);
            copy.removeFirst();
            return copy;
        }
    }

    private static LinkedList<?> linkedListOf(Object o) {
        LinkedList<Object> objects = new LinkedList<>();
        objects.add(o);
        return objects;
    }
}
