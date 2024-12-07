const ruleSeparator = '|';
const pageSeparator = ',';

export const parseInput = (input: string[]) => {
    const rules = input.filter(i => i.includes(ruleSeparator)).map(i => i.split(ruleSeparator))
    const pages = input.filter(i => i.includes(pageSeparator)).map(i => i.split(pageSeparator));

    return {rules, pages};
}

export const complies = (page: string, pages: string[], rules: string[][]) => {
    return rules.every(([a, b]) => {
        if ((page === a && pages.includes(b)) || (page === b && pages.includes(a))) {
            return pages.indexOf(b) > pages.indexOf(a);
        }

        return true;
    });
}

export const middlePage = (pages: string[]) => {
    return +pages[(pages.length - 1) / 2];
}

export const fixOrdering = (pageNumbers: string[], rules: string[][]) => {
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
