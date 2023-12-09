package adventofcode2023.day08;

import adventofcode2023.common.Input;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static adventofcode2023.common.runner.Runner.runnerFor;
import static adventofcode2023.day08.Day08Input.*;

class Day08 {
    public static void main(String[] args) {
        runnerFor(Day08.class)
                .execute("Puzzle 1", Day08::numberOfStepsToReachEnd).withInput(EXAMPLE_1A).expect(2L)
                .execute("Puzzle 1", Day08::numberOfStepsToReachEnd).withInput(EXAMPLE_1B).expect(6L)
                .execute("Puzzle 1", Day08::numberOfStepsToReachEnd).withInput(INPUT).expect(15871L)
                .execute("Puzzle 2", Day08::numberOfStepsToReachAllEndNodes).withInput(EXAMPLE_2).expect(6L)
                .execute("Puzzle 2", Day08::numberOfStepsToReachAllEndNodes).withInput(INPUT).expect(-1L) // this naive implementation runs for too long, no answer found yet
                .build()
                .run();
    }

    private static Long numberOfStepsToReachEnd(Input input) {
        String[] inputLines = input.lines();
        List<Function<NodePaths, String>> movementInstructions = parseMovementInstructions(inputLines);
        Map<String, NodePaths> nodePathsMap = parseNodePaths(inputLines);

        String currentNode = "AAA";
        long steps = 0;
        while (!"ZZZ".equals(currentNode)) {
            Function<NodePaths, String> movementInstruction = movementInstructions.get((int) (steps % movementInstructions.size()));
            currentNode = movementInstruction.apply(nodePathsMap.get(currentNode));
            steps++;
        }

        return steps;
    }

    private static Long numberOfStepsToReachAllEndNodes(Input input) {
        String[] inputLines = input.lines();
        List<Function<NodePaths, String>> movementInstructions = parseMovementInstructions(inputLines);
        Map<String, NodePaths> nodePathsMap = parseNodePaths(inputLines);

        String[] currentNodes = nodePathsMap.keySet().stream().filter(node -> node.endsWith("A")).toArray(String[]::new);
        long steps = 0;
        while (!Arrays.stream(currentNodes).allMatch(node -> node.endsWith("Z"))) {
            if (Arrays.stream(currentNodes).anyMatch(node -> node.endsWith("Z")))
                System.out.println(Arrays.toString(currentNodes) + " #" + steps);
            Function<NodePaths, String> movementInstruction = movementInstructions.get((int) (steps % movementInstructions.size()));
            for (int nr = 0; nr < currentNodes.length; nr++) {
                currentNodes[nr] = movementInstruction.apply(nodePathsMap.get(currentNodes[nr]));
            }
            steps++;
        }

        return steps;
    }

    @NotNull
    private static List<Function<NodePaths, String>> parseMovementInstructions(String[] inputLines) {
        return Arrays.stream(inputLines[0].split(""))
                .map(instr -> "R".equals(instr) ? (Function<NodePaths, String>) NodePaths::rightPath : (Function<NodePaths, String>) NodePaths::leftPath)
                .toList();
    }

    @NotNull
    private static Map<String, NodePaths> parseNodePaths(String[] inputLines) {
        Map<String, NodePaths> nodePathsMap = new HashMap<>();
        for (int i = 2; i < inputLines.length; i++) {
            NodePaths nodePaths = new NodePaths(
                    inputLines[i].substring(0, 3),
                    inputLines[i].substring(7, 10),
                    inputLines[i].substring(12, 15));
            nodePathsMap.put(nodePaths.node(), nodePaths);
        }
        return nodePathsMap;
    }


}
