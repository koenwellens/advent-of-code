import {example} from './example-input';
import {input} from './input';

const manhattanDistance = ([x1, y1]: number[], [x2, y2]: number[]) => Math.abs(x1 - x2) + Math.abs(y1 - y2);

const parseWithStr = (str, substring) => {
    const xAndY = str.substring(substring.length).split(', ');
    return [Number(xAndY[0].substring('x='.length)), Number(xAndY[1].substring('y='.length))];
};

const parseSensor = str => parseWithStr(str, 'Sensor at ');
const parseBeacon = str => parseWithStr(str, 'closest beacon is at ');

const parse = str => {
    const sensorAndBeacon = str.split(': ');
    const sensor = parseSensor(sensorAndBeacon[0]);
    const beacon = parseBeacon(sensorAndBeacon[1]);
    return [sensor, beacon, manhattanDistance(sensor, beacon)];
};

const computeXBoundaries = ([sensor, b, distance]: any[], y: number) => {
    if (y <= sensor[1] + distance && y >= sensor[1] - distance) {
        const partial = distance - Math.abs(sensor[1] - y);
        const x1 = sensor[0] - partial;
        const x2 = sensor[0] + partial;

        return x1 < x2 ? [x1, x2] : [x2, x1];
    }

    return [];
};

const merge = ([x1, x2], [x3, x4]) => {
    if (x3 >= x1 && x3 <= x2) {
        if (x4 <= x2) {
            return [[x1, x2]];
        }
        return [[x1, x4]];
    }

    if (x3 === x2 + 1) {
        return [[x1, x4]];
    }

    return [[x1, x2], [x3, x4]];
}

const optimize = array => {
    let prevArray = [];
    let currentArray = array;
    while (prevArray.length !== currentArray.length && currentArray.length > 1) {
        const [b1, b2, ...rest] = currentArray;
        prevArray = currentArray;
        currentArray = [...merge(b1, b2), ...optimize(rest)];
    }
    return currentArray;
}

const mapAllSensorsToBoundaries = (sensors, y) => {
    return optimize(sensors.map(sensor => computeXBoundaries(sensor, y))
        .filter(arr => !!arr.length)
        .sort(([x1], [x2]) => x1 - x2));
}

const countValuesIn = boundaries => boundaries.reduce((res, [a, b]) => res + (b - a), 0);


const exampleSensors = example.split('\n').map(str => parse(str));
const exampleBoundaries = mapAllSensorsToBoundaries(exampleSensors, 10);
console.log('Example 1:', countValuesIn(exampleBoundaries));


const inputSensors = input.split('\n').map(str => parse(str));
const inputBoundaries = mapAllSensorsToBoundaries(inputSensors, 2000000);
console.log('Solution 1:', countValuesIn(inputBoundaries));

const findBeacon = (sensors, min, max) => {
    for (let y = min; y <= max; y++) {
        const boundaries = mapAllSensorsToBoundaries(sensors, y);
        for (let idx = 0; idx < boundaries.length; idx++) {
            const [xMin, xMax] = boundaries[idx];
            if (xMin <= min && max > xMax) {
                return [xMax + 1, y];
            }
        }
    }
    return [0, 0];
}

const tuningFrequency = ([x, y]: number[]) => x * 4000000 + y;

const exampleBeacon = findBeacon(exampleSensors, 0, 20);
console.log('Example 2:', tuningFrequency(exampleBeacon));

const inputBeacon = findBeacon(inputSensors, 0, 4000000);
console.log('Solution 2:', tuningFrequency(inputBeacon));
