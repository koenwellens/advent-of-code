import {readFile} from '../common/readFile';

const example1 = readFile(10, 'example1');
const example3 = readFile(10, 'example3');
const input1 = readFile(10, 'input1');

const computePipe = (lines, x, y) => {
    const goesSouth = y < lines.length && ['J', 'L', '|'].includes(lines[y + 1][x]);
    const goesNorth = y > 0 && ['F', '7', '|'].includes(lines[y - 1][x]);
    const goesEast = x < lines[y].length && ['-', '7', 'J'].includes(lines[y][x + 1]);
    const goesWest = x > 0 && ['-', 'F', 'L'].includes(lines[y][x - 1]);

    if (goesSouth) {
        if (goesEast) {
            return 'F';
        }

        if (goesWest) {
            return '7';
        }

        return '|';
    }

    if (goesNorth) {
        if (goesEast) {
            return 'L';
        }

        if (goesWest) {
            return 'J';
        }

        return '|';
    }

    return '-';
}

const findS = lines => {
    for (let y = 0; y < lines.length; y++) {
        for (let x = 0; x < lines[y].length; x++) {
            if (lines[y][x] === 'S') {
                const pipe = computePipe(lines, x, y);
                return {x, y, pipe,};
            }
        }
    }

    throw new Error("No S was found!");
}

const createResultArray = (x, y, maxX, maxY) => {
    const result = [];
    for (let myY = 0; myY < maxY; myY++) {
        const array = new Array(maxX).fill('.');
        result.push(array);
        for (let myX = 0; myX < maxX; myX++) {
            if (myX === x && myY === y) {
                result[y][x] = 0;
            }
        }
    }

    return result;
}

const findHighestNumber = doubleArr => doubleArr.reduce((res, arr) => {
    const value = arr.reduce((r, num) => isNaN(+num) || r > +num ? r : +num, 0);
    return res > value ? res : value;
}, 0);

const computeDistancesFromStartingPoints = (lines) => {
    const startingPoint = findS(lines);
    const result = createResultArray(startingPoint.x, startingPoint.y, lines[0].length, lines.length);

    let stillRunning = true;
    while (stillRunning) {
        // console.log(startingPoint, result);
        stillRunning = false;
        for (let y = 0; y < lines.length; y++) {
            for (let x = 0; x < lines[y].length; x++) {
                const number = +result[y][x];
                if (!isNaN(number)) {
                    const pipe = startingPoint.x === x && startingPoint.y === y ? startingPoint.pipe : lines[y][x];
                    if (x > 0 && isNaN(+result[y][x - 1]) && ['-', '7', 'J'].includes(pipe)) {
                        stillRunning = true;
                        result[y][x - 1] = number + 1;
                    }

                    if (x < result[y].length - 1 && isNaN(+result[y][x + 1]) && ['-', 'F', 'L'].includes(pipe)) {
                        stillRunning = true;
                        result[y][x + 1] = number + 1;
                    }

                    if (y > 0 && isNaN(+result[y - 1][x]) && ['|', 'J', 'L'].includes(pipe)) {
                        stillRunning = true;
                        result[y - 1][x] = number + 1;
                    }

                    if (y < result.length - 1 && isNaN(+result[y + 1][x]) && ['|', '7', 'F'].includes(pipe)) {
                        stillRunning = true;
                        result[y + 1][x] = number + 1;
                    }
                }
            }
        }
    }

    return result;
}

const computeStepsFromFarthestPoint = lines => {
    const result = computeDistancesFromStartingPoints(lines);
    // console.log(result);
    return findHighestNumber(result);
}

const enclosedByLeft = (mappedPipes, x, y) => {
    if (x === 0) {
        return false;
    }

    if (!mappedPipes[y]) {
        console.log(y, x, x - 1);
    }
    if (isNaN(+mappedPipes[y][x - 1])) {
        return true;
    }

    return enclosedByLeft(mappedPipes, x - 1, y) && enclosedByTop(mappedPipes, x - 1, y) && enclosedByBottom(mappedPipes, x - 1, y);
}

const enclosedByRight = (mappedPipes, x, y) => {
    if (x === mappedPipes[y].length - 1) {
        return false;
    }

    if (isNaN(+mappedPipes[y][x + 1])) {
        return true;
    }

    return enclosedByRight(mappedPipes, x + 1, y) && enclosedByTop(mappedPipes, x + 1, y) && enclosedByBottom(mappedPipes, x + 1, y);
}

