const findMatches = (str: string, regex: RegExp) => {
    const matches: RegExpExecArray[] = [];
    let match;

    while ((match = regex.exec(str)) !== null) {
        matches.push(match);
    }

    return matches;
}

export const findMulNumbers = (str: string, doApply: (match: RegExpExecArray) => boolean = () => true) => {
    const regex = /mul\((\d{1,3}),(\d{1,3})\)/g;
    const allMatches = findMatches(str, regex);
    const matches = [];

    for (let match of allMatches) {
        if (doApply(match)) {
            const x = parseInt(match[1], 10);
            const y = parseInt(match[2], 10);
            matches.push([x, y]);
        }
    }

    return matches;
}

export const getAllIndexesFor = (str: string, regex: RegExp) => {
    return findMatches(str, regex)
        .reduce((res, m) => [...res, m.index], [] as number[]);
}

export const convertToIntervals = (startIndexes: number[], endIndexes: number[], finalEndIndex: number) => {
    const intervals = [];
    let endIndexPointer = 0;

    startIndexes.forEach((start) => {
        while (
            endIndexPointer < endIndexes.length &&
            endIndexes[endIndexPointer] < start
            ) {
            endIndexPointer++;
        }

        const end = endIndexPointer < endIndexes.length
            ? endIndexes[endIndexPointer]
            : finalEndIndex;

        intervals.push([start, end]);
    });

    return intervals;
}
