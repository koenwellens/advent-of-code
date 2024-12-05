import {readFile} from '../common/readFile';
import {A, numberOfDirectionsOfXmas, numberOfXmasStars, X} from "./helper";

const exampleInput = readFile('4', 'example');
const input = readFile('4', 'input');

const algorithm1 = (input: string[]) => {
    let result = 0;
    for (let y = 0; y < input.length; y++) {
        for (let x = 0; x < input[y].length; x++) {
            if (input[y][x] === X) {
                result += numberOfDirectionsOfXmas(x, y, input);
            }
        }
    }

    return result;
}

const algorithm2 = (input: string[]) => {
    let result = 0;
    for (let y = 0; y < input.length; y++) {
        for (let x = 0; x < input[y].length; x++) {
            if (input[y][x] === A) {
                result += numberOfXmasStars(x, y, input);
            }
        }
    }

    return result;
}

// 18
console.log('example 1', algorithm1(exampleInput));
// 2583
console.log('input 1', algorithm1(input));

// 9
console.log('example 2', algorithm2(exampleInput));
// 1978
console.log('input 2', algorithm2(input));

