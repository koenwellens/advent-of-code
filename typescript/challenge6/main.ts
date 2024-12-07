import {readFile} from '../common/readFile';

const exampleInput = readFile('6', 'example');
const input = readFile('6', 'input');

const algorithm1 = (input: string[]) => {
    return input;
}


const algorithm2 = (input: string[]) => {
    return input;
}

// 143
console.log('example 1', algorithm1(exampleInput));
// 6505
console.log('input 1', algorithm1(input));

// 123
console.log('example 2', algorithm2(exampleInput));
// 6897
console.log('input 2', algorithm2(input));

