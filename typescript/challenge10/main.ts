import {input as example, largeExample} from "./example-input";
import {input} from './input';

class Cycle {
    constructor(public duration: number, public value: number) {
    }
}

const isNoOp = str => str.indexOf('noop') >= 0;

const noop = new Cycle(1, 0);
const parseAddX = str => {
    const addX = str.split(' ');
    return [noop, new Cycle(1, Number(addX[1]))];
}

const parseCommands = commands => commands.map(command => {
    if (isNoOp(command)) {
        return [noop];
    }
    return parseAddX(command);
}).reduce((res, command) => [...res, ...command], []);

const move = cycles => {
    return cycles.reduce((res, {value}) => {
        const previousX = res[res.length - 1];
        return [...res, previousX + value];
    }, [1]);
}

const signalStrengthAt = (execution, cycle) => execution[cycle - 1] * cycle
const sumOfSignalStrengths = execution => signalStrengthAt(execution, 20)
    + signalStrengthAt(execution, 60)
    + signalStrengthAt(execution, 100)
    + signalStrengthAt(execution, 140)
    + signalStrengthAt(execution, 180)
    + signalStrengthAt(execution, 220);

const exampleCommands = example.split('\n');
const largeExampleCommands = largeExample.split('\n');
const inputCommands = input.split('\n');

const exampleMoves = move(parseCommands(exampleCommands));
const largeExampleMoves = move(parseCommands(largeExampleCommands));
const inputmoves = move(parseCommands(inputCommands));

console.log("Example 1: ", sumOfSignalStrengths(largeExampleMoves));
console.log("Solution 1: ", sumOfSignalStrengths(inputmoves));

const CRT_WIDTH = 40;
const createEmptyCrt = () => [[], [], [], [], [], []];
const computePixel = (cycle, x) => Math.abs(cycle - x) <= 1 ? '#' : '.';

const computeCrt = (movesAfterCycles) => {
    const crt = createEmptyCrt();
    for (let i = 0; i < crt.length; i++) {
        for (let j = 0; j < CRT_WIDTH; j++) {
            const cycle = (CRT_WIDTH * i) + j;
            crt[i].push(computePixel(j, movesAfterCycles[cycle]));
        }
    }
    return crt;
}

console.log('Example 2:');
computeCrt(largeExampleMoves).forEach(row => console.log(row.join('')))

console.log('Solution 2:');
computeCrt(inputmoves).forEach(row => console.log(row.join('')))
