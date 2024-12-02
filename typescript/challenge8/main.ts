import {readFile} from '../common/readFile';

const example1 = readFile(8, 'example1');
const input1 = readFile(8, 'input1');

const parseLine = (line: string) => {
    const [id, leftAndRight] = line.split(" = (");
    const [L, rightWithClosure] = leftAndRight.split(", ");
    const R = rightWithClosure.slice(0, -1);
    return {[id]: {id, L, R}};
}

const parse = (lines: string[]) => {
    const [instructions, , ...elems] = lines;

    const elements = elems.reduce((res, line) => ({...res, ...parseLine(line)}), {});
    return {instructions, elements};
}

const numberOfStepsToFind = ({instructions, elements}, startNode = 'AAA', endNode = 'ZZZ') => {
    let node = elements[startNode];
    let result = 0;
    while (node.id !== endNode) {
        const instruction = instructions[(result++ % instructions.length)];
        node = elements[node[instruction]];
    }

    return result;
}

console.log(numberOfStepsToFind(parse(example1))); // 6
console.log(numberOfStepsToFind(parse(input1))); // 18827

const example2 = readFile(8, 'example2');
const input2 = readFile(8, 'input2');

function gcd(a: number, b: number): number {
    return b === 0 ? a : gcd(b, a % b);
}

function lcm(a: number, b: number): number {
    return (a * b) / gcd(a, b);
}

function findLCM(numbers: number[]): number {
    if (!numbers.length) {
        return 1;
    }

    let result = numbers[0];

    for (let i = 1; i < numbers.length; i++) {
        result = lcm(result, numbers[i]);
    }

    return result;
}

const findNodesEndingWith = (elements, suffix) => {
    return Object.getOwnPropertyNames(elements).filter(([, , c]) => c === suffix);
}

const numberOfStepsToFindAll = ({instructions, elements}, startNodeEnd = 'A', endNodeEnd = 'Z') => {
    let nodes = findNodesEndingWith(elements, startNodeEnd);
    const result = [];

    for (let i = 0; i < nodes.length; i++) {
        result[i] = 0;
        let node = nodes[i];
        while (!node.endsWith(endNodeEnd)) {
            const instruction = instructions[(result[i]++ % instructions.length)];
            node = elements[node][instruction];
        }
    }

    return findLCM(result);
}

console.log(numberOfStepsToFindAll(parse(example2))); // 6
console.log(numberOfStepsToFindAll(parse(input2))); // ???