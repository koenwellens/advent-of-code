import {readFile} from '../common/readFile';
import {equationIsPossible, Operator, parse} from "./helper";

const exampleInput = readFile('7', 'example');
const input = readFile('7', 'input');

const algorithm1 = (input: string[]) => {
    const possibleEquations = parse(input);
    let result = 0;
    for (let possibleEquation of possibleEquations) {
        if (equationIsPossible(possibleEquation, [Operator.PLUS, Operator.TIMES])) {
            result += possibleEquation.res;
        }
    }
    return result;
}


const algorithm2 = (input: string[]) => {
    const possibleEquations = parse(input);
    let result = 0;
    for (let possibleEquation of possibleEquations) {
        if (equationIsPossible(possibleEquation, [Operator.PLUS, Operator.TIMES, Operator.CONCAT])) {
            result += possibleEquation.res;
        }
    }
    return result;
}

// 3749
console.log('example 1', algorithm1(exampleInput));
// 42283209483350
console.log('input 1', algorithm1(input));

// 11387
console.log('example 2', algorithm2(exampleInput));
// 1026766857276279
console.log('input 2', algorithm2(input));
