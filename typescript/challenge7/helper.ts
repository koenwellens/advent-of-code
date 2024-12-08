export enum Operator {
    PLUS = '+',
    TIMES = '*',
    CONCAT = '||'
}

export const parse = (input: string[]) => {
    return input.map((s: string) => {
        const [res, unsplitNumbers] = s.split(': ');
        const numbers = unsplitNumbers.split(' ').map(a => +a);
        return {res: +res, numbers};
    });
}

const numberFn = {
    [Operator.PLUS]: (a, b) => a + b,
    [Operator.TIMES]: (a, b) => a * b,
    [Operator.CONCAT]: (a, b) => +`${a}${b}`,
}

const getNext = (res: number, numbers: number[], possibleOperators: Operator[]) => {
    const [first, second, ...rest] = numbers;
    return possibleOperators.map(operator => ({res, numbers: [numberFn[operator](first, second), ...rest]}));
}

export const equationIsPossible = (start: { res: number, numbers: number[] }, possibleOperators: Operator[]) => {
    let next: { res: number, numbers: number[] }[] = [start];
    while (next.length) {
        if (next.find(({res, numbers}) => numbers.length === 1 && res === numbers[0])) {
            return true;
        }

        const {res, numbers} = next.splice(0, 1)[0];

        if (numbers.length > 1 && numbers[0] <= res) {
            next.push(...getNext(res, numbers, possibleOperators));
            next.sort((a, b) => a.numbers.length - b.numbers.length);
        }
    }

    return false;
}
