import {readFile} from '../common/readFile';

const example1 = readFile(1, 'example1');
const input1 = readFile(1, 'input1');

const getCalibrationValue = (line: string) => {
    const [...chars] = line;
    const onlyDigits = chars.filter(a => !isNaN(+a));
    const onlyDigitsReversed = [...onlyDigits].reverse();
    return onlyDigits[0] + onlyDigitsReversed[0];
}

const mapWordsToDigits = (line: string) => {
    const words = {one: '1', two: '2', three: '3', four: '4', five: '5', six: '6', seven: '7', eight: '8', nine: '9'};
    let text = line;
    Object.getOwnPropertyNames(words).forEach(word => {
        text = text.replace(new RegExp(word, 'gi'), word + words[word] + word);
    });

    return text;
}

const sumOfCalibrations = lines => lines.map(line => getCalibrationValue(line)).reduce((sum, val) => sum + Number(val), 0);
console.log(sumOfCalibrations(example1));
console.log(sumOfCalibrations(input1));

const example2 = readFile(1, 'example2');
const input2 = readFile(1, 'input2');
console.log(sumOfCalibrations(example2.map(line => mapWordsToDigits(line))));
console.log(sumOfCalibrations(input2.map(line => mapWordsToDigits(line))));