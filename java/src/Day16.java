import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class Day16 implements DaySolution<Integer> {

    @Override
    public Integer part1(Path inputFilePath) {
        try (Stream<String> lines = lines(inputFilePath)) {
            List<Valve> valves = lines
                    .map(this::parseValveData)
                    .toList();
            ValvesAndTunnels system = new ValvesAndTunnels(valves);
            ValvePathWalker valvePathWalker = new ValvePathWalker(system);
            valvePathWalker.startWalking("AA");

            System.out.println("Path ");

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

        void startWalking(String initialValveId) {
            Valve initialValve = system.getValve(initialValveId);
            path.add(new ValvePathNode(0, 0));
            doSomething(0, new Traveller(initialValve, List.of()));
        }

        private void doSomething(int minutesSpent, Traveller traveller) {
            ValvePathNode previousPathNode = path.getLast();
            if (minutesSpent == 30) {
//                System.out.println("== After Minute 30 ==");
//                System.out.printf("\tPressure released in current path is %s%n", previousPathNode.totalPressureRelieved);
                maxPressureRelieved = max(maxPressureRelieved, previousPathNode.totalPressureRelieved);
                return;
            }

//            System.out.printf("== Minute %s ==%n", minutesSpent + 1);
//            System.out.printf("\tAt %s%n", currentValve);
//            System.out.printf("\tValves [%s] are open, releasing %s pressure, total released is %s%n",
//                    valvesById.values().stream().filter(Valve::isOpen).map(v -> v.id).sorted().collect(joining(", ")),
//                    previousPathNode.totalFlowRate,
//                    previousPathNode.totalPressureRelieved
//            );

            if (traveller.isTravelling()) {
                Traveller newtraveller = doMove(minutesSpent, traveller, previousPathNode);
                doSomething(minutesSpent + 1, newtraveller);
                rollback();
            } else if (system.allValvesAreOpen()) {
                path.add(new ValvePathNode(
                        previousPathNode.totalFlowRate,
                        previousPathNode.totalPressureRelieved + previousPathNode.totalFlowRate));
                doSomething(minutesSpent + 1, new Traveller(traveller.currentValve, List.of()));
//                System.out.printf("== Rolling back to minute %s ==%n", minutesSpent);
                rollback();
            } else if (traveller.currentValve.canBeOpened()) {
//                System.out.printf("\tOpening %s (flowrate %s)%n", currentValve.id, currentValve.flowRate);
                system.open(traveller.currentValve);
                path.add(new ValvePathNode(
                        previousPathNode.totalFlowRate + traveller.currentValve.flowRate,
                        previousPathNode.totalPressureRelieved + previousPathNode.totalFlowRate,
                        Set.of(traveller.currentValve))
                );
                doSomething(minutesSpent + 1, new Traveller(traveller.currentValve, List.of()));
//                System.out.printf("== Rolling back to minute %s ==%n", minutesSpent);
                rollback();
            } else {
                Set.copyOf(system.valvesToOpen)
                        .forEach(nextValveToOpen -> {
//                            System.out.printf("\tSelected %s as next valve to open%n", nextValveToOpen.id);
                            List<String> pathToNextValveToOpen = traveller.currentValve.shortestPathByTargetValve.get(nextValveToOpen.id);
                            Traveller newtraveller = doMove(minutesSpent, new Traveller(traveller.currentValve, pathToNextValveToOpen), previousPathNode);
                            doSomething(minutesSpent + 1, newtraveller);
                            rollback();
                        });
            }
        }

        private void rollback() {
            //            System.out.printf("== Rolling back to minute %s ==%n", minutesSpent);
            path.removeLast().valvesOpened.forEach(system::close);
        }

        private Traveller doMove(int minutesSpent, Traveller traveller, ValvePathNode previousPathNode) {
            String nextValveId = traveller.pathToTargetValve.get(0);
            if (!traveller.currentValve.tunnels.contains(nextValveId))
                throw new RuntimeException();
//            System.out.printf("\tMove to %s, following path%s%n", nextValveId, pathToNextValveToOpen);
            path.add(new ValvePathNode(
                    previousPathNode.totalFlowRate,
                    previousPathNode.totalPressureRelieved + previousPathNode.totalFlowRate));
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

        private record ValvePathNode(int totalFlowRate,

                                     int totalPressureRelieved,
                                     Set<Valve> valvesOpened) {
            ValvePathNode(int totalFlowRate,
                          int totalPressureRelieved) {
                this(totalFlowRate, totalPressureRelieved, Set.of());
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
            return Set.copyOf(valvesToOpen);
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
