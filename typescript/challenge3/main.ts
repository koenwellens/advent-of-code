import {input} from './input';

const rugsacks = input.split('\n');

const priority = (character) => 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'.indexOf(character) + 1;

const itemsInOther = (items1, items2) => items1.split('')
    .filter(item => items2.indexOf(item) >= 0)
    .reduce((res, item) => res + item, '');

const removeDuplicaties = str => {
    const array = str.split('');
    return array.filter((item, index) => array.indexOf(item) === index)
        .join('');
}

const sumOfPriorities = (itemsPerRugsack) => itemsPerRugsack
    .map(items => removeDuplicaties(items))
    .map(items => priority(items[0]))
    .reduce((res, prio) => res + prio, 0);

const groupRugsacks = (rugsacks) => {
    const allGroups = [];
    let group;
    for (let i = 0; i < rugsacks.length; i++) {
        if (i % 3 === 0) {
            group = [];
        }
        group.push(rugsacks[i]);
        if (i % 3 === 2) {
            allGroups.push(group);
        }
    }
    return allGroups;
}

const itemsPerRugsack = rugsacks.map(rugsack => {
    const compartmentSize = rugsack.length / 2;
    const compartment1 = rugsack.substring(0, compartmentSize);
    const compartment2 = rugsack.substring(compartmentSize);

    return itemsInOther(compartment1, compartment2);
});

const rugsacksPerGroup = groupRugsacks(rugsacks);

const itemsPerGroup = rugsacksPerGroup.map(rugsacksPerGroup => {
    const rugsack1 = rugsacksPerGroup[0];
    const rugsack2 = rugsacksPerGroup[1];
    const rugsack3 = rugsacksPerGroup[2];

    return itemsInOther(rugsack1, itemsInOther(rugsack2, itemsInOther(rugsack1, rugsack3)));
});


console.log('Solution 1: ', sumOfPriorities(itemsPerRugsack));
console.log('Solution 2: ', sumOfPriorities(itemsPerGroup));
