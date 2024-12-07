import {readFile} from '../common/readFile';
import {computeEndings, Direction, findStartingPosition, move, obstacleLocations, UP} from "./helper";

const exampleInput = readFile('6', 'example');
const input = readFile('6', 'input');

const algorithm1 = (input: string[]) => {
    const endings = computeEndings(input);
    const obstacles = obstacleLocations(input);

    let currentPosition = findStartingPosition(input);
    let currentDirection: Direction = UP;
    const result = new Set();
    while (currentPosition !== null) {
        const {next, direction, travelled} = move(currentPosition, currentDirection, obstacles, endings);
        currentPosition = next;
        currentDirection = direction;
        travelled.forEach(({x, y}) => result.add(`${x},${y}`));
    }

    return result.size;
}


const algorithm2 = (input: string[]) => {
    const endings = computeEndings(input);
    const obstacles = obstacleLocations(input);

    const startingPosition = findStartingPosition(input);
    let result = 0;
    for (let y = 0; y < input.length; y++) {
        for (let x = 0; x < input[y].length; x++) {
            if (![...obstacles, startingPosition].find(obstacle => obstacle.x === x && obstacle.y === y)) {
                let currentPosition = startingPosition;
                let currentDirection: Direction = UP;
                const newObstacles = [...obstacles, {x, y}];
                const travelled = [`${currentPosition.x},${currentPosition.y},${currentDirection}`];
                while (currentPosition !== null) {
                    const {next, direction} = move(currentPosition, currentDirection, newObstacles, endings);
                    currentPosition = next;
                    currentDirection = direction;
                    if (next !== null) {
                        if (travelled.includes(`${currentPosition.x},${currentPosition.y},${currentDirection}`)) {
                            result++;
                            break;
                        }
                        travelled.push(`${currentPosition.x},${currentPosition.y},${currentDirection}`);
                    }
                }
            }
        }
    }

    return result;
}

// 41
console.log('example 1', algorithm1(exampleInput));
// 4515
console.log('input 1', algorithm1(input));

// 6
console.log('example 2', algorithm2(exampleInput));
// 1309
console.log('input 2', algorithm2(input));
