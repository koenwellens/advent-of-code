import {input as example} from './example-input';
import {input} from './input';

const START = 'S';
const END = 'E';
const MAX_VALUE = 9999999;
const LETTERS = {
    ...'abcdefghijklmnopqrstuvwxyz'.split('').reduce((res, val, idx) => ({...res, [val]: idx}), {}),
    [START]: 0,
    [END]: 25,
};
const toHeight = s => LETTERS[s];
const toLetterArray = str => str.split('\n').map(s => s.split(''));

const howToGoNext = {
    L: [-1, 0],
    R: [1, 0],
    U: [0, -1],
    D: [0, 1],
}

const computeNext = (x, y, direction) => {
    const [nextX, nextY] = howToGoNext[direction];
    return [x + nextX, y + nextY];
}
const isWithinBoundaries = (field, x, y) => y >= 0 && y < field.length && x >= 0 && x < field[y].length;

const isReachable = (field, x, y, value, direction) => {
    const [newX, newY] = computeNext(x, y, direction);
    return isWithinBoundaries(field, newX, newY) && toHeight(field[newY][newX]) - value <= 1;
}

const smarterNodes = (field, end) => {
    const playingGround = [];
    for (let y = 0; y < field.length; y++) {
        playingGround.push(field[y].map((letter, x) => {
            const value = toHeight(letter);
            return {
                letter, value, x, y,
                L: isReachable(field, x, y, value, 'L'),
                R: isReachable(field, x, y, value, 'R'),
                U: isReachable(field, x, y, value, 'U'),
                D: isReachable(field, x, y, value, 'D'),
                distanceToEnd: letter === end ? 0 : MAX_VALUE,
            };
        }));
    }

    return playingGround;
}

const computeFromCurrentNode = (field, x, y, direction) => {
    if (field[y][x][direction]) {
        const [newX, newY] = computeNext(x, y, direction);
        const previousDistanceToEnd = field[y][x].distanceToEnd;
        const distanceToEnd = field[newY][newX].distanceToEnd + 1;

        if (distanceToEnd < previousDistanceToEnd) {
            field[y][x].distanceToEnd = distanceToEnd;
        }
    }
}

const countUnvisitedMembers = field => {
    let unvisitedMembers = 0;
    for (let y = 0; y < field.length; y++) {
        for (let x = 0; x < field[y].length; x++) {
            if (field[y][x].distanceToEnd !== MAX_VALUE) {
                unvisitedMembers++;
            }
        }
    }

    return unvisitedMembers;
};

const getDistance = (field, start) => {
    const distancesAndValues = field.map(row => row.map(({letter, distanceToEnd: distance}) => ({letter, distance})));
    for (let y = 0; y < distancesAndValues.length; y++) {
        for (let x = 0; x < distancesAndValues[y].length; x++) {
            const {letter, distance} = distancesAndValues[y][x];
            if (letter === start) {
                return distance;
            }
        }
    }
    return -1;
}

const computeDistanceToEnd = field => {
    let previousUnvisitedMembers = 0;
    let unvisitedMembers = 1;
    while (unvisitedMembers !== 0 && unvisitedMembers !== previousUnvisitedMembers) {
        for (let y = 0; y < field.length; y++) {
            for (let x = 0; x < field[y].length; x++) {
                ['L', 'U', 'R', 'D'].forEach(direction => computeFromCurrentNode(field, x, y, direction));
            }
        }

        previousUnvisitedMembers = unvisitedMembers;
        unvisitedMembers = countUnvisitedMembers(field);
    }
}

const exampleField = toLetterArray(example);
const smarterExampleField = smarterNodes(exampleField, END);
computeDistanceToEnd(smarterExampleField);
console.log('Example 1:', getDistance(smarterExampleField, START));

const inputField = toLetterArray(input);
const smarterInputField = smarterNodes(inputField, END);
computeDistanceToEnd(smarterInputField);
console.log('Solution 1:', getDistance(smarterInputField, START));

const getShortestPathSameHeightAs = (field, start) => {
    const lettersToCheck = Object.getOwnPropertyNames(LETTERS)
        .filter(l => LETTERS[start] === LETTERS[l]);

    const shortestDistances = [];
    for (let y = 0; y < field.length; y++) {
        for (let x = 0; x < field[y].length; x++) {
            const {letter, distanceToEnd} = field[y][x];
            if(lettersToCheck.some(val => val === letter)) {
                shortestDistances.push(distanceToEnd);
            }
        }
    }

    return shortestDistances.sort((a, b) => a - b)[0];
}

console.log('Example 2:', getShortestPathSameHeightAs(smarterExampleField, START));
console.log('Solution 2:', getShortestPathSameHeightAs(smarterInputField, START));
