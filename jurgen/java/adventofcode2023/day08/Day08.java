package adventofcode2023.day08;

import adventofcode2023.common.Input;
import org.jetbrains.annotations.NotNull;

import java.util.*;
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

                // this naive implementation runs for too long, no answer found yet
                // stopped run at: [CGH, SDJ, NCX, MMD, GFJ, GJZ] #24026405617
//                .execute("Puzzle 2", Day08::numberOfStepsToReachAllEndNodes).withInput(INPUT).expect(-1L)
                .execute("Puzzle 2 inspect", Day08::numberOfStepsToReachAllEndNodesInspected).withInput(INPUT).expect(-1L)
                .build()
                .run();
    }

    private static Long numberOfStepsToReachEnd(Input input) {
        String[] inputLines = input.lines();
        List<Instruction> movementInstructions = parseMovementInstructions(inputLines);
        Map<String, Node> nodePathsMap = parseNodePaths(inputLines);

        String currentNode = "AAA";
        long steps = 0;
        while (!"ZZZ".equals(currentNode)) {
            Instruction movementInstruction = movementInstructions.get((int) (steps % movementInstructions.size()));
            currentNode = movementInstruction.execute(nodePathsMap.get(currentNode));
            steps++;
        }

        return steps;
    }

    private static Long numberOfStepsToReachAllEndNodes(Input input) {
        String[] inputLines = input.lines();
        List<Instruction> movementInstructions = parseMovementInstructions(inputLines);
        Map<String, Node> nodePathsMap = parseNodePaths(inputLines);

        String[] currentNodes = nodePathsMap.keySet().stream().filter(node -> node.endsWith("A")).toArray(String[]::new);
        long steps = 0;
        while (!Arrays.stream(currentNodes).allMatch(node -> node.endsWith("Z"))) {
//            if (Arrays.stream(currentNodes).anyMatch(node -> node.endsWith("Z")))
//                System.out.println(Arrays.toString(currentNodes) + " #" + steps);
            Instruction movementInstruction = movementInstructions.get((int) (steps % movementInstructions.size()));
            for (int nr = 0; nr < currentNodes.length; nr++) {
                currentNodes[nr] = movementInstruction.execute(nodePathsMap.get(currentNodes[nr]));
            }
            steps++;
        }

        return steps;
    }

    record EndNodeReached(String node, Instruction instruction) {
    }

    private static Long numberOfStepsToReachAllEndNodesInspected(Input input) {
        String[] inputLines = input.lines();
        List<Instruction> movementInstructions = parseMovementInstructions(inputLines);
        Map<String, Node> nodePathsMap = parseNodePaths(inputLines);

        String[] currentNodes = nodePathsMap.keySet().stream().filter(node -> node.endsWith("A")).toArray(String[]::new);

        for (String currentStartNode : currentNodes) {
            System.out.println("========= " + currentStartNode + " =========");
            String currentNode = currentStartNode;
            Set<EndNodeReached> endNodesReached = new LinkedHashSet<>();
            boolean alreadyAdded = false;
            long steps = 0;
            long successStep = 0;
            Instruction movementInstruction = null;
            do {
                do {
                    int instructionIndex = (int) (steps % movementInstructions.size());
                    movementInstruction = movementInstructions.get(instructionIndex);
                    currentNode = movementInstruction.execute(nodePathsMap.get(currentNode));
                    steps++;
                } while (!currentNode.endsWith("Z"));
                System.out.println(successStep + "::" + (steps - successStep));
                successStep = steps;
                alreadyAdded = !endNodesReached.add(new EndNodeReached(currentNode, movementInstruction));
            }
            while (steps < 100000);

            endNodesReached.forEach(System.out::println);

        }


        return 0L;
    }

    record Instruction(int index, Function<Node, String> action) {
        String execute(Node node) {
            return action.apply(node);
        }
    }

    @NotNull
    private static List<Instruction> parseMovementInstructions(String[] inputLines) {
        List<Instruction> list = new ArrayList<>();
        String[] split = inputLines[0].split("");
        for (int index = 0; index < split.length; index++) {
            String instr = split[index];
            Function<Node, String> nodeStringFunction = "R".equals(instr) ? Node::rightPath : Node::leftPath;
            list.add(new Instruction(index, nodeStringFunction));
        }
        return list;
    }

    @NotNull
    private static Map<String, Node> parseNodePaths(String[] inputLines) {
        Map<String, Node> nodePathsMap = new HashMap<>();
        for (int i = 2; i < inputLines.length; i++) {
            Node node = new Node(
                    inputLines[i].substring(0, 3),
                    inputLines[i].substring(7, 10),
                    inputLines[i].substring(12, 15));
            nodePathsMap.put(node.node(), node);
        }
        return nodePathsMap;
    }


}
