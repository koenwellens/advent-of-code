import {readFile} from '../common/readFile';
import {computeAntinodes, computeAntinodesWithHarmonies, parse} from "./helper";

const exampleInput = readFile('8', 'example');
const input = readFile('8', 'input');


const algorithm1 = (input: string[]) => {
    const antennas = parse(input);
    const boundaries = [input[0].length - 1, input.length - 1];

    const antinodes = computeAntinodes(antennas).filter(({
                                                             x,
                                                             y
                                                         }) => x >= 0 && x <= boundaries[0] && y >= 0 && y <= boundaries[1])
        .filter(({
                     x,
                     y
                 }, i, array) => array.findIndex(({
                                                      x: a,
                                                      y: b
                                                  }) => a === x && y === b) === i)

    return antinodes.length;
}


const algorithm2 = (input: string[]) => {
    const antennas = parse(input);
    const boundaries = [input[0].length - 1, input.length - 1];

    const antinodes = computeAntinodesWithHarmonies(antennas, boundaries).filter(({
                                                             x,
                                                             y
                                                         }) => x >= 0 && x <= boundaries[0] && y >= 0 && y <= boundaries[1])
        .filter(({
                     x,
                     y
                 }, i, array) => array.findIndex(({
                                                      x: a,
                                                      y: b
                                                  }) => a === x && y === b) === i)

    return antinodes.length;
}

// 14
console.log('example 1', algorithm1(exampleInput));
// 280
console.log('input 1', algorithm1(input));

// 34
console.log('example 2', algorithm2(exampleInput));
// 958
console.log('input 2', algorithm2(input));
