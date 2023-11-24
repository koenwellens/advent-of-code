import {input as exampleInput} from './example';
import {input} from './input';

const CHAMBER_WIDTH = 7;
const START_HEIGHT = 4;
const START_FROM_LEFT = 3;

const MOVE_BY_JET = {'>': 'moveRight', '<': 'moveLeft'};

class Shape {

    public numberOfCoordinates;
    public fallenHeight = 3;
    private horizontalDiff = 0;
    private readonly highestX;
    private readonly highestY;
    private readonly lowestX;
    private readonly lowestY;

    constructor(private readonly leftCheckCoordinates: number[][],
                private readonly rightCheckCoordinates: number[][],
                private readonly bottomCheckCoordinates: number[][],
                private readonly originalCoordinates: number[][],
                readonly lowest: number[],
                readonly maxDiff: number[],
                readonly jetStart: string[]) {
        this.numberOfCoordinates = originalCoordinates.length;
        [this.lowestX, this.lowestY] = lowest;
        this.highestX = this.lowestX + maxDiff[0];
        this.highestY = this.lowestY + maxDiff[1];

        this[MOVE_BY_JET[jetStart[0]]]([], false);
        this[MOVE_BY_JET[jetStart[1]]]([], false);
        this[MOVE_BY_JET[jetStart[2]]]([], false);
    }

    public get coordinates() {
        return this.originalCoordinates.map(([x, y]) => [x + this.horizontalDiff, y - this.fallenHeight]);
    }

    private atLeastOneCoordinateMatches(coordinates: number[][], [expectedX, expectedY]) {
        return coordinates.some(([x, y]) => (x + this.horizontalDiff) === expectedX && expectedY === y - this.fallenHeight)
    }

    public moveLeft(chamber: number[][], shouldCheckRocks: boolean = true) {
        if (this.lowestX + this.horizontalDiff !== 1) {
            if (!shouldCheckRocks || !chamber.some(([x, y]) => this.atLeastOneCoordinateMatches(this.leftCheckCoordinates, [x + 1, y]))) {
                this.horizontalDiff--;
            }
        }
    }

    public moveRight(chamber: number[][], shouldCheckRocks: boolean = true) {
        if (this.highestX + this.horizontalDiff !== CHAMBER_WIDTH) {
            if (!shouldCheckRocks || !chamber.some(([x, y]) => this.atLeastOneCoordinateMatches(this.rightCheckCoordinates, [x - 1, y]))) {
                this.horizontalDiff++;
            }
        }
    }

    public canMoveDown(chamber: number[][], cannotHitBottom: boolean) {
        if (!cannotHitBottom) {
            return this.lowestY - this.fallenHeight > 1;
        }

        return !chamber.some(([x, y]) => y >= this.lowestY - this.fallenHeight - 2 && this.atLeastOneCoordinateMatches(this.bottomCheckCoordinates, [x, y + 1]));
    }

    public moveDown() {
        this.fallenHeight++;
    }

    public getHighestY() {
        return this.highestY - this.fallenHeight;
    }
}


class Minus extends Shape {
    constructor(left, bottom, start) {
        super(
            [[left, bottom]],
            [[left + 3, bottom]],
            [[left, bottom], [left + 1, bottom], [left + 2, bottom], [left + 3, bottom]],
            [[left, bottom], [left + 1, bottom], [left + 2, bottom], [left + 3, bottom]],
            [left, bottom],
            [3, 0],
            start,
        );
    }
}

class Plus extends Shape {
    constructor(left, bottom, start) {
        super(
            [[left, bottom + 1], [left + 1, bottom + 2], [left + 1, bottom]],
            [[left + 2, bottom + 1], [left + 1, bottom + 2], [left + 1, bottom]],
            [[left, bottom + 1], [left + 1, bottom], [left + 2, bottom + 1]],
            [[left, bottom + 1], [left + 1, bottom + 2], [left + 1, bottom + 1], [left + 1, bottom], [left + 2, bottom + 1]],
            [left, bottom],
            [2, 2],
            start,
        );
    }
}

