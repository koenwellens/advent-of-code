import {input as example} from './example-input';
import {input} from './input';

const parseOperation = str => {
    const commands = str.split(' ');
    if (commands[3] === '*') {
        if (commands[4] === 'old') {
            return x => x * x;
        }
        return x => x * BigInt(commands[4]);
    }
    if (commands[3] === '+') {
        if (commands[4] === 'old') {
            return x => x + x;
        }
        return x => x + BigInt(commands[4]);
    }
}

const parseMonkey = str => {
    const monkeyData = str.split('\n');
    const testNumber = BigInt(monkeyData[3].substring('  Test: divisible by '.length));
    return {
        items:monkeyData[1].substring('  Starting items: '.length).split(', ').map(str => BigInt(str)),
        operation: parseOperation(monkeyData[2].substring('  Operation: '.length)),
        testNumber,
        test: (x: bigint) => x % testNumber === BigInt(0),
        nextMonkeyIfTrue: Number(monkeyData[4].substring('    If true: throw to monkey '.length)),
        nextMonkeyIfFalse: Number(monkeyData[5].substring('    If false: throw to monkey '.length)),
        itemsInspected: 0,
    };
}

const parseMonkeys = str => {
    const monkeysStr = str.split('\n\n');
    return monkeysStr.map(s => parseMonkey(s));
}

const addRelief = (worryLevel: bigint, manageablifier: bigint) => worryLevel / BigInt(3);
const lostRelief = (worryLevel: bigint, manageablifier: bigint) => worryLevel % manageablifier;

const playRound = (monkeys, reliefFn = addRelief, manageablifier = BigInt(0)) => {
    monkeys.forEach((monkey, monkeyIndex) => {
        monkey.items.forEach(item => {
            monkey.itemsInspected++;
            const newWorryLevel = monkey.operation(item);
            const newWorryLevelAfterRelief = reliefFn(newWorryLevel, manageablifier);
            if (monkey.test(newWorryLevelAfterRelief)) {
                monkeys[monkey.nextMonkeyIfTrue].items.push(newWorryLevelAfterRelief);
            } else {
                monkeys[monkey.nextMonkeyIfFalse].items.push(newWorryLevelAfterRelief);
            }
        });
        monkey.items = [];
    });
}

const playGame = (monkeys, numberOfRounds = 20, reliefFn = addRelief, manageablifier = monkeys.reduce((res, {testNumber}) => res * testNumber, BigInt(1))) => {
    for (let i = 0; i < numberOfRounds; i++) {
        playRound(monkeys, reliefFn, manageablifier);
    }
}

const computeLevelOfMonkeyBusiness = monkeys => {
    const sortedMonkeys = [...monkeys].sort((m1, m2) => m2.itemsInspected - m1.itemsInspected);
    return sortedMonkeys[0].itemsInspected * sortedMonkeys[1].itemsInspected;
}

const exampleMonkeys = parseMonkeys(example);
playGame(exampleMonkeys);
console.log('Example 1', computeLevelOfMonkeyBusiness(exampleMonkeys));

const inputMonkeys = parseMonkeys(input);
playGame(inputMonkeys);
console.log('Solution 1', computeLevelOfMonkeyBusiness(inputMonkeys));

const exampleMonkeys2 = parseMonkeys(example);
playGame(exampleMonkeys2, 10000, lostRelief);
console.log(JSON.stringify(exampleMonkeys2.map(({itemsInspected}) => itemsInspected)));
console.log('Example 2', computeLevelOfMonkeyBusiness(exampleMonkeys2));

const inputMonkeys2 = parseMonkeys(input);
playGame(inputMonkeys2, 10000, lostRelief);
console.log(JSON.stringify(inputMonkeys2.map(({itemsInspected}) => itemsInspected)));
console.log('Solution 2', computeLevelOfMonkeyBusiness(inputMonkeys2));
