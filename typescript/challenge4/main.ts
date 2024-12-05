import {readFile} from '../common/readFile';

const exampleInput = readFile('4', 'example');
const input = readFile('4', 'input');
// const exampleInput2 = readFile('4', 'example2').join('');

type XMAS = 'X' | 'M' | 'A' | 'S'
const firstLetter = 'X';

const checkFor = (x: number, y: number, char: XMAS, input: string[]) => {
    return input[y]?.[x] === char;
}

const checkLeft = (x: number, y: number, input: string[]) => {
    return checkFor(x - 1, y, 'M', input) && checkFor(x - 2, y, 'A', input) && checkFor(x - 3, y, 'S', input);
}

const checkRight = (x: number, y: number, input: string[]) => {
    return checkFor(x + 1, y, 'M', input) && checkFor(x + 2, y, 'A', input) && checkFor(x + 3, y, 'S', input);
}

const checkUp = (x: number, y: number, input: string[]) => {
    return checkFor(x, y - 1, 'M', input) && checkFor(x, y - 2, 'A', input) && checkFor(x, y - 3, 'S', input);
}

const checkDown = (x: number, y: number, input: string[]) => {
    return checkFor(x, y + 1, 'M', input) && checkFor(x, y + 2, 'A', input) && checkFor(x, y + 3, 'S', input);
}

const checkDiagUpLeft = (x: number, y: number, input: string[]) => {
    return checkFor(x - 1, y - 1, 'M', input) && checkFor(x - 2, y - 2, 'A', input) && checkFor(x - 3, y - 3, 'S', input);
}

const checkDiagDownRight = (x: number, y: number, input: string[]) => {
    return checkFor(x + 1, y + 1, 'M', input) && checkFor(x + 2, y + 2, 'A', input) && checkFor(x + 3, y + 3, 'S', input);
}

const checkDiagUpRight = (x: number, y: number, input: string[]) => {
    return checkFor(x + 1, y - 1, 'M', input) && checkFor(x + 2, y - 2, 'A', input) && checkFor(x + 3, y - 3, 'S', input);
}

const checkDiagDownLeft = (x: number, y: number, input: string[]) => {
    return checkFor(x - 1, y + 1, 'M', input) && checkFor(x - 2, y + 2, 'A', input) && checkFor(x - 3, y + 3, 'S', input);
}

const numberOfDirections = (x: number, y: number, input: string[]) => {
    return [
        checkLeft(x, y, input),
        checkRight(x, y, input),
        checkUp(x, y, input),
        checkDown(x, y, input),
        checkDiagUpLeft(x, y, input),
        checkDiagUpRight(x, y, input),
        checkDiagDownRight(x, y, input),
        checkDiagDownLeft(x, y, input),
    ].filter(check => check).length;
}

const algorithm1 = (input: string[]) => {
    let result = 0;
    for (let y = 0; y < input.length; y++) {
        for (let x = 0; x < input[y].length; x++) {
            if (input[y][x] === firstLetter) {
                result += numberOfDirections(x, y, input);
            }
        }
    }

    return result;
}

const algorithm2 = (str: string[]) => {
    return str;
}

// 18
console.log('example 1', algorithm1(exampleInput));
// 2583
console.log('input 1', algorithm1(input));

// 9
console.log('example 2', algorithm2(exampleInput));
// ???
// console.log('input 2', algorithm2(input));

