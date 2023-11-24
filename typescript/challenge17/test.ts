import {input as exampleInput} from './example';
import {input} from './input';

const CHAMBER_WIDTH = 7;
const START_HEIGHT = 4;
const START_FROM_LEFT = 3;

class Shape {
    constructor(public coordinates: number[][], private highestX, private highestY, private lowestX, private lowestY) {
    }

    private canMoveHorizontally(chamber: number[][], wall: number, difference: number) {
        return !this.coordinates.some(([x]) => x === wall)
            && !chamber.some(([x1, y1]) => this.coordinates.some(([x2, y2]) => x2 === x1 - difference && y1 === y2));
    }

    public moveLeft(chamber: number[][]) {
        // if (this.highestX !== 1) {
        //     this.highestX--;
        //     this.lowestX--;
        // }
        if (this.canMoveHorizontally(chamber, 1, -1)) {
            this.coordinates = this.coordinates.map(([x, y]) => [x - 1, y]);
        }
    }

    public moveRight(chamber: number[][]) {
        // if (this.highestX !== CHAMBER_WIDTH) {
        //     this.highestX++;
        //     this.lowestX++;
        // }
        if (this.canMoveHorizontally(chamber, CHAMBER_WIDTH, 1)) {
            this.coordinates = this.coordinates.map(([x, y]) => [x + 1, y]);
        }
    }

    public canMoveDown(chamber: number[][]) {
        return !chamber.some(([x1, y1]) => this.coordinates.some(([x2, y2]) => x2 === x1 && y1 === y2 - 1))
            && !this.coordinates.some(([, y]) => y === 1);
    }

    public moveDown() {
        this.coordinates = this.coordinates.map(([x, y]) => [x, y - 1]);
        this.highestY--;
        // this.lowestY--;
    }

    public getHighestY() {
        return this.highestY;
    }
}

const minus = (left, bottom) => new Shape([[left, bottom], [left + 1, bottom], [left + 2, bottom], [left + 3, bottom]], left + 3, bottom, left, bottom);
const plus = (left, bottom) => new Shape([[left, bottom + 1], [left + 1, bottom + 2], [left + 1, bottom + 1], [left + 1, bottom], [left + 2, bottom + 1]], left + 2,bottom + 2, left, bottom);
const corner = (left, bottom) => new Shape([[left, bottom], [left + 1, bottom], [left + 2, bottom], [left + 2, bottom + 1], [left + 2, bottom + 2]], left + 2, bottom + 2, left, bottom);
const line = (left, bottom) => new Shape([[left, bottom + 3], [left, bottom + 2], [left, bottom + 1], [left, bottom]], left, bottom + 3, left, bottom);
const square = (left, bottom) => new Shape([[left, bottom + 1], [left + 1, bottom + 1], [left, bottom], [left + 1, bottom]], left + 1, bottom + 1, left, bottom);

const pushLeft = (chamber: number[][], shape: Shape) => shape.moveLeft(chamber);
const pushRight = (chamber: number[][], shape: Shape) => shape.moveRight(chamber);

const MOVE_BY_JET = {'>': pushRight, '<': pushLeft};
const SHAPES = [minus, plus, corner, line, square];

const printBoard = (chamber, shape) => {
    const allCoordinates = chamber.map(({coordinates}) => coordinates).reduce((res, c) => [...res, ...c], []);

    console.log('');
    for (let y = shape.getHighestY(); y >= 0; y--) {
        const line = [0, 1, 2, 3, 4, 5, 6, 7, 8].map(x => {
            if (y === 0) {
                if (x === 0 || x === CHAMBER_WIDTH + 1) {
                    return '+';
                }
                return '-';
            }
            if (x === 0 || x === CHAMBER_WIDTH + 1) {
                return '|';
            }
            if (shape.coordinates.some(([xs, ys]) => xs === x && ys === y)) {
                return '@';
            }
            if (allCoordinates.some(([xc, yc]) => xc === x && yc === y)) {
                return '#'
            }
            return '.';
        }).join('');
        console.log(line);
    }
    console.log('');
}

