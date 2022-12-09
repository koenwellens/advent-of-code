// import {input} from './example-input';
import {input} from './input';

const SIZE_THRESHOLD = 100000;

interface Data {
    name: string;
}

class File implements Data {
    constructor(public name: string, public size: number) {
    }
}

class Directory implements Data {
    constructor(public name: string, public children: Data[] = []) {
    };
}

const isNoCommand = str => str.indexOf('$') < 0;
const isLsLine = str => str === '$ ls';
const isCdLine = str => str.indexOf('$ cd') === 0;
const isGoBackLine = str => str === '$ cd ..';

const parseNameFromCdLine = str => str.substring('$ cd '.length);

const isFileListing = str => str.split(' ')[0] !== 'dir';
const parseDirectory = str => new Directory(str.split(' ')[1]);
const parseFile = str => {
    const file = str.split(' ');
    return new File(file[1], Number(file[0]));
};


const convertInputToTree = str => {
    const terminalOutput = str.split('\n');

    const diskStructure = new Directory('/');
    let currentDirectoryStrucure = [diskStructure];

    for (let i = 1; i < terminalOutput.length; i++) {
        const line = terminalOutput[i];
        const currentDirectory = currentDirectoryStrucure[currentDirectoryStrucure.length - 1];
        if (isLsLine(line)) {
            continue;
        }

        if (isNoCommand(line)) {
            const data = isFileListing(line) ? parseFile(line) : parseDirectory(line);
            currentDirectory.children.push(data);
        }

        if (isCdLine(line)) {
            if (isGoBackLine(line)) {
                currentDirectoryStrucure.pop();
            } else {
                const directoryName = parseNameFromCdLine(line);
                const nextDirectory: Directory = currentDirectory.children.filter(d => d instanceof Directory && d.name === directoryName)[0] as Directory;
                currentDirectoryStrucure.push(nextDirectory);
            }
        }
    }
    return diskStructure;
}

const computeDirectorySize = directory => {
    return directory.children.reduce((res, child) => {
        if (child instanceof Directory) {
            return res + computeDirectorySize(child);
        }
        return res + child.size;
    }, 0);
}

const computeSizePerDirectory = directory => {
    return [
        {...directory, size: computeDirectorySize(directory)},
        ...directory.children.filter(child => child instanceof Directory).reduce((res, child) => [...res, ...computeSizePerDirectory(child)], []),
    ];
}

const directoriesBelowTreshold = (directories, treshold) => directories.filter(d => d.size <= treshold)

const tree = convertInputToTree(input);
const sizes = computeSizePerDirectory(tree);
const wantedDirectories = directoriesBelowTreshold(sizes, SIZE_THRESHOLD);
const totalWantedDirectoriesSize = wantedDirectories.reduce((res, dir) => res + dir.size, 0);

console.log('Solution 1', totalWantedDirectoriesSize);

const FREE_UP_SPACE = 30000000;
const TOTAL_DISK_SPACE = 70000000;

const currentFreeSpace = TOTAL_DISK_SPACE - sizes.filter(d => d.name === '/')[0].size;
const spaceToFree = FREE_UP_SPACE - currentFreeSpace;

const sortedBySize = [...sizes].sort((d1, d2) => d1.size - d2.size);
const firstDirectoryToDelete = sortedBySize.filter(d => d.size >= spaceToFree)[0];
console.log('Solution 2', firstDirectoryToDelete.name, firstDirectoryToDelete.size);
