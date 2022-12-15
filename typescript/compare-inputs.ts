import {readFileSync} from 'fs';
import {join} from 'path';

const MAX_DAY = 14;
const toInputFilePath1 = day => day < 10 ? `input/day0${day}_input` : `input/day${day}_input`;
const toInputFilePath2 = day => `typescript/challenge${day}/input.ts`;
const koenSubstring = 'export const input = `';

// ✅ read file SYNCHRONOUSLY
function syncReadFile(filename: string) {
    const dirName = __dirname.split('/');
    const actualDir = dirName.filter((val, idx) => idx < dirName.length - 1).join('/');

    return readFileSync(join(actualDir, filename), 'utf-8');
}

for (let day = 1; day <= MAX_DAY; day++) {
    const fileNameJurgen = toInputFilePath1(day);
    const fileNameKoen = toInputFilePath2(day);

    const jurgenInput = syncReadFile(fileNameJurgen);
    const koenInput = syncReadFile(fileNameKoen);

    if (koenInput.includes(jurgenInput)) {
        console.log(`Inputs for day ${day} are the same :-)`);
    } {
        console.log(`Inputs for day ${day} are different! 🤯`);
    }

}
