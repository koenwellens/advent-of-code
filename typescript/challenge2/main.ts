import {input} from './input';

const losesFrom: Record<string, string> = {
    X: 'C',
    Y: 'A',
    Z: 'B',
    A: 'Z',
    B: 'X',
    C: 'Y',
}

const winsFrom: Record<string, string> = {
    X: 'B',
    Y: 'C',
    Z: 'A',
    A: 'Y',
    B: 'Z',
    C: 'X',
}
const drawWhen: Record<string, string> = {
    X: 'A',
    Y: 'B',
    Z: 'C',
    A: 'X',
    B: 'Y',
    C: 'Z',
}

const outcomeWhen: Record<string, string> = {
    X:'loss',
    Y:'draw',
    Z:'win',
}

const handCalculator: Record<string, Record<string, string>> = {
    win: winsFrom,
    loss: losesFrom,
    draw: drawWhen,
}

const score: Record<string, number> = {
    X: 1,
    Y: 2,
    Z: 3,
    win: 6,
    loss: 0,
    draw: 3,
}

const rounds = input.split('\n');
// console.log(rounds);

const totalScore = rounds.reduce((res, round) => {
    const game = round.split(' ');
    const opponentHand = game[0];
    const yourHand = game[1];
    if(losesFrom[yourHand] === opponentHand) {
        return res + score.win + score[yourHand];
    } else if(winsFrom[yourHand] === opponentHand) {
        return res + score.loss + score[yourHand];
    }
    return res + score.draw + score[yourHand];
}, 0);

console.log('Solution for challenge 1: ', totalScore);

const totalScoreWithExpectedOutcome = rounds.reduce((res, round) => {
    const game = round.split(' ');
    const opponentHand = game[0];
    const expectedOutcome = game[1];
    const newOutcome = outcomeWhen[expectedOutcome];

    const yourHand = handCalculator[newOutcome][opponentHand];
    return res + score[newOutcome] + score[yourHand];
}, 0);

console.log('Solution for challenge 2: ', totalScoreWithExpectedOutcome);
