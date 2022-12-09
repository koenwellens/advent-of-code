import {input} from "./input";

function getCategoriesPerElfSorted(elfiesString: string) : number[] {
    const elves = elfiesString.split('\n\n');
    const caloriesPerElf = elves.map(elf => elf.split('\n').reduce((res, e) => res + Number(e.trim()), 0));
    caloriesPerElf.sort((e1, e2) => e2 - e1);
    return caloriesPerElf;
}

const categoriesPerElfSorted = getCategoriesPerElfSorted(input);
console.log('Solution for question 1:', categoriesPerElfSorted[0]);
console.log('Solution for question 2:', categoriesPerElfSorted[0] + categoriesPerElfSorted[1] + categoriesPerElfSorted[2]);
