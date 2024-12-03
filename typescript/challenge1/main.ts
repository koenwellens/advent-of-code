import {readFile} from '../common/readFile';
import {distance, parseArrays, similarity, sort, toNumberArray} from "./helper";

const exampleInput = readFile('1', 'example');
const input = readFile('1', 'input');

const algorithm1 = (str: string[]) => {
    const arrays = parseArrays(str);
    const array1 = toNumberArray(arrays, 0);
    const array2 = toNumberArray(arrays, 1);
    const sortedArray1 = sort(array1, 'ASC');
    const sortedArray2 = sort(array2, 'ASC');

    let result = 0;
    for (let i = 0; i < array1.length; i++) {
        result += distance(sortedArray1[i], sortedArray2[i]);
    }

    return result;
}

const algorithm2 = (str: string[]) => {
    const arrays = parseArrays(str);
    const array1 = toNumberArray(arrays, 0);
    const array2 = toNumberArray(arrays, 1);

    let result = 0;
    for (let i = 0; i < array1.length; i++) {
        const elem = array1[i];
        result += (elem * similarity(elem, array2));
    }

    return result;
}

// 11
console.log('example 1', algorithm1(exampleInput));
// 2086478
console.log('input 1', algorithm1(input));

// 31
console.log('example 2', algorithm2(exampleInput));
// 24941624
console.log('input 2', algorithm2(input));

