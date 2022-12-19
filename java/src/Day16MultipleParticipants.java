import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.util.Set.copyOf;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class Day16MultipleParticipants implements DaySolution<Integer> {

    // Contains a bug - after rolling back path nodes the valves available for selection somehow gets messed up

    @Override
    public Integer part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            List<Valve> valves = lines
                    .map(this::parseValveData)
                    .toList();
            ValvesAndTunnels system = new ValvesAndTunnels(valves);
            ValvePathWalker valvePathWalker = new ValvePathWalker(system);
            valvePathWalker.startWalking("AA", 1, 30);

            return valvePathWalker.maxPressureRelieved;
        }
    }

    @Override
    public Integer part2(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            List<Valve> valves = lines
                    .map(this::parseValveData)
                    .toList();
            ValvesAndTunnels system = new ValvesAndTunnels(valves);
            ValvePathWalker valvePathWalker = new ValvePathWalker(system);
            valvePathWalker.startWalking("AA", 2, 26);

            return valvePathWalker.maxPressureRelieved;
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

    private static class ValvePathWalker {

        private final ValvesAndTunnels system;
        private int maxPressureRelieved = 0;

        private final LinkedList<ValvePathNode> path = new LinkedList<>();

        ValvePathWalker(ValvesAndTunnels system) {
            this.system = system;
        }

        void startWalking(String initialValveId, int numberOfTravellers, int maxMinutes) {
            Valve initialValve = system.getValve(initialValveId);
            path.add(new ValvePathNode(0, 0, system.getValvesToOpen()));

            Traveller[] travellers = new Traveller[numberOfTravellers];
            Arrays.fill(travellers, new Traveller(initialValve, List.of()));
            doMinute(0, travellers, maxMinutes);
        }

        private void doMinute(int minutesSpent, Traveller[] travellers, int maxMinutes) {
            ValvePathNode previousPathNode = path.getLast();
            if (minutesSpent == maxMinutes) {
                System.out.println("== After Minute 30 ==");
                System.out.printf("\tPressure released in current path is %s%n", previousPathNode.totalPressureRelieved);
                maxPressureRelieved = max(maxPressureRelieved, previousPathNode.totalPressureRelieved);
                return;
            }

            System.out.printf("== Minute %s ==%n", minutesSpent + 1);
            System.out.printf("\tAt %s%n", Arrays.stream(travellers).map(Traveller::currentValve).toArray());
            System.out.printf("\tValves [%s] are open, releasing %s pressure, total released is %s%n",
                    system.valvesById.values().stream().filter(v -> v.open).map(v -> v.id).sorted().collect(joining(", ")),
                    system.currentTotalFlowRate,
                    previousPathNode.totalPressureRelieved
            );


            if (system.allValvesAreOpen()) {
                System.out.println("\tAll valves open");
                path.add(new ValvePathNode(
                        system.currentTotalFlowRate,
                        previousPathNode.totalPressureRelieved + previousPathNode.totalFlowRate,
                        previousPathNode.valvesAvailableForSelection));
                doMinute(minutesSpent + 1, travellers, maxMinutes);
                rollback();
            } else {
                Traveller[] newTravellers = new Traveller[travellers.length];
                Set<Valve> valvesOpenedThisMinute = new HashSet<>();
                Map<String, Set<Integer>> travellersThatNeedPathSelectionGroupedByCurrentValve = new HashMap<>();
                for (int p = 0; p < travellers.length; p++) {
                    if (travellers[p].isTravelling()) {
                        System.out.printf("\t%s is travelling: %s%n", p, travellers[p].pathToTargetValve);
                        newTravellers[p] = doMove(travellers[p]);
                    } else if (travellers[p].currentValve.canBeOpened()) {
                        Valve valveToOpen = travellers[p].currentValve;
                        System.out.printf("\t%s is opening valve %s%n", p, valveToOpen);
                        system.open(valveToOpen);
                        valvesOpenedThisMinute.add(valveToOpen);
                        newTravellers[p] = travellers[p];
                    } else {
                        travellersThatNeedPathSelectionGroupedByCurrentValve.computeIfAbsent(travellers[p].currentValve.id, k -> new LinkedHashSet<>()).add(p);
                    }
                }

                if (travellersThatNeedPathSelectionGroupedByCurrentValve.isEmpty()) {
                    path.add(new ValvePathNode(
                            system.currentTotalFlowRate,
                            previousPathNode.totalPressureRelieved + previousPathNode.totalFlowRate,
                            previousPathNode.valvesAvailableForSelection,
                            valvesOpenedThisMinute));
                    doMinute(minutesSpent + 1, newTravellers, maxMinutes);
                    rollback();
                } else {
                    List<Map<Integer, Valve>> valveSelections = generateVariations(
                            travellersThatNeedPathSelectionGroupedByCurrentValve.values().stream().flatMap(Collection::stream).collect(Collectors.toList()),
                            new ArrayList<>(previousPathNode.valvesAvailableForSelection),
                            new HashSet<>(travellersThatNeedPathSelectionGroupedByCurrentValve.values()));
                    System.out.println("\tAvailable for selection1: " + previousPathNode.valvesAvailableForSelection);
                    for (Map<Integer, Valve> valveSelection : valveSelections) {
                        Set<Valve> valvesAvailableForSelection = new HashSet<>(previousPathNode.valvesAvailableForSelection);
                        for (int p = 0; p < newTravellers.length; p++) {
                            if (newTravellers[p] == null) {
                                if (valveSelection.containsKey(p)) {
                                    Valve currentValve = travellers[p].currentValve;
                                    Valve valveToReach = valveSelection.get(p);
                                    newTravellers[p] = doMove(new Traveller(currentValve, currentValve.shortestPathByTargetValve.get(valveToReach.id)));
                                    valvesAvailableForSelection.remove(valveToReach);
                                    System.out.printf("\t%s selected target %s following path %s%n", p, valveToReach.id, currentValve.shortestPathByTargetValve.get(valveToReach.id));
                                } else {
                                    newTravellers[p] = travellers[p];
                                }
                            }
                        }
                        System.out.println("\tAvailable for selection: " + valvesAvailableForSelection);
                        path.add(new ValvePathNode(
                                system.currentTotalFlowRate,
                                previousPathNode.totalPressureRelieved + previousPathNode.totalFlowRate,
                                copyOf(valvesAvailableForSelection),
                                valvesOpenedThisMinute));
                        doMinute(minutesSpent + 1, newTravellers, maxMinutes);
                        rollback();
                    }
                }
            }
        }

        private List<Map<Integer, Valve>> generateVariations(List<Integer> indexes, List<Valve> valves, Set<Set<Integer>> sameNodeGroups) {
            LinkedHashSet<Map<Integer, Valve>> collector = new LinkedHashSet<>();
            generateVariations(indexes, valves, sameNodeGroups, Map.of(), collector);
            return new ArrayList<>(collector);
        }

        private void generateVariations(List<Integer> indexes, List<Valve> valves, Set<Set<Integer>> sameNodeGroups, Map<Integer, Valve> runningCombo, LinkedHashSet<Map<Integer, Valve>> collector) {
            if (indexes.isEmpty() || valves.isEmpty()) {
                boolean noDuplicateFound = sameNodeGroups.stream()
                        .noneMatch(sameNodeGroup -> runningCombo.keySet().containsAll(sameNodeGroup)
                                && collector.stream().anyMatch(combo -> combo.keySet().containsAll(sameNodeGroup) && haveSameValuesForKeysMatching(combo, runningCombo, sameNodeGroup::contains)));
                if (noDuplicateFound)
                    collector.add(runningCombo);
            } else
                indexes.forEach(index -> valves.forEach(valve ->
                        generateVariations(
                                copyWithoutElement(indexes, index),
                                copyWithoutElement(valves, valve),
                                sameNodeGroups,
                                comboWith(runningCombo, index, valve), collector)));
        }

        private <K, V> boolean haveSameValuesForKeysMatching(Map<K, V> firstMap, Map<K, V> secondMap, Predicate<K> keyPredicate) {
            return firstMap.keySet().stream().filter(keyPredicate).map(firstMap::get).collect(toSet())
                    .equals(secondMap.keySet().stream().filter(keyPredicate).map(secondMap::get).collect(toSet()));
        }

        private void rollback() {
            System.out.printf("== Rolling back to minute %s ==%n", path.size() - 2);
            path.removeLast().valvesOpened.forEach(system::close);
            System.out.println("\tAvailable for selection: " + path.getLast().valvesAvailableForSelection);
        }

        private Traveller doMove(Traveller traveller) {
            String nextValveId = traveller.pathToTargetValve.get(0);
            if (!traveller.currentValve.tunnels.contains(nextValveId))
                throw new RuntimeException();
//            System.out.printf("\tMove to %s, following path%s%n", nextValveId, pathToNextValveToOpen);
            return new Traveller(system.getValve(nextValveId), copyWithoutElement(traveller.pathToTargetValve, nextValveId));
        }

        @NotNull
        private static <T> Set<T> copyWithoutElement(Set<T> set, T element) {
            HashSet<T> copy = new HashSet<>(set);
            copy.remove(element);
            return copy;
        }

        @NotNull
        private static <T> List<T> copyWithoutElement(List<T> list, T element) {
            LinkedList<T> copy = new LinkedList<>(list);
            copy.remove(element);
            return copy;
        }

        private static Map<Integer, Valve> comboWith(Map<Integer, Valve> combo, Integer index, Valve valve) {
            HashMap<Integer, Valve> next = new HashMap<>(combo);
            next.put(index, valve);
            return Map.copyOf(next);
        }

        private record ValvePathNode(int totalFlowRate,
                                     int totalPressureRelieved,
                                     Set<Valve> valvesAvailableForSelection,
                                     Set<Valve> valvesOpened) {
            ValvePathNode(int totalFlowRate,
                          int totalPressureRelieved,
                          Set<Valve> valvesAvailableForSelection) {
                this(totalFlowRate, totalPressureRelieved, valvesAvailableForSelection, Set.of());
            }
        }

    }

    private static class ValvesAndTunnels {
        private final Map<String, Valve> valvesById;
        private final Set<Valve> valvesToOpen = new HashSet<>();
        private int currentTotalFlowRate = 0;

        private ValvesAndTunnels(Collection<Valve> valves) {
            valvesById = valves.stream()
                    .peek(v -> {
                        if (v.flowRate > 0)
                            valvesToOpen.add(v);
                    })
                    .collect(toMap(v -> v.id, identity()));
            valvesById.keySet().forEach(valveId -> {
                Valve startValve = valvesById.get(valveId);
                for (Valve targetValve : valvesToOpen) {
                    if (startValve != targetValve) {
                        AtomicReference<List<String>> shortestPathHolder = new AtomicReference<>();
                        getShortestPath(startValve, startValve, targetValve, List.of(), shortestPathHolder, valvesById);
                        startValve.setShortestPathTo(targetValve, shortestPathHolder.get());
                    }
                }
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

        public Valve getValve(String valveId) {
            return valvesById.get(valveId);
        }

        public Set<Valve> getValvesToOpen() {
            return copyOf(valvesToOpen);
        }

        public boolean allValvesAreOpen() {
            return valvesToOpen.isEmpty();
        }

        public void open(Valve valve) {
            valve.open = true;
            valvesToOpen.remove(valve);
            currentTotalFlowRate += valve.flowRate;
        }

        public void close(Valve valve) {
            valve.open = false;
            valvesToOpen.add(valve);
            currentTotalFlowRate -= valve.flowRate;
        }
    }

    private static class Valve {
        String id;
        final int flowRate;
        final Set<String> tunnels;
        Map<String, List<String>> shortestPathByTargetValve = new HashMap<>();

        private boolean open = false;

        Valve(String id, int flowRate, Set<String> tunnels) {
            this.id = id;
            this.flowRate = flowRate;
            this.tunnels = tunnels;
        }

        boolean canBeOpened() {
            return !open && flowRate > 0;
        }

        @Override
        public String toString() {
            return id + " open=" + open + " -> " + tunnels;
        }

        void setShortestPathTo(Valve valve, List<String> path) {
            shortestPathByTargetValve.put(valve.id, List.copyOf(path));
        }
    }

    private record Traveller(Valve currentValve, List<String> pathToTargetValve) {
        public boolean isTravelling() {
            return !pathToTargetValve.isEmpty();
        }
    }
}
