import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class Day16Attempt2 implements DaySolution<Integer> {
    @Override
    public Integer part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            Map<String, Valve> valvesById = lines
                    .map(this::parseValveData)
                    .toList().stream()
                    .collect(toMap(Valve::getId, identity()));

            List<Valve> valvesInNetwork = valvesById.values().stream()
                    .filter(valve -> valve.id.equals("AA") || valve.flowRate > 0)
                    .toList();

            valvesInNetwork.forEach(startValve -> {
                for (Valve targetValve : valvesInNetwork) {
                    if (startValve != targetValve) {
                        AtomicReference<List<String>> shortestPathHolder = new AtomicReference<>();
                        getShortestPath(startValve, startValve, targetValve, List.of(), shortestPathHolder, valvesById);
                        startValve.setShortestDistanceTo(targetValve, shortestPathHolder.get().size());
                    }
                }
            });

            Valve initialNode = valvesById.get("AA");
            return maxPressureReleased(initialNode, 0, 30, 0, copyWithoutElement(valvesInNetwork, initialNode));
        }
    }

    @NotNull
    private static <T> List<T> copyWithoutElement(List<T> list, T element) {
        LinkedList<T> copy = new LinkedList<>(list);
        copy.remove(element);
        return copy;
    }

    private void getShortestPath(
            Valve startValve,
            Valve currentValve,
            Valve targetValve,
            List<String> currentPath,
            AtomicReference<List<String>> shortestPathHolder,
            Map<String, Valve> valvesById) {
        List<String> shortestPath = shortestPathHolder.get();
        if (currentValve == targetValve) {
            shortestPathHolder.set(currentPath);
            return;
        }
        for (String tunnel : currentValve.tunnels) {
            if (!startValve.id.equals(tunnel)
                    && (shortestPath == null || currentPath.size() < shortestPath.size() - 1)
                    && !currentPath.contains(tunnel)) {
                LinkedList<String> path = new LinkedList<>(currentPath);
                path.add(tunnel);
                getShortestPath(
                        startValve,
                        valvesById.get(tunnel),
                        targetValve,
                        List.copyOf(path),
                        shortestPathHolder,
                        valvesById);
            }
        }
    }

    private Valve parseValveData(String valveData) {
        String id = valveData.substring(6, 8);
        int flowRate = parseInt(valveData.substring(valveData.indexOf("=") + 1, valveData.indexOf(";")));
        int tunnelDataIndex = valveData.indexOf(",");
        if (tunnelDataIndex == -1)
            tunnelDataIndex = valveData.length();
        Set<String> tunnels = Set.of(valveData.substring(tunnelDataIndex - 2).split(", "));

        return new Valve(id, flowRate, tunnels);
    }

    int maxPressureReleased(Valve valve, int minutesSpent, int maxMinutes, int totalFlow, List<Valve> remainingValves) {
        if(minutesSpent == maxMinutes) {
            return 0;
        }
        if (remainingValves.isEmpty()) {
            System.out.printf("%s minutes spent%n", maxMinutes);
            System.out.printf("Flow is %s%n", totalFlow);
            System.out.println();
            return totalFlow * (maxMinutes - minutesSpent);
        }
        return remainingValves.stream().mapToInt(nextValve -> {
                    int distanceToNextValve = valve.getDistance(nextValve);
                    int nextMinutesSpent = Math.min(minutesSpent + distanceToNextValve + 1, maxMinutes);
                    System.out.printf("Moving from %s to %s and opening it, %s minutes spent%n", valve.id, nextValve.id, nextMinutesSpent);
                    System.out.printf("Flow was %s, now %s%n", totalFlow, totalFlow + nextValve.flowRate);
                    System.out.println();
                    return maxPressureReleased(
                            nextValve,
                            nextMinutesSpent,
                            maxMinutes,
                            totalFlow + nextValve.flowRate,
                            copyWithoutElement(remainingValves, nextValve))
                            + totalFlow * Math.min(distanceToNextValve + 1, maxMinutes - minutesSpent);
                })
                .max().orElseThrow();
    }

    private static class Valve {
        private final String id;
        private final int flowRate;
        private final Set<String> tunnels;
        private final Map<Valve, Integer> shortestDistanceToValves = new HashMap<>();

        public Valve(String id, int flowRate, Set<String> tunnels) {
            this.id = id;
            this.flowRate = flowRate;
            this.tunnels = tunnels;
        }

        public String getId() {
            return id;
        }

        public void setShortestDistanceTo(Valve targetValve, int distance) {
            shortestDistanceToValves.put(targetValve, distance);
        }

        public int getDistance(Valve valve) {
            return shortestDistanceToValves.get(valve);
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
