import {input} from './input';
import {input as exampleInput, largeExample} from './example-input';

interface Coordinate {
    x: number;
    y: number;
}

const zeroZero = {x: 0, y: 0};

const moves = {
    U: {x: 0, y: 1},
    D: {x: 0, y: -1},
    L: {x: -1, y: 0},
    R: {x: 1, y: 0},
};

const lastCoordinate = coords => coords[coords.length - 1];
const differenceBetweenTheEndsIsTooBig = ({x, y}) => Math.abs(x) > 1 || Math.abs(y) > 1

const computeDifference = (heads, tails) => {
    const lastHead = lastCoordinate(heads);
    const lastTail = lastCoordinate(tails);
    return {x: lastHead.x - lastTail.x, y: lastHead.y - lastTail.y};
}

const computeDirection = ({x, y}) => {
    return {x: x === 0 ? 0 : x / Math.abs(x), y: y === 0 ? 0 : y / Math.abs(y)};
}

const moveInDirection = (coords, direction) => {
    const {x, y} = lastCoordinate(coords);
    return {x: x + direction.x, y: y + direction.y};
}

const doMovesFor = (commands, startPosition = zeroZero, numberOfTails = 1) => {
    const head: Coordinate[] = [startPosition];
    const tails: Coordinate[][] = [];
    for (let i = 0; i < numberOfTails; i++) {
        tails.push([startPosition]);
    }
    const headAndTails = [head, ...tails];

    commands.forEach(command => {
        const directionAndTimes = command.split(' ');
        const times = Number(directionAndTimes[1]);
        const headDirection = moves[directionAndTimes[0]];

        for (let i = 0; i < times; i++) {
            head.push(moveInDirection(head, headDirection));

            for (let tailIndex = 1; tailIndex < headAndTails.length; tailIndex++) {
                const previousTail = headAndTails[tailIndex - 1];
                const currentTail = headAndTails[tailIndex];

                const difference = computeDifference(previousTail, currentTail);
                if (differenceBetweenTheEndsIsTooBig(difference)) {
                    currentTail.push(moveInDirection(currentTail, computeDirection(difference)));
                }
            }
        }
    });

    return {head, tails};
};

const isSameAs = (coord1, coord2) => coord1.x === coord2.x && coord1.y === coord2.y;
const numberOfUniquePlacesVisited = tails => tails[tails.length - 1].reduce((res, coord) => res.filter(val => isSameAs(val, coord)).length ? res : [...res, coord], []).length

const commandsForExample = exampleInput.split('\n');
const commandsForInput = input.split('\n');
const commandsForLargeExample = largeExample.split('\n');

const movesDoneForExample = doMovesFor(commandsForExample);
const movesDoneForInput = doMovesFor(commandsForInput);

console.log('Example 1: ', numberOfUniquePlacesVisited(movesDoneForExample.tails));
console.log('Solution 1: ', numberOfUniquePlacesVisited(movesDoneForInput.tails));

const movesDoneForExampleNineTails = doMovesFor(commandsForExample, zeroZero, 9);
const movesDoneForLargeExampleNineTails = doMovesFor(commandsForLargeExample, {x: 11, y: 5}, 9);
const movesDoneForInput2 = doMovesFor(commandsForInput, {x: 11, y: 5}, 9);

console.log('Example 2: ', numberOfUniquePlacesVisited(movesDoneForExampleNineTails.tails));
console.log('Large example 2: ', numberOfUniquePlacesVisited(movesDoneForLargeExampleNineTails.tails));
console.log('Solution 2: ', numberOfUniquePlacesVisited(movesDoneForInput2.tails));