const watchTheRocksFall = (jet) => {
    const jetStream = jet.split('').map(c => c === '>' ? 1 : -1);
    let currentHeight = 0;
    let shouldFallDown = false;
    let nextShapeIdx = 1;
    let nextJetId = 0;
    let currentShape = SHAPES[0](START_FROM_LEFT, START_HEIGHT);
    const chamber = [];

    while (nextShapeIdx < 2023) {
        const flatChamber = chamber.map(({coordinates}) => coordinates).reduce((res, c) => [...res, ...c], []);
        if (shouldFallDown) {
            if (currentShape.canMoveDown(flatChamber)) {
                currentShape.moveDown();
            } else {
                chamber.push(currentShape);
                const shapeY = currentShape.getHighestY();
                if (currentHeight < shapeY) {
                    currentHeight = shapeY;
                }
                currentShape = SHAPES[(nextShapeIdx % 5)](START_FROM_LEFT, currentHeight + START_HEIGHT);
                nextShapeIdx++;
            }
        } else {
            MOVE_BY_JET[jet.charAt(nextJetId)](flatChamber, currentShape);
            nextJetId = (nextJetId + 1) % jet.length;
        }

        shouldFallDown = !shouldFallDown;
    }

    return currentHeight;
}

const watchTheRocksFall2 = (jet, numberOfExpectedRocks) => {
    // const jetStream = jet.split('').map(c => c === '>' ? 1 : -1);
    let currentHeight = 0;
    let shouldFallDown = false;
    let nextShapeIdx = 1;
    let nextJetId = 0;
    let currentShape = SHAPES[0](START_FROM_LEFT, START_HEIGHT);
    let chamber = [];

    while (nextShapeIdx <= numberOfExpectedRocks) {
        const flatChamber = chamber.map(({coordinates}) => coordinates).reduce((res, c) => [...res, ...c], []);
        // const heightDifference = currentShape.getLowestY() - currentHeight;
        // const nextMoves = jetStream.slice(nextJetId, nextJetId + heightDifference);

        if (shouldFallDown) {
            if (currentShape.canMoveDown(flatChamber)) {
                currentShape.moveDown();
            } else {
                chamber = [...chamber.slice(-7), currentShape];
                // chamber.push(currentShape);
                const shapeY = currentShape.getHighestY();
                if (currentHeight < shapeY) {
                    currentHeight = shapeY;
                }
                currentShape = SHAPES[(nextShapeIdx % 5)](START_FROM_LEFT, currentHeight + START_HEIGHT);
                nextShapeIdx++;
            }
        } else {
            MOVE_BY_JET[jet.charAt(nextJetId)](flatChamber, currentShape);
            nextJetId = (nextJetId + 1) % jet.length;
        }

        shouldFallDown = !shouldFallDown;
    }

    return currentHeight;
}

let startTime = new Date().getTime();
// const exampleHeight = watchTheRocksFall(exampleInput);
const exampleHeight = watchTheRocksFall2(exampleInput, 2022);
console.log('Example 1:', exampleHeight, new Date().getTime() - startTime);

startTime = new Date().getTime();
// const inputHeight = watchTheRocksFall(input);
const inputHeight = watchTheRocksFall2(input, 2022);
console.log('Solution 1:', inputHeight, new Date().getTime() - startTime);

startTime = new Date().getTime();
// const exampleHeight = watchTheRocksFall(exampleInput);
const wtf = 10000;
// const wtf = 1000000000000;
const largeExampleHeight = watchTheRocksFall2(exampleInput, wtf);
console.log('Example 2:', largeExampleHeight, new Date().getTime() - startTime);

startTime = new Date().getTime();
// const inputHeight = watchTheRocksFall(input);
const largeInputHeight = watchTheRocksFall2(input, wtf);
console.log('Solution 2:', largeInputHeight, new Date().getTime() - startTime);
