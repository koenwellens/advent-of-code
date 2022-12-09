import {input as exampleInput} from "./example-input";
import {input} from "./input";

enum Direction {
    LEFT = 'LEFT', RIGHT = 'RIGHT', TOP = 'TOP', BOTTOM = 'BOTTOM',
}

const directionalData = {
    [Direction.LEFT]: {
        edgeIndex: (forest, xIndex, yIndex) => xIndex === 0,
        startIndex: (xIndex) => xIndex - 1,
        stopCondition: (x) => x >= 0,
        indexChanger: (x) => x - 1,
        findTreesToCheck: (forest, xIndex, yIndex) => forest[yIndex].split(''),
        matchingTree: (treesToCheck, xIndex, yIndex) => treesToCheck[xIndex],
    },
    [Direction.RIGHT]: {
        edgeIndex: (forest, xIndex, yIndex) => xIndex === forest[yIndex].length - 1,
        startIndex: (xIndex) => xIndex + 1,
        stopCondition: (x, forest, yIndex) => x < forest[yIndex].length,
        indexChanger: (x) => x + 1,
        findTreesToCheck: (forest, xIndex, yIndex) => forest[yIndex].split(''),
        matchingTree: (treesToCheck, xIndex, yIndex) => treesToCheck[xIndex],
    },
    [Direction.TOP]: {
        edgeIndex: (forest, xIndex, yIndex) => yIndex === 0,
        startIndex: (xIndex, yIndex) => yIndex - 1,
        stopCondition: (y) => y >= 0,
        indexChanger: (y) => y - 1,
        findTreesToCheck: (forest, xIndex, yIndex) => forest.map(trees => trees.charAt(xIndex)),
        matchingTree: (treesToCheck, xIndex, yIndex) => treesToCheck[yIndex],
    },
    [Direction.BOTTOM]: {
        edgeIndex: (forest, xIndex, yIndex) => yIndex === forest.length - 1,
        startIndex: (xIndex, yIndex) => yIndex + 1,
        stopCondition: (y, forest) => y < forest.length,
        indexChanger: (y) => y + 1,
        findTreesToCheck: (forest, xIndex, yIndex) => forest.map(trees => trees.charAt(xIndex)),
        matchingTree: (treesToCheck, xIndex, yIndex) => treesToCheck[yIndex],
    },
};

const computeViewingDistance = (forest, xIndex, yIndex, direction, edgeDistance = 0) => {
    const {edgeIndex, startIndex, stopCondition, indexChanger, findTreesToCheck, matchingTree} = directionalData[direction];

    if (edgeIndex(forest, xIndex, yIndex)) {
        return 0;
    }

    const treesToCheck: string[] = findTreesToCheck(forest, xIndex, yIndex);
    let viewingDistance = edgeDistance;
    for (let index = startIndex(xIndex, yIndex); stopCondition(index, forest, yIndex); index = indexChanger(index)) {
        if(Number(treesToCheck[index]) >= Number(matchingTree(treesToCheck, xIndex, yIndex))) {
            return viewingDistance;
        }
        viewingDistance++;
    }

    return viewingDistance - edgeDistance;
}

const isVisible = (forest, xIndex, yIndex) => {
    return computeViewingDistance(forest, xIndex, yIndex, Direction.TOP) === yIndex ||
        computeViewingDistance(forest, xIndex, yIndex, Direction.RIGHT) === forest[yIndex].length - xIndex - 1 ||
        computeViewingDistance(forest, xIndex, yIndex, Direction.BOTTOM) === forest.length - yIndex - 1 ||
        computeViewingDistance(forest, xIndex, yIndex, Direction.LEFT) === xIndex;
}

const countNumberOfVisibleTrees = (forest) => {
    let numberOfVisibleTrees = 0;
    for (let yIndex = 0; yIndex < forest.length; yIndex++) {
        for(let xIndex = 0; xIndex < forest[yIndex].length; xIndex++) {
            if (isVisible(forest, xIndex, yIndex)) {
                numberOfVisibleTrees++;
            }
        }
    }

    return numberOfVisibleTrees;
}

console.log('Example 1: ', countNumberOfVisibleTrees(exampleInput.split('\n')));
console.log('Solution 1: ', countNumberOfVisibleTrees(input.split('\n')));

const computeScenicScore = (forest, xIndex, yIndex) => {
    return computeViewingDistance(forest, xIndex, yIndex, Direction.TOP, 1) *
        computeViewingDistance(forest, xIndex, yIndex, Direction.RIGHT, 1) *
        computeViewingDistance(forest, xIndex, yIndex, Direction.BOTTOM, 1) *
        computeViewingDistance(forest, xIndex, yIndex, Direction.LEFT, 1);
}

const highestScenicScore = (forest) => {
    let highestScenicScore = 0;
    for (let yIndex = 1; yIndex < forest.length - 1; yIndex++) {
        for(let xIndex = 1; xIndex < forest[yIndex].length - 1; xIndex++) {
            const scenicScore = computeScenicScore(forest, xIndex, yIndex);
            if (scenicScore > highestScenicScore) {
                highestScenicScore = scenicScore;
            }
        }
    }

    return highestScenicScore;
}

console.log('Example 2: ', highestScenicScore(exampleInput.split('\n')));
console.log('Solution 2: ', highestScenicScore(input.split('\n')));
