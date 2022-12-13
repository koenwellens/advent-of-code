import {input as example} from './example-input';
import {input} from './input';

const ALL_HEIGHTS = 'SabcdefghijklmnopqrstuvwxyzE'.split('');
const allHeights = ALL_HEIGHTS.reduce((res, pos, idx) => ({...res, [pos]: idx}), {});
const nextPosition = pos => ALL_HEIGHTS[ALL_HEIGHTS.indexOf(pos) + 1];
const previousPosition = pos => ALL_HEIGHTS[ALL_HEIGHTS.indexOf(pos) - 1];
const START = 'S';
const END = 'E';

const computePositionOf = (rows, elem) => {
    return rows.reduce((res, row, y) => {
        const x = row.indexOf(elem);
        if (x >= 0) {
            return {x, y};
        }
        return res;
    }, {x: -1, y: -1});
}

const numberOfSteps = path => path?.length - 1;

const UNREACHABLE = {difference: 500, obj: null};

const computeNode = (rows, prevPosition, x, y) => {
    if (x < 0 || y < 0 || y >= rows.length || x >= rows[y].length) {
        return UNREACHABLE;
    }

    const position = rows[y][x];
    return {
        difference: allHeights[position] - allHeights[prevPosition],
        obj: {x, y, position}
    };
}

const computeSurroundingHeights = (rows, x, y, position) => {
    return {
        x,
        y,
        position,
        L: computeNode(rows, position, x - 1, y),
        R: computeNode(rows, position, x + 1, y),
        U: computeNode(rows, position, x, y - 1),
        D: computeNode(rows, position, x, y + 1),
    };
}

const computeBoardWithSurroundings = (rows) => rows.map((arr, y) => arr.map((pos, x) => computeSurroundingHeights(rows, x, y, pos)));
const contains = (path, {x: objX, y: objY}) => path.some(({x, y}) => x === objX && y === objY);

const computeNewPath = (currentPath, element, direction, noUseNodes, differenceShouldBe = 1) => {
    if (element[direction].difference === 0 || element[direction].difference === differenceShouldBe) {
        if (!contains(currentPath, element[direction].obj) && !contains(noUseNodes, element[direction].obj)) {
            return [...currentPath, element[direction].obj];
        }
    }

    return [];
}

const computePath = (board, start, endPosition) => {
    const differenceShouldBe = endPosition === END ? 1 : -1;
    let allPaths = [[start]];
    const noUseNodes = [];
    while (allPaths.length > 0) {
        const [currentPath, ...nextPaths] = allPaths;
        const currentElement = currentPath[currentPath.length - 1];
        const currentBoardElement = board[currentElement.y][currentElement.x];

        if (currentBoardElement.position === endPosition) {
            return currentPath;
        } else if (contains(noUseNodes, currentElement)) {
            allPaths = nextPaths;
        } else {
            const newPaths = ['L', 'R', 'D', 'U']
                .map(direction => computeNewPath(currentPath, currentBoardElement, direction, noUseNodes, differenceShouldBe))
                .filter(arr => arr.length > 0)
                .sort((a, b) => a.length - b.length);

            let preComputedPaths = [];
            let shortestLength = 999999999;
            for (let idx = 0; idx < newPaths.length; idx++) {
                const currentNewPath = newPaths[idx];
                const {x, y, position} = currentNewPath[currentNewPath.length - 1];
                if(position === currentElement.position && board[y][x].computedPath?.length > 0) {
                    preComputedPaths.push(currentNewPath);
                    shortestLength = currentNewPath.length + board[y][x].computedPath.length - 1;
                }

                if (currentNewPath[currentNewPath.length - 1].position === endPosition && currentNewPath.length < shortestLength) {
                    return currentNewPath;
                }
            }

            if(preComputedPaths.length > 0) {
                const shortestPath = preComputedPaths.sort((a, b) => a.length - b.length)[0];
                const [firstElement, ...computedPath] = board[shortestPath[shortestPath.length - 1].y][shortestPath[shortestPath.length - 1].x].computedPath;
                return [
                    ...shortestPath,
                    ...computedPath,
                ];
            }

            if (newPaths.length === 0) {
                noUseNodes.push(currentElement);
            }

            allPaths = [...nextPaths.filter(path => {
                const {x, y} = path[path.length - 1];
                return x !== currentElement.x || y !== currentElement.y;
            }), ...newPaths,].sort((a, b) => a.length - b.length);
        }
    }

    return [];
}

const computeExtendedBoard = (board, start, end) => {
    for (let y = 0; y < board.length; y++) {
        for (let x = 0; x < board[y].length; x++) {
            const element = board[y][x];

            if (element.position !== START) {
                const previous = previousPosition(element.position);
                console.log(`Computing for ${x},${y}: from ${element.position} to ${previous}`);
                element.computedPath = computePath(board, element, previous);
                let computedLength = element.computedPath.length;
                const lastElement = computedLength > 0 ? {x: element.computedPath[computedLength - 1].x, y: element.computedPath[computedLength - 1].y} : {x: -1, y: -1};
                console.log(` Computed for ${x},${y}: from ${element.position} to ${previous}: ${computedLength} to (${lastElement.x}, ${lastElement.y})`);
            }
        }
    }

    let fullPath = [];
    let current = end;
    while (current.position !== start.position) {
        const element = board[current.y][current.x];
        fullPath = [...fullPath, element.computedPath.map(({x, y, position}) => ({x, y, position}))];
        console.log('Computed path length', element.computedPath.length);
        current = fullPath[fullPath.length - 1];
    }

    return fullPath;
}

const exampleRows = example.split('\n').map(arr => arr.split(''));
const exampleBoard = computeBoardWithSurroundings(exampleRows);
// const examplePathFromBoard = computePaths(exampleBoard, {
//     ...computePositionOf(exampleRows, START),
//     position: START,
// }, END)[0];
// console.log('Example 1:', numberOfSteps(examplePathFromBoard));

const inputRows = input.split('\n').map(arr => arr.split(''));
const inputBoard = computeBoardWithSurroundings(inputRows);
const extendedBoard = computeExtendedBoard(inputBoard, {
    ...computePositionOf(inputRows, START),
    position: START,
}, {
    ...computePositionOf(inputRows, END),
    position: END,
});

console.log("Say what?! Did we make it???");
console.log('extended board got calculated!!', extendedBoard);
// const inputPathFromBoard = computePaths(inputBoard, {
//     ...computePositionOf(inputRows, END),
//     position: END,
// }, START);
console.log("Say what? Did we make it???");
// console.log(inputPathFromBoard.map(arr => arr.length - 1));
// console.log('Solution 1:', numberOfSteps(inputPathFromBoard))
