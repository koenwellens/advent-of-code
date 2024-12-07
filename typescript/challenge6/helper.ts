export const UP = '^';
export const DOWN = 'v';
export const LEFT = '<';
export const RIGHT = '>';
export const OBSTACLE = '#';

const rotate = {
    [UP]: RIGHT,
    [DOWN]: LEFT,
    [LEFT]: UP,
    [RIGHT]: DOWN,
}

export type Direction = '^' | 'v' | '<' | '>';
export type Coordinate = { x: number, y: number; };
export type EndingComputors = { [key: string]: (Coordinate) => Coordinate };

export interface ComputationStep {
    next: Coordinate,
    direction: Direction,
    travelled: Coordinate[]
}

export const obstacleLocations = (input: string[]) => {
    const result: Coordinate[] = [];
    for (let y = 0; y < input.length; y++) {
        for (let x = 0; x < input[y].length; x++) {
            if (input[y][x] === OBSTACLE) {
                result.push({x, y})
            }
        }
    }
    return result;
}

export const findStartingPosition = (input: string[], start = UP) => {
    for (let y = 0; y < input.length; y++) {
        for (let x = 0; x < input[y].length; x++) {
            if (input[y][x] === start) {
                return {x, y};
            }
        }
    }
    throw new Error(`no start found: ${start}`);
}

const findObstacle = ({x, y}: Coordinate, direction: Direction, obstacles: Coordinate[]) => {
    if (direction === UP) {
        return obstacles.filter(({x: a, y: b}) => a === x && b < y).sort((a, b) => b.y - a.y)?.[0];
    }

    if (direction === DOWN) {
        return obstacles.filter(({x: a, y: b}) => a === x && b > y).sort((a, b) => a.y - b.y)?.[0];
    }

    if (direction === LEFT) {
        return obstacles.filter(({x: a, y: b}) => a < x && b === y).sort((a, b) => b.x - a.x)?.[0];
    }

    if (direction === RIGHT) {
        return obstacles.filter(({x: a, y: b}) => a > x && b === y).sort((a, b) => a.x - b.x)?.[0];
    }

    return null;
}

const moveTo = (obstacle: Coordinate, direction: Direction) => {
    if (obstacle && direction === UP) {
        return {x: obstacle.x, y: obstacle.y + 1};
    }

    if (obstacle && direction === DOWN) {
        return {x: obstacle.x, y: obstacle.y - 1};
    }

    if (obstacle && direction === LEFT) {
        return {x: obstacle.x + 1, y: obstacle.y};
    }

    if (obstacle && direction === RIGHT) {
        return {x: obstacle.x - 1, y: obstacle.y};
    }

    return null;
}

export const computeEndings = (input: string[]) => {
    return {
        [UP]: ({x, y}: Coordinate) => ({x, y: 0}),
        [DOWN]: ({x, y}: Coordinate) => ({x, y: input.length - 1}),
        [LEFT]: ({x, y}: Coordinate) => ({x: 0, y}),
        [RIGHT]: ({x, y}: Coordinate) => ({x: input[0].length - 1, y}),
    }
}

const coordinatesBetween = (c1: Coordinate, c2: Coordinate, direction: Direction) => {
    const result = [];
    if (direction === UP) {
        for (let y = c1.y; y >= c2.y; y--) {
            result.push({x: c1.x, y});
        }
    }
    if (direction === DOWN) {
        for (let y = c1.y; y <= c2.y; y++) {
            result.push({x: c1.x, y});
        }
    }
    if (direction === LEFT) {
        for (let x = c1.x; x >= c2.x; x--) {
            result.push({x, y: c1.y});
        }
    }
    if (direction === RIGHT) {
        for (let x = c1.x; x <= c2.x; x++) {
            result.push({x, y: c1.y});
        }
    }
    return result;
}

export const move = (current: Coordinate, direction: Direction, obstacles: Coordinate[], endings: EndingComputors) => {
    const obstacle = findObstacle(current, direction, obstacles);
    const next = moveTo(obstacle, direction);

    return {
        next,
        direction: rotate[direction],
        travelled: coordinatesBetween(current, next || endings[direction](current), direction),
    } as ComputationStep;
}
