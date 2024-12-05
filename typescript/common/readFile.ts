import * as fs from 'fs';
import * as path from 'path';

export const readFile = (day, name, delimiter = '\n') => {
    const p = path.resolve(__dirname, `../../input/day${day}/${name}.txt`);
    return fs.readFileSync(p, 'utf8').split(delimiter).filter(str => str !== '');
}
