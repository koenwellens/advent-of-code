import {readFile} from '../common/readFile';

const exampleInput = readFile('5', 'example');
const input = readFile('5', 'input');


const algorithm1 = (input: string[]) => {
    return input;
}

const algorithm2 = (input: string[]) => {
    return input;
}

// ???
console.log('example 1', algorithm1(exampleInput));
// ???
console.log('input 1', algorithm1(input));

// ???
console.log('example 2', algorithm2(exampleInput));
// ???
console.log('input 2', algorithm2(input));

