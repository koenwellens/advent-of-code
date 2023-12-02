import * as fs from 'fs';

export const readFile = (day, name, delimiter = '\n') => {
    const path = `/Users/koenwellens/Projects/Wellens_IT/advent-of-code-2022/input/koen/day${day}/${name}.txt`;
    return fs.readFileSync(path, 'utf8').split(delimiter);
}