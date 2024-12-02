import {readFile} from '../common/readFile';

const example1 = readFile(12, 'example1');
const input1 = readFile(12, 'input1');

const parse = lines => {
    return lines.map(line => line.split(" "))
        .map(([records, groups]) => ({records, groups}));
}

const enhance = (lines, repititions = 5) => {
    return lines.map(({records, groups}) => ({
        records: Array.from({length: repititions}, () => records).join('?'),
        groups: Array.from({length: repititions}, () => groups).join(','),
    }));
}

const parseToRegex = groups => {
    return new RegExp('^\\.*' + groups.split(",").map((n: string) => `#{${n}}`).join('\\.+') + '\\.*$');
}

const generateCombinations = (str: string) => {
    return {
        [Symbol.iterator]: function* () {
            const stack: { str: string; index: number }[] = [];
            stack.push({ str, index: 0 });

            while (stack.length > 0) {
                const { str, index } = stack.pop()!;

                if (index < str.length) {
                    if (str[index] === '?') {
                        stack.push({ str: str.substring(0, index) + '.' + str.substring(index + 1), index: index + 1 });
                        stack.push({ str: str.substring(0, index) + '#' + str.substring(index + 1), index: index + 1 });
                    } else {
                        stack.push({ str, index: index + 1 });
                    }
                } else {
                    yield str;
                }
            }
        },
    } as IterableIterator<string>;
}

const countPossibleArrangements = (input: { records: string, groups: string }[], debug = false) => {
    let result = 0;

    for (let idx = 0; idx < input.length; idx++) {
        const {records, groups} = input[idx];
        if(debug) {
            console.log(records);
        }
        const regex = parseToRegex(groups);
        for (const arrangement of generateCombinations(records)) {
            if (regex.test(arrangement)) {
                result++;
            }
        }
    }

    return result;
}


const inputOfExample1 = enhance(parse(example1), 1);
console.log(countPossibleArrangements(inputOfExample1)); // 21
const inputOfInput1 = enhance(parse(input1), 1);
console.log(countPossibleArrangements(inputOfInput1)); // 7118

const example2 = readFile(12, 'example2');
const input2 = readFile(12, 'input2');

const inputOfExample2 = enhance(parse(example2), 5);
console.log(countPossibleArrangements(inputOfExample2, true)); // 525152
// const inputOfInput2 = enhance(parse(input2), 5);
// console.log(countPossibleArrangements(inputOfInput2)); // ????