const enclosedByBottom = (mappedPipes, x, y) => {
    if (y === mappedPipes.length - 1) {
        return false;
    }

    if (isNaN(+mappedPipes[y + 1][x])) {
        return true;
    }

    return enclosedByLeft(mappedPipes, x, y + 1) && enclosedByRight(mappedPipes, x, y + 1) && enclosedByBottom(mappedPipes, x, y + 1);
}

const enclosedByTop = (mappedPipes, x, y) => {
    if (y === 0) {
        return false;
    }

    if (isNaN(+mappedPipes[y - 1][x])) {
        return true;
    }

    return enclosedByTop(mappedPipes, x, y - 1) && enclosedByRight(mappedPipes, x, y - 1) && enclosedByLeft(mappedPipes, x, y - 1);
}

const isOutsideOfTheLoop = (mappedPipes, x, y) => {
    let left = 0;
    for (let l = 0; l < x; l++) {
        if (!isNaN(+mappedPipes[y][l])) {
            left++;
        }
    }

    let right = 0;
    for (let r = x + 1; r < mappedPipes[y].length; r++) {
        if (!isNaN(+mappedPipes[y][r])) {
            right++;
        }
    }

    let top = 0;
    for (let t = 0; t < y; t++) {
        if (!isNaN(+mappedPipes[t][x])) {
            top++;
        }
    }

    let bottom = 0;
    for (let b = y + 1; b < mappedPipes.length; b++) {
        if (!isNaN(+mappedPipes[b][x])) {
            bottom++;
        }
    }
    return left % 2 === 0 && right % 2 === 0 && top % 2 === 0 && bottom % 2 === 0;
}

const computeNumberOfEnclosedTiles = lines => {
    const mappedPipes = computeDistancesFromStartingPoints(lines);

    for (let r of mappedPipes) {
        console.log(r.join("  "));
    }

    let stillRunning = true;
    while (stillRunning) {
        // console.log(startingPoint, result);
        stillRunning = false;
        for (let y = 0; y < lines.length; y++) {
            for (let x = 0; x < lines[y].length; x++) {
                const number = +mappedPipes[y][x];
                if (isNaN(number) && 'O' !== mappedPipes[y][x]) {
                    if (x === 0 || y === 0 || y === mappedPipes.length - 1 || x === mappedPipes[y].length - 1) {
                        stillRunning = true;
                        mappedPipes[y][x] = 'O';
                    }

                    if (x > 0 && mappedPipes[y][x - 1] === 'O') {
                        stillRunning = true;
                        mappedPipes[y][x] = 'O';
                    }
                    if (x < mappedPipes[y].length - 1 && mappedPipes[y][x + 1] === 'O') {
                        stillRunning = true;
                        mappedPipes[y][x] = 'O';
                    }
                    if (y > 0 && mappedPipes[y - 1][x] === 'O') {
                        stillRunning = true;
                        mappedPipes[y][x] = 'O';
                    }
                    if (y < mappedPipes.length - 1 && mappedPipes[y + 1][x] === 'O') {
                        stillRunning = true;
                        mappedPipes[y][x] = 'O';
                    }
                }
            }
        }
    }

    for (let y = 0; y < lines.length; y++) {
        for (let x = 0; x < lines[y].length; x++) {
            if (mappedPipes[y][x] === '.' && isOutsideOfTheLoop(mappedPipes, x, y)) {
                mappedPipes[y][x] = 'O';
            }
        }
    }

    for (let r of mappedPipes) {
        // console.log(r.replace('\d', 'P');
        console.log(r.map(s => ("" + s).replace(/\d+/g, 'P')).join(""));
    }

    return mappedPipes.reduce((res, arr) => res + arr.filter(s => s === '.').length, 0);
}

console.log(computeStepsFromFarthestPoint(example1)); // 4
console.log(computeStepsFromFarthestPoint(example3)); // 8
console.log(computeStepsFromFarthestPoint(input1)); // 6897

const example2 = readFile(10, 'example2');
const example4 = readFile(10, 'example4');
const example5 = readFile(10, 'example5');

console.log(computeNumberOfEnclosedTiles(example2)); // 4
// console.log(computeNumberOfEnclosedTiles(example4)); // 8
console.log(computeNumberOfEnclosedTiles(example5)); // 10