import {input as example} from './example-input';
import {input} from './input';

const isInCorrectOrder = (left, right) => {
    if (!Array.isArray(left) && !Array.isArray(right)) {
        return left - right;
    }

    if (!Array.isArray(left)) {
        return isInCorrectOrder([left], right);
    }

    if (!Array.isArray(right)) {
        return isInCorrectOrder(left, [right]);
    }

    for (let i = 0; i < left.length; i++) {
        if (i === right.length) {
            return 10;
        }

        const subResult = isInCorrectOrder(left[i], right[i]);
        if (subResult !== 0) {
            return subResult;
        }
    }

    return left.length - right.length;
}

const sumOfIndicesInCorrectOrder = pairs => pairs.map(pair => pair.split('\n'))
    .map(([left, right]) => [JSON.parse(left), JSON.parse(right)])
    .map(([left, right]) => isInCorrectOrder(left, right))
    .reduce((res, val, idx) => val < 0 ? res + (idx + 1) : res, 0);

const examplePairs = example.split('\n\n');
console.log('Example 1:', sumOfIndicesInCorrectOrder(examplePairs));

const inputPairs = input.split('\n\n');
console.log('Solution 1:', sumOfIndicesInCorrectOrder(inputPairs));

const DIVIDER_PACKETS = [[[2]], [[6]]];
const flatMapPairs = pairs => pairs.map(pair => pair.split('\n'))
    .map(([left, right]) => [JSON.parse(left), JSON.parse(right)])
    .reduce((res, pair) => [...res, ...pair], []);

const findDecoderKey = packets => {
    packets.sort((left, right) => isInCorrectOrder(left, right));

    return (packets.indexOf(DIVIDER_PACKETS[0]) + 1)
        * (packets.indexOf(DIVIDER_PACKETS[1]) + 1);
}

const fullExampleSignal = [...flatMapPairs(examplePairs), ...DIVIDER_PACKETS];
console.log('Example 2:', findDecoderKey(fullExampleSignal));

const fullInputSignal = [...flatMapPairs(inputPairs), ...DIVIDER_PACKETS];
console.log('Solution 2:', findDecoderKey(fullInputSignal));
