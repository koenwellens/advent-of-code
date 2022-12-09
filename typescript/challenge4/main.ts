import {input} from './input';

const toSection = str => {
    const boundaries = str.split('-');
    return {
        min: Number(boundaries[0]),
        max: Number(boundaries[1]),
    }
}

const isFullyContained = (section1, section2) => section1.min >= section2.min && section1.max <= section2.max
const hasPartialOverlap = (section1, section2) => section1.max >= section2.min && section1.max <= section2.max

const pairs = input.split('\n');

const pairsWithFullyContainedSections = pairs.map(pair => {
    const sections = pair.split(',');
    const section1 = toSection(sections[0]);
    const section2 = toSection(sections[1]);

    return isFullyContained(section1, section2) || isFullyContained(section2, section1);
});

const pairsWithPartiallyOveralppingSections = pairs.map(pair => {
    const sections = pair.split(',');
    const section1 = toSection(sections[0]);
    const section2 = toSection(sections[1]);

    return hasPartialOverlap(section1, section2) || hasPartialOverlap(section2, section1);
});

const numberOfPairsWithFullyContainedSections = pairsWithFullyContainedSections.filter(val => !!val).length
const numberOfPairsWithPartiallyOverlappingSections = pairsWithPartiallyOveralppingSections.filter(val => !!val).length;
console.log('Solution 1: ', numberOfPairsWithFullyContainedSections);
console.log('Solution 2: ', numberOfPairsWithPartiallyOverlappingSections);
