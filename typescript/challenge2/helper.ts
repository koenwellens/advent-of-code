const allDifferencesAreOkay = (numbers: number[], differences: number[]) => {
    for (let i = 0; i + 1 < numbers.length; i++) {
        if (!differences.includes(numbers[i + 1] - numbers[i])) {
            return false;
        }
    }
    return true;
}

export const parse = (str: string) => str.split(' ').map(s => +s);

export const allAreSafe = (numbers: number[]) => {
    return allDifferencesAreOkay(numbers, [1, 2, 3]) || allDifferencesAreOkay(numbers, [-1, -2, -3])
}

export const allButOneLevelAreSafe = (numbers: number[]) => {
    for (let i = 0; i < numbers.length; i++) {
        const newNumbers = [...numbers];
        newNumbers.splice(i, 1);
        if (allAreSafe(newNumbers)) {
            return true;
        }
    }

    return false;
}
