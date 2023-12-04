import {readFile} from '../common/readFile';

const example1 = readFile(3, 'example1');
const input1 = readFile(3, 'input1');

/**
 * 467..114..
 * ...*......
 * ..35..633.
 * ......#...
 * 617*......
 * .....+.58.
 * ..592.....
 * ......755.
 * ...$.*....
 * .664.598..
 */

const range = (start, end) => Array.from({length: (end - start)}, (v, k) => k + start);

const allNumbers = (text: string[]) => {
    return text.map((line, row) => {
        const result: {row: number, columns: number[], num: number}[] = [];
        for (let column = 0; column < line.length; column++) {
            if (!isNaN(+line[column])) {
                for (let endColumn = column + 1; endColumn < line.length; endColumn++) {
                    if (endColumn === line.length - 1 && !isNaN(+line[endColumn])) {
                        result.push({
                            num: +line.substring(column),
                            row,
                            columns: range(column, endColumn + 1),
                        });
                        column += (endColumn - column - 1);
                    }
                    if (isNaN(+line[endColumn])) {
                        result.push({
                            num: +line.substring(column, endColumn),
                            row,
                            columns: range(column, endColumn),
                        });
                        column += (endColumn - column - 1);
                        break;
                    }
                }
            }
        }
        return result;
    })
        .reduce((res, arr) => [...res, ...arr], []);
}

const computeAdjacentCoordinates = ({row, columns}) => {
    return [
        ...range(columns[0] - 1, columns[columns.length - 1] + 2)
            .map(column => ({row: row - 1, column})),
        {row, column: columns[0] - 1},
        {row, column: columns[columns.length - 1] + 1},
        ...range(columns[0] - 1, columns[columns.length - 1] + 2)
            .map(column => ({row: row + 1, column})),
    ];
}

const atLeastOneIsSymbol = (text: string[], coordinates: { row: number, column: number }[]) => {
    return coordinates
        .filter(({row}) => row >= 0 && row < text.length)
        .filter(({row, column}) => column >= 0 && column < text[row].length)
        .some(({
                   row,
                   column
               }) => text[row][column] !== '.' && isNaN(+text[row][column]));
}

const findPartNumbers = text => {
    const potentialNumberParts = allNumbers(text);

    return potentialNumberParts.filter(({row, columns, num}) => {
        const adjacentCoordinates = computeAdjacentCoordinates({row, columns});
        return atLeastOneIsSymbol(text, adjacentCoordinates);
    });
}

const sum = arr => arr.reduce((res, num) => res + num, 0)

console.log(sum(findPartNumbers(example1).map(({num}) => num))); // 4361
console.log(sum(findPartNumbers(input1).map(({num}) => num))); // 539433

const allGears = (text: string[]) => {
    return text.map((line, row) => {
        const result: {row: number, column: number}[] = [];
        for (let column = 0; column < line.length; column++) {
            if (text[row][column] === '*') {
                result.push({row, column});
            }
        }
        return result;
    })
        .reduce((res, arr) => [...res, ...arr], []);
}

const computeGearRatio = (potentialNumberParts, numberParts) => {
    const potentialNumberPartsNextToGears = potentialNumberParts.map(({row, column}) => numberParts.find(np => np.row === row && np.columns.includes(column)))
        .filter(np => np)
        .filter((np, index, self) => self.indexOf(np) === index);

    if (potentialNumberPartsNextToGears.length == 2) {
        const [{num: num1}, {num: num2}] = potentialNumberPartsNextToGears;
        return num1 * num2;
    }

    return 0;
}

const findGearRatios = text => {
    const numberParts = findPartNumbers(text);
    const gears = allGears(text);

    return gears
        .map(({row, column}) => computeAdjacentCoordinates({row, columns: [column]}))
        .map(gear => computeGearRatio(gear, numberParts))
        .filter(ratio => ratio !== 0);
}

const example2 = readFile(3, 'example2');
const input2 = readFile(3, 'input2');
console.log(sum(findGearRatios(example2))); // 467835
console.log(sum(findGearRatios(input2))); // 75847567