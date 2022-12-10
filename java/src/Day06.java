import java.nio.file.Path;
import java.util.stream.Stream;

public class Day06 implements DaySolution<Integer> {

    @Override
    public Integer part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            int endPositionOfFirstStartOfPacketMarker = lines
                    .mapToInt(line -> findEndPositionOfFirstStartOfPacketMarker(line, 4))
                    .findFirst()
                    .orElseThrow();

            System.out.println("End position of first start-of-packet marker is " + endPositionOfFirstStartOfPacketMarker);
            return endPositionOfFirstStartOfPacketMarker;
        }
    }

    @Override
    public Integer part2(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            int endPositionOfFirstStartOfMessageMarker = lines
                    .mapToInt(line -> findEndPositionOfFirstStartOfPacketMarker(line, 14))
                    .findFirst()
                    .orElseThrow();

            System.out.println("End position of first start-of-nessage marker is " + endPositionOfFirstStartOfMessageMarker);
            return endPositionOfFirstStartOfMessageMarker;
        }
    }


    private int findEndPositionOfFirstStartOfPacketMarker(String message, int packetMarkerSize) {
        int position = packetMarkerSize;
        int[] buffer = new int[packetMarkerSize];
        for (int i = 0; i < packetMarkerSize; i++) {
            buffer[i] = message.charAt(i);
        }
        while (position < message.length()) {
            boolean noDoubles = true;
            for (int p1 = 0; noDoubles && p1 < packetMarkerSize - 1; p1++) {
                for (int p2 = p1 + 1; noDoubles && p2 < packetMarkerSize; p2++) {
                    noDoubles = buffer[p1] != buffer[p2];
                }
            }

            if (noDoubles)
                return position;

            buffer[position % packetMarkerSize] = message.charAt(position);
            position++;
        }
        throw new RuntimeException("marker not found");
    }
}
