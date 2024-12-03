import {readFile} from '../common/readFile';
import {convertToIntervals, findMulNumbers, getAllIndexesFor} from "./helper";

const exampleInput = readFile('3', 'example').join('');
const input = readFile('3', 'input').join('');
const exampleInput2 = readFile('3', 'example2').join('');

const algorithm1 = (str: string) => {
    const allNumberPairs = findMulNumbers(str);
    let result = 0;
    for (let [x, y] of allNumberPairs) {
        result += x * y;
    }

    return result;
}

const algorithm2 = (str: string) => {
    const doIndexes = getAllIndexesFor(str, /do\(\)/g);
    const dontIndexes = getAllIndexesFor(str, /don't\(\)/g);
    const intervals = convertToIntervals([0, ...doIndexes], dontIndexes, str.length - 1);

    const allNumberPairs = findMulNumbers(str, ({index}) => {
        return intervals.some(([s, e]) => s <= index && e >= index);

    });
    let result = 0;
    for (let [x, y] of allNumberPairs) {
        result += x * y;
    }

    return result;
}

// 161
console.log('example 1', algorithm1(exampleInput));
// 161085926
console.log('input 1', algorithm1(input));

// 48
console.log('example 2', algorithm2(exampleInput2));
// 82045421
console.log('input 2', algorithm2(input));