class Corner extends Shape {
    constructor(left, bottom, start) {
        super(
            [[left, bottom], [left + 2, bottom + 1], [left + 2, bottom + 2]],
            [[left + 2, bottom], [left + 2, bottom + 1], [left + 2, bottom + 2]],
            [[left, bottom], [left + 1, bottom], [left + 2, bottom]],
            [[left, bottom], [left + 1, bottom], [left + 2, bottom], [left + 2, bottom + 1], [left + 2, bottom + 2]],
            [left, bottom],
            [2, 2],
            start,
        );
    }
}

class Line extends Shape {
    constructor(left, bottom, start) {
        super(
            [[left, bottom + 3], [left, bottom + 2], [left, bottom + 1], [left, bottom]],
            [[left, bottom + 3], [left, bottom + 2], [left, bottom + 1], [left, bottom]],
            [[left, bottom]],
            [[left, bottom + 3], [left, bottom + 2], [left, bottom + 1], [left, bottom]],
            [left, bottom],
            [0, 3],
            start,
        );
    }
}

class Square extends Shape {
    constructor(left, bottom, start) {
        super(
            [[left, bottom + 1], [left, bottom]],
            [[left + 1, bottom + 1], [left + 1, bottom]],
            [[left, bottom], [left + 1, bottom]],
            [[left, bottom + 1], [left + 1, bottom + 1], [left, bottom], [left + 1, bottom]],
            [left, bottom],
            [1, 1],
            start,
        );
    }
}

const SHAPES = [Minus, Plus, Corner, Line, Square];

const watchTheRocksFall = ([jet1, jet2, jet3, ...jets]: string[], numberOfExpectedRocks) => {
    let currentHeight = 0;
    let nextShapeIdx = 0;
    let currentShape = new SHAPES[nextShapeIdx++](START_FROM_LEFT, START_HEIGHT, [jet1, jet2, jet3]);
    let chamber = [];
    let movingJets = [...jets, jet1, jet2, jet3];

    while (nextShapeIdx <= numberOfExpectedRocks) {
        const [nextJet, ...otherJets] = movingJets;
        currentShape[MOVE_BY_JET[nextJet]](chamber);
        movingJets = [...otherJets, nextJet];

        if (currentShape.canMoveDown(chamber, nextShapeIdx > 1)) {
            currentShape.moveDown();
        } else {
            chamber = [...chamber, ...currentShape.coordinates];

            const shapeY = currentShape.getHighestY();
            if (currentHeight < shapeY) {
                currentHeight = shapeY;
            }
            const [j1, j2, j3, ...otherJets] = movingJets;
            currentShape = new SHAPES[nextShapeIdx % 5](
                START_FROM_LEFT,
                currentHeight + START_HEIGHT,
                [j1, j2, j3],
            );
            movingJets = [...otherJets, j1, j2, j3];
            nextShapeIdx++;
        }
    }

    return currentHeight;
}

let startTime = new Date().getTime();
const exampleHeight = watchTheRocksFall(exampleInput.split(''), 2022);
console.log('Example 1:', exampleHeight, new Date().getTime() - startTime);
// 3068

startTime = new Date().getTime();
const inputHeight = watchTheRocksFall(input.split(''), 2022);
console.log('Solution 1:', inputHeight, new Date().getTime() - startTime);
// 3092

startTime = new Date().getTime();
const wtf = 100000;
// const wtf = 1000000000000;
const largeExampleHeight = watchTheRocksFall(exampleInput.split(''), wtf);
console.log('Example 2:', largeExampleHeight, new Date().getTime() - startTime);
// 1514285714288

startTime = new Date().getTime();
const largeInputHeight = watchTheRocksFall(input.split(''), wtf);
console.log('Solution 2:', largeInputHeight, new Date().getTime() - startTime);
// 10091371428571428 is too high
