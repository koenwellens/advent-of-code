import {readFile} from '../common/readFile';
import {complies, fixOrdering, middlePage, parseInput} from "./helper";

const exampleInput = readFile('5', 'example');
const input = readFile('5', 'input');


const algorithm1 = (input: string[]) => {
    const {rules, pages} = parseInput(input);

    let result = 0;
    for (let pageNumbers of pages) {
        if (pageNumbers.every(page => complies(page, pageNumbers, rules))) {
            result += middlePage(pageNumbers);
        }

    }

    return result;
}

const algorithm2 = (input: string[]) => {
    const {rules, pages} = parseInput(input);

    let result = 0;
    for (let pageNumbers of pages) {
        if (pageNumbers.some(page => !complies(page, pageNumbers, rules))) {
            result += middlePage(fixOrdering(pageNumbers, rules));
        }
    }

    return result;
}

// 143
console.log('example 1', algorithm1(exampleInput));
// 6505
console.log('input 1', algorithm1(input));

// 123
console.log('example 2', algorithm2(exampleInput));
// 6897
console.log('input 2', algorithm2(input));

