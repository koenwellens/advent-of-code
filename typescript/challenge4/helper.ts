export const X = 'X';
export const M = 'M';
export const A = 'A';
export const S = 'S';
type XMAS = 'X' | 'M' | 'A' | 'S'
const checkFor = (x: number, y: number, char: XMAS, input: string[]) => {
    return input[y]?.[x] === char;
}

const checkLeft = (x: number, y: number, chars: XMAS[], input: string[]) => {
    return chars.every((c, i) => checkFor(x - (i + 1), y, c, input));
}

const checkRight = (x: number, y: number, chars: XMAS[], input: string[]) => {
    return chars.every((c, i) => checkFor(x + (i + 1), y, c, input));
}

const checkUp = (x: number, y: number, chars: XMAS[], input: string[]) => {
    return chars.every((c, i) => checkFor(x, y - (i + 1), c, input));
}

const checkDown = (x: number, y: number, chars: XMAS[], input: string[]) => {
    return chars.every((c, i) => checkFor(x, y + (i + 1), c, input));
}

const checkDiagUpLeft = (x: number, y: number, chars: XMAS[], input: string[]) => {
    return chars.every((c, i) => checkFor(x - (i + 1), y - (i + 1), c, input));
}

const checkDiagDownRight = (x: number, y: number, chars: XMAS[], input: string[]) => {
    return chars.every((c, i) => checkFor(x + (i + 1), y + (i + 1), c, input));
}

const checkDiagUpRight = (x: number, y: number, chars: XMAS[], input: string[]) => {
    return chars.every((c, i) => checkFor(x + (i + 1), y - (i + 1), c, input));
}

const checkDiagDownLeft = (x: number, y: number, chars: XMAS[], input: string[]) => {
    return chars.every((c, i) => checkFor(x - (i + 1), y + (i + 1), c, input));
}

export const numberOfDirectionsOfXmas = (x: number, y: number, input: string[]) => {
    return [
        checkLeft(x, y, [M, A, S], input),
        checkRight(x, y, [M, A, S], input),
        checkUp(x, y, [M, A, S], input),
        checkDown(x, y, [M, A, S], input),
        checkDiagUpLeft(x, y, [M, A, S], input),
        checkDiagUpRight(x, y, [M, A, S], input),
        checkDiagDownRight(x, y, [M, A, S], input),
        checkDiagDownLeft(x, y, [M, A, S], input),
    ].filter(check => check).length;
}

export const numberOfXmasStars = (x: number, y: number, input: string[]) => {
    return [
        checkDiagUpLeft(x, y, [M], input) && checkDiagDownRight(x, y, [S], input) && checkDiagDownLeft(x, y, [M], input) && checkDiagUpRight(x, y, [S], input),
        checkDiagUpLeft(x, y, [S], input) && checkDiagDownRight(x, y, [M], input) && checkDiagDownLeft(x, y, [M], input) && checkDiagUpRight(x, y, [S], input),
        checkDiagUpLeft(x, y, [M], input) && checkDiagDownRight(x, y, [S], input) && checkDiagDownLeft(x, y, [S], input) && checkDiagUpRight(x, y, [M], input),
        checkDiagUpLeft(x, y, [S], input) && checkDiagDownRight(x, y, [M], input) && checkDiagDownLeft(x, y, [S], input) && checkDiagUpRight(x, y, [M], input),
    ].filter(check => check).length;
}
