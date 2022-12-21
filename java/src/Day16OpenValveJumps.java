import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static common.CollectionTools.copyWithoutElement;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
import static java.util.Arrays.copyOfRange;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * Solves the puzzle by determining how many minutes will be spent before the next selected valve will be opened and
 * then calculates the pressure relieved in that time, chooses next valve to open and repeat
 * Works very well for part 1 and part2 with the example
 * Part 2 with the real input completes correctly bu takes about 35 minutes to complete
 */
public class Day16OpenValveJumps implements DaySolution<Integer> {
    @Override
    public Integer part1(Path inputFilePath) {
        return run(inputFilePath, 30, 1);
    }

    @Override
    public Integer part2(Path inputFilePath) {
        return run(inputFilePath, 26, 2);
    }

    private int run(Path inputFilePath, int maxMinutes, int numberOfParticipants) {
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
            State[] initialStates = new State[numberOfParticipants];
            Arrays.fill(initialStates, new State(initialNode, 0));
            return maxPressureReleased(initialStates, 0, maxMinutes, 0, copyWithoutElement(valvesInNetwork, initialNode));
        }
    }

    int maxPressureReleased(State[] states, int minutesSpent, int maxMinutes, int totalFlow, List<Valve> remainingValves) {
//        if (totalFlow > 81)
//            System.out.println("HELP");
        if (minutesSpent == maxMinutes) {
            return 0;
        }
        if (remainingValves.isEmpty() && Arrays.stream(states).allMatch(state -> state.remainingMinutesBeforeOpening == 0)) {
//            System.out.printf("%s minutes spent%n", maxMinutes);
//            System.out.printf("Flow is %s%n", totalFlow);
//            System.out.println();
            return totalFlow * (maxMinutes - minutesSpent);
        }
        if (remainingValves.isEmpty())
            return spendTimeToOpenNextValve(minutesSpent, maxMinutes, totalFlow, List.of(),
                    Arrays.stream(states).filter(state -> state.remainingMinutesBeforeOpening > 0).toArray(State[]::new));

        return generateNextStates(states, remainingValves)
                .mapToInt(targetStates -> spendTimeToOpenNextValve(minutesSpent, maxMinutes, totalFlow, remainingValves, targetStates))
                .max().orElseThrow();
    }

    private int spendTimeToOpenNextValve(int minutesSpent, int maxMinutes, int totalFlow, List<Valve> remainingValves, State[] targetStates) {
//        String collect = Arrays.stream(targetStates).map(s -> s.valve.id).collect(Collectors.joining());
        AtomicInteger minutesBeforeNextValveOpened = new AtomicInteger(maxMinutes);
        List<Valve> nextRemainingValves = new ArrayList<>(remainingValves);
        Arrays.stream(targetStates).forEach(targetState -> {
            minutesBeforeNextValveOpened.accumulateAndGet(targetState.remainingMinutesBeforeOpening, Math::min);
            nextRemainingValves.remove(targetState.valve);
        });
        int minutesToSpend = min(minutesBeforeNextValveOpened.get(), maxMinutes - minutesSpent);
//                    int nextMinutesSpent = min(minutesSpent + minutesBeforeNextValveOpened, maxMinutes);
//                    System.out.printf("Moving from %s to %s and opening it, %s minutes spent%n", valve.id, targetStates.id, nextMinutesSpent);
//                    System.out.printf("Flow was %s, now %s%n", totalFlow, totalFlow + targetStates.flowRate);
//                    System.out.println();
        AtomicInteger flowRateAccumulator = new AtomicInteger(totalFlow);
//        System.out.println("SPENT " + minutesSpent);
        State[] statesAtNextStop = Arrays.stream(targetStates)
                .map(state -> new State(state.valve, state.remainingMinutesBeforeOpening() - minutesToSpend))
                .peek(state -> {
                    if (state.remainingMinutesBeforeOpening == 0) {
                        flowRateAccumulator.accumulateAndGet(state.valve().flowRate, Integer::sum);
//                        System.out.println("opened " + state.valve().id + "(" + state.valve.flowRate + "); remainingMinutes=" + state.remainingMinutesBeforeOpening + " total " + flowRateAccumulator.get() + " remaining=" + remainingValves.size());
                    }
                })
                .toArray(State[]::new);
        return totalFlow * minutesToSpend
                + maxPressureReleased(
                statesAtNextStop,
                minutesSpent + minutesToSpend,
                maxMinutes,
                flowRateAccumulator.get(),
                nextRemainingValves);
    }

    private static Stream<State[]> generateNextStates(State[] states, List<Valve> remainingValves) {
        if (remainingValves.isEmpty())
            throw new IllegalStateException("Don't come here then");

        int[] indexesNeedingReplacement = IntStream.range(0, states.length)
                .filter(index -> states[index].remainingMinutesBeforeOpening == 0)
                .toArray();

        List<Valve> valvesToDistribute = new ArrayList<>(remainingValves);
        while (valvesToDistribute.size() < indexesNeedingReplacement.length) {
            valvesToDistribute.add(Valve.NO_VALVE);
        }

        return generatePermutations(valvesToDistribute, states, indexesNeedingReplacement);
    }

    private static Stream<State[]> generatePermutations(List<Valve> remainingValves, State[] states, int[] indexesNeedingReplacement) {
        if (indexesNeedingReplacement.length == 0 || remainingValves.isEmpty())
            return Stream.<State[]>builder().add(states).build();
        return remainingValves.stream()
                .flatMap(valve -> {
                    State[] newStates = Arrays.copyOf(states, states.length);
                    newStates[indexesNeedingReplacement[0]] = valve == Valve.NO_VALVE
                            ? State.NO_VALVE_STATE
                            : new State(valve, states[indexesNeedingReplacement[0]].valve.getDistance(valve) + 1);
                    return generatePermutations(
                            copyWithoutElement(remainingValves, valve),
                            newStates,
                            copyOfRange(indexesNeedingReplacement, 1, indexesNeedingReplacement.length));
                });
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

    private static class Valve {
        public static final Valve NO_VALVE = new Valve("NONE", 0, Set.of());
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

    private record State(Valve valve, int remainingMinutesBeforeOpening) {
        public static final State NO_VALVE_STATE = new State(Valve.NO_VALVE, 1000);
    }

}
