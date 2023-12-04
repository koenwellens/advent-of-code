import {readFile} from '../common/readFile';

const example1 = readFile(4, 'example1');
const input1 = readFile(4, 'input1');

const toCards = lines => {
    return lines.map(line => {
        const [cardIdPart, allNumbersPart] = line.split(": ");
        const [cardId] = cardIdPart.split(" ").map(num => num.trim()).filter(s => !isNaN(s));
        const [allWinningNumbers, allPotentialNumbers] = allNumbersPart.split(" | ");
        const winningNumbers = allWinningNumbers.split(" ").map(num => +num.trim());
        const potentialNumbers = allPotentialNumbers.split(" ").filter(n => n.trim()).map(num => +num.trim());
        return {cardId, winningNumbers, potentialNumbers};
    });
}

const numberOfWinningNumbers = (potentialNumbers: number[], winningNumbers: number[]) => {
    return potentialNumbers.filter(num => winningNumbers.includes(num)).length
}

const computePoints = lines => {
    return toCards(lines)
        .map(({potentialNumbers, winningNumbers}) => numberOfWinningNumbers(potentialNumbers, winningNumbers))
        .filter(num => num)
        .map(num => 2 ** (num - 1))
        .reduce((res, num) => res + num, 0);
}

console.log(computePoints(example1)); // 13
console.log(computePoints(input1)); // 19135

const example2 = readFile(4, 'example2');
const input2 = readFile(4, 'input2');

const computeNumberOfScratchCards = text => {
    const cards = toCards(text);
    let result = 0;
    const cardsToHandle = cards.map(card => ({cardId: card.cardId, value: 1, card}));
    let cardIndex = 0;
    do {
        const card = cards[cardIndex];
        const nextIndexes = numberOfWinningNumbers(card.potentialNumbers, card.winningNumbers);
        const numberOfCardsCurrentlyHandling = cardsToHandle[cardIndex].value;
        result += numberOfCardsCurrentlyHandling;
        cardsToHandle[cardIndex].value = 0;
        for (let next = cardIndex + 1; next < cardIndex + nextIndexes + 1; next++) {
            cardsToHandle[next].value += numberOfCardsCurrentlyHandling;
        }
        cardIndex++;
    } while (cardsToHandle.filter(({value}) => value).length > 0);

    return result;
}

console.log(computeNumberOfScratchCards(example2)); // 30
console.log(computeNumberOfScratchCards(input2)); // 5704953