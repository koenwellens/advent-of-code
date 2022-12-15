import {input as example} from './example-input';
import {input} from './input';

const SAND_START = [500, 0];
const SAND = 'O';
const ROCK = '#';
const AIR = '.';

const parse = str => str.split('\n').map(path => path.split(' -> ').map(path => path.split(',')))

const findLowestX = (parsedInput, [startX]: number[]) => parsedInput.reduce((res, row) => row.reduce((r, [x]) => r < x ? r : x, res), startX);
const findLowestY = (parsedInput, [, startY]: number[]) => parsedInput.reduce((res, row) => row.reduce((r, [, y]) => r < y ? r : y, res), startY);
const findHighestX = (parsedInput, [startX]: number[]) => parsedInput.reduce((res, row) => row.reduce((r, [x]) => r > x ? r : x, res), startX);
const findHighestY = (parsedInput, [, startY]: number[]) => parsedInput.reduce((res, row) => row.reduce((r, [, y]) => r > y ? r : y, res), startY);

const normalize = (parsedInput: number[][][], [startX, startY]: number[], [lowestX, lowestY]: number[], withFloor = false) => {
    const floorExtension = 300;
    const lowX = withFloor ? lowestX - floorExtension : lowestX;
    const paths = parsedInput.map(path => path.map(([x, y]) => [x - lowX, y - lowestY]));
    const start = [startX - lowX, startY - lowestY];
    return {
        paths,
        start,
        lowest: [0, 0],
        highest: [findHighestX(paths, start) + (withFloor ? floorExtension : 0), findHighestY(paths, start) + (withFloor ? 2 : 0)],
    };
}

const fillInHorizontally = (map, fromX, toX, y, val) => {
    for (let drawX = fromX; drawX <= toX; drawX++) {
        map[y][drawX] = val;
    }
}
const fillInVertically = (map, x, fromY, toY, val) => {
    for (let drawY = fromY; drawY <= toY; drawY++) {
        map[drawY][x] = val;
    }
}

const drawEmptyMap = ([lowestX, lowestY]: number[], [highestX, highestY]: number[]) => {
    const map = new Array(highestY);
    for (let y = lowestY; y <= highestY; y++) {
        map[y] = new Array(highestX);
        fillInHorizontally(map, lowestX, highestX, y, AIR);
    }
    return map;
}

const fillInRocks = (map, rockPaths) => {
    rockPaths.forEach(path => {
        for (let idx = 0; idx < path.length - 1; idx++) {
            const [currentX, currentY] = path[idx];
            const [nextX, nextY] = path[idx + 1];
            if (currentX !== nextX) {
                fillInHorizontally(map, currentX, nextX, currentY, ROCK);
                fillInHorizontally(map, nextX, currentX, currentY, ROCK);
            }

            if (currentY !== nextY) {
                fillInVertically(map, currentX, currentY, nextY, ROCK);
                fillInVertically(map, currentX, nextY, currentY, ROCK);
            }
        }
    });
}

const copy = map => [...map.map(row => [...row])];
const toString = map => map.map(row => row.join('  ')).join('\n')
const isWithinBoundaries = (map, [x, y]: number[]) => x > -1 && y > -1 && y < map.length && x < map[y].length;

const drawSand = (map, [x, y]: number[]) => {
    if (isWithinBoundaries(map, [x, y]) && map[y][x] === AIR) {
        map[y][x] = SAND;
    }
}

const dropSand = (map, [sandX, sandY]: number[]) => {
    if (isWithinBoundaries(map, [sandX, sandY])) {
        const current = map[sandY][sandX];

        if (current !== AIR) {
            // if left from this is filled and right from this is filled, move it to the top
            const leftIsAlreadyFilled = isWithinBoundaries(map, [sandX - 1, sandY]) && map[sandY][sandX - 1] !== AIR;
            const rightIsAlreadyFilled = isWithinBoundaries(map, [sandX + 1, sandY]) && map[sandY][sandX + 1] !== AIR;
            if (leftIsAlreadyFilled && rightIsAlreadyFilled) {
                drawSand(map, [sandX, sandY - 1]);
            } else if (leftIsAlreadyFilled) {
                dropSand(map, [sandX + 1, sandY]);
            } else {
                dropSand(map, [sandX - 1, sandY]);
            }
        } else {
            dropSand(map, [sandX, sandY + 1]);
        }
    }
}

const watchTimePass = (map, [sandX, sandY]: number[]) => {
    let previousMap = [];
    let currentMap = map;
    while (toString(currentMap) !== toString(previousMap)) {
        previousMap = currentMap;
        currentMap = copy(currentMap);

        if (currentMap[sandY][sandX] !== SAND) {
            dropSand(currentMap, [sandX, sandY]);
        }
    }

    return currentMap;
}

const count = (map, val) => map.reduce((res, row) => res + row.reduce((r, s) => s === val ? r + 1 : r, 0), 0)

const parsedExample = parse(example);
const lowestExampleX = findLowestX(parsedExample, SAND_START);
const lowestExampleY = findLowestY(parsedExample, SAND_START);
const {
    lowest: examplePathLowest,
    highest: examplePathHighest,
    paths: examplePaths,
    start: exampleStart
} = normalize(parsedExample, SAND_START, [lowestExampleX, lowestExampleY]);
const exampleMap = drawEmptyMap(examplePathLowest, examplePathHighest);
fillInRocks(exampleMap, examplePaths);
const exampleMapAfterwards = watchTimePass(exampleMap, exampleStart);
console.log('Example 1:', count(exampleMapAfterwards, SAND));

const parsedInput = parse(input);
const lowestInputX = findLowestX(parsedInput, SAND_START);
const lowestInputY = findLowestY(parsedInput, SAND_START);
const {
    lowest: inputPathLowest,
    highest: inputPathHighest,
    paths: inputPaths,
    start: inputStart
} = normalize(parsedInput, SAND_START, [lowestInputX, lowestInputY]);
const inputMap = drawEmptyMap(inputPathLowest, inputPathHighest);
fillInRocks(inputMap, inputPaths);
const inputMapAfterwards = watchTimePass(inputMap, inputStart);
console.log('Solution 1:', count(inputMapAfterwards, SAND));

const {
    lowest: examplePathWithFloorLowest,
    highest: examplePathWithFloorHighest,
    paths: examplePathsWithFloor,
    start: exampleStartWithFloor
} = normalize(parsedExample, SAND_START, [lowestExampleX, lowestExampleY], true);
const exampleCave = drawEmptyMap(examplePathWithFloorLowest, examplePathWithFloorHighest);
fillInRocks(exampleCave, [...examplePathsWithFloor, [[examplePathWithFloorLowest[0], examplePathWithFloorHighest[1]], examplePathWithFloorHighest]]);
const exampleCaveAfterwards = watchTimePass(exampleCave, exampleStartWithFloor);
console.log('Example 2:', count(exampleCaveAfterwards, SAND));

const {
    lowest: inputPathWithFloorLowest,
    highest: inputPathWithFloorHighest,
    paths: inputPathsWithFloor,
    start: inputStartWithFloor
} = normalize(parsedInput, SAND_START, [lowestInputX, lowestInputY], true);
const inputCave = drawEmptyMap(inputPathWithFloorLowest, inputPathWithFloorHighest);
fillInRocks(inputCave, [...inputPathsWithFloor, [[inputPathWithFloorLowest[0], inputPathWithFloorHighest[1]], inputPathWithFloorHighest]]);
const inputCaveAfterwards = watchTimePass(inputCave, inputStartWithFloor);
console.log('Solution 2:', count(inputCaveAfterwards, SAND));
