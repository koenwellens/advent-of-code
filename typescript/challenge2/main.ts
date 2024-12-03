import {readFile} from '../common/readFile';
import {allAreSafe, allButOneLevelAreSafe, parse} from "./helper";

const exampleInput = readFile('2', 'example');
const input = readFile('2', 'input');

const algorithm1 = (str: string[]) => {
    let result = 0;
    for (let s of str) {
        const numbers = parse(s);
        if (allAreSafe(numbers)) {
            result++;
        }
    }

    return result;
}

const algorithm2 = (str: string[]) => {
    let result = 0;
    for (let s of str) {
        const numbers = parse(s);

        if (allAreSafe(numbers) || allButOneLevelAreSafe(numbers)) {
            result++;
        }
    }

    return result;
}

// 2
console.log('example 1', algorithm1(exampleInput));
// 524
console.log('input 1', algorithm1(input));

// 4
console.log('example 2', algorithm2(exampleInput));
// 569
console.log('input 2', algorithm2(input));

