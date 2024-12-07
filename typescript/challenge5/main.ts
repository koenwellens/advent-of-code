import {readFile} from '../common/readFile';

const exampleInput = readFile('5', 'example');
const input = readFile('5', 'input');

const ruleSeparator = '|';
const pageSeparator = ',';

const parseInput = (input: string[]) => {
    const rules = input.filter(i => i.includes(ruleSeparator)).map(i => i.split(ruleSeparator))
    const pages = input.filter(i => i.includes(pageSeparator)).map(i => i.split(pageSeparator));

    return {rules, pages};
}

const complies = (page: string, pages: string[], rules: string[][]) => {
    return rules.every(([a, b]) => {
        if ((page === a && pages.includes(b)) || (page === b && pages.includes(a))) {
            return pages.indexOf(b) > pages.indexOf(a);
        }

        return true;
    });
}

const middlePage = (pages: string[]) => {
    return +pages[(pages.length - 1) / 2];
}


const algorithm1 = (input: string[]) => {
    const {rules, pages} = parseInput(input);

    let result = 0;
    for (let pageNumbers of pages) {
        if (pageNumbers.every(page => complies(page, pageNumbers, rules))) {
            result += middlePage(pageNumbers);
        }

    }

    return result;
}

const fixOrdering = (pageNumbers: string[], rules: string[][]) => {
    const newOrdering = [...pageNumbers];
    newOrdering.sort((a, b
    ) => {
        if (complies(a, [a, b], rules)) {
            return -1;
        }
        if (complies(b, [a, b], rules)) {
            return 1;
        }

        return 0;
    });

    return newOrdering;
}

const algorithm2 = (input: string[]) => {
    const {rules, pages} = parseInput(input);

    let result = 0;
    for (let pageNumbers of pages) {
        if (pageNumbers.some(page => !complies(page, pageNumbers, rules))) {
            result += middlePage(fixOrdering(pageNumbers, rules));
        }
    }

    return result;
}

// 143
console.log('example 1', algorithm1(exampleInput));
// 6505
console.log('input 1', algorithm1(input));

// 123
console.log('example 2', algorithm2(exampleInput));
// 6897
console.log('input 2', algorithm2(input));

