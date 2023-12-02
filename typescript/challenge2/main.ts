import {readFile} from '../common/readFile';

const example1 = readFile(2, 'example1');
const input1 = readFile(2, 'input1');

interface CubeSet {
    reds: number;
    greens: number;
    blues: number;
}

type Bag = CubeSet;

class Game {
    constructor(public id: string, private cubeSets: CubeSet[]) {
    }

    isPossible(bag: Bag) {
        return this.cubeSets.every(({
                                        reds,
                                        greens,
                                        blues
                                    }) => reds <= bag.reds && greens <= bag.greens && blues <= bag.blues);
    }

    power() {
        return this.minimumNumberOf('reds')
            * this.minimumNumberOf('greens')
            * this.minimumNumberOf('blues');
    }

    private minimumNumberOf(cube: string) {
        return this.cubeSets.reduce((res, cubeSet) => cubeSet[cube] > res ? cubeSet[cube] : res, 0);
    }
}

const lineToGame = str => {
    // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    const [gameId, sets] = str.split(": ");
    const [g, a, m, e, space, ...id] = gameId;
    const cubeSetsStrings = sets.split("; ");
    const cubeSets = cubeSetsStrings
        .map(s => {
            const [, blues] = new RegExp('(\\d+)\\sblue').exec(s) || [0, 0];
            const [, reds] = new RegExp('(\\d+)\\sred').exec(s) || [0, 0];
            const [, greens] = new RegExp('(\\d+)\\sgreen').exec(s) || [0, 0];
            return {reds: +reds, blues: +blues, greens: +greens};
        });
    return new Game(id.join(''), cubeSets);
}

const sumOfPossibleIds = (games: Game[], bag: Bag) =>
    games.filter(game => game.isPossible(bag))
        .map(({id}) => +id)
        .reduce((res, id) => res + id, 0);

const bag = {reds: 12, greens: 13, blues: 14};
console.log(sumOfPossibleIds(example1.map(s => lineToGame(s)), bag));
console.log(sumOfPossibleIds(input1.map(s => lineToGame(s)), bag));

const example2 = readFile(2, 'example2');
const input2 = readFile(2, 'input2');

const sumOfPowers = (games: Game[]) => games.map(game => game.power()).reduce((res, p) => res + p, 0);
console.log(sumOfPowers(example2.map(s => lineToGame(s))));
console.log(sumOfPowers(input2.map(s => lineToGame(s))));