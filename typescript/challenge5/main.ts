import {input} from './input';

interface Stack {
    values: string[][];
    size: number;
}

interface Move {
    value: number;
    fromIndex: number;
    toIndex: number;
}

const parseMove = moveStr => {
    const moveStringParsed = moveStr.split(' ');
    return {
        value: Number(moveStringParsed[1]),
        fromIndex: Number(moveStringParsed[3]) - 1,
        toIndex: Number(moveStringParsed[5]) - 1,
    }
}

const parseStack = stackStr => {
    const parsed = stackStr.split('\n');
    const size = parsed[parsed.length-1].split('   ').length;

    const values = [];
    for (let i = 0; i < size; i++) {
        const currentStack = [];
        const itemIndex = 1 + (4 * i);
        for (let j = parsed.length - 2; j >= 0; j--) {
            const item = parsed[j].substring(itemIndex, itemIndex + 1).trim();
            if (item !== '') {
                currentStack.push(item);
            }
        }
        values.push(currentStack);
    }

    return {
        values,
        size,
    };
}

const parsedInput = input.split('\n\n');
const initialStack: Stack = parseStack(parsedInput[0]);
const moves: Move[] = parsedInput[1].split('\n').map(move => parseMove(move));

const moveItemsOneByOne = (fromStack, toStack, value) => {
    for (let nmb = 0; nmb < value; nmb++) {
        const item = fromStack.pop();
        toStack.push(item);
    }
}

const moveItemsTogether = (fromStack, toStack, value) => {
    const tempArray = [];
    for (let i = 0; i < value; i++) {
        tempArray.push(fromStack.pop());
    }

    tempArray.reverse().forEach(item => toStack.push(item));
}

function perform(stack: Stack, move: Move, moveFunction: (fromStack: string[], toStack: string[], value: number) => void): Stack {
    const fromStack = [...stack.values[move.fromIndex]];
    const toStack = [...stack.values[move.toIndex]];
    moveFunction(fromStack, toStack, move.value);

    return {
        size: stack.size,
        values: stack.values.map((stck, index) => {
            if (index === move.fromIndex) {
                return fromStack;
            }
            if(index === move.toIndex) {
                return toStack;
            }
            return stck;
        }),
    };
}

const readMessageFromTop = stack => stack.values.reduce((res, stck) => res + stck[stck.length - 1], '');

const stackAfterMoves1 = moves.reduce((stack, move) => perform(stack, move, moveItemsOneByOne), initialStack);
const stackAfterMoves2 = moves.reduce((stack, move) => perform(stack, move, moveItemsTogether), initialStack);

console.log('Solution 1: ', readMessageFromTop(stackAfterMoves1));
console.log('Solution 2: ', readMessageFromTop(stackAfterMoves2));
