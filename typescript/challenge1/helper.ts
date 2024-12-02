export const parseArrays = (str: string[]) => str.filter(s => s !== '').map(s => s.split('   '))
export const toNumberArray = (str: string[][], index: number) => {
    return str.reduce((res, [a, b]) => {
        if (index === 0) {
            return [...res, +a];
        }
        return [...res, +b];
    }, []) as number[];
}
export const sort = (array: number[], sortDirection: 'DESC' | 'ASC') => {
    return array.sort((a, b) => {
        if (sortDirection === 'ASC') {
            return a - b;
        }
        return b - a;
    })
}
export const distance = (a, b) => Math.abs(a - b);

export const similarity = (a, arr) => arr.reduce((res, x) => x === a ? res + 1 : res, 0);
