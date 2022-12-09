import {input} from './input';
import {input1, input2, input3, input4, input5} from './example-input';

const PACKET_BUFFER_SIZE = 4;
const MESSAGE_BUFFER_SIZE = 14;

const isOnlyInOnce = (str, chr) => {
    return str.indexOf(chr) === str.lastIndexOf(chr);
}

const lastXCharactersAreDifferent = (str, numberOfDifferentChars) => {
    const len = str.length;
    if (len >= numberOfDifferentChars) {
        const lastPartOfString = str.substring(len - numberOfDifferentChars, len);
        return lastPartOfString.split('').every(chr => isOnlyInOnce(lastPartOfString, chr));
    }

    return false;
}

const getMarker = (str, numbah) => {
    for (let i = 0; i < str.length; i++) {
        if (lastXCharactersAreDifferent(str.substring(0, i), numbah)) {
            return i;
        }
    }
    return -1;
}

console.log('First exercise');
console.log('Example 1: ', getMarker(input1, PACKET_BUFFER_SIZE));
console.log('Example 2: ', getMarker(input2, PACKET_BUFFER_SIZE));
console.log('Example 3: ', getMarker(input3, PACKET_BUFFER_SIZE));
console.log('Example 4: ', getMarker(input4, PACKET_BUFFER_SIZE));
console.log('Example 5: ', getMarker(input5, PACKET_BUFFER_SIZE));
console.log('Solution 1: ', getMarker(input, PACKET_BUFFER_SIZE));


console.log('Second exercise');
console.log('Example 1: ', getMarker(input1, MESSAGE_BUFFER_SIZE));
console.log('Example 2: ', getMarker(input2, MESSAGE_BUFFER_SIZE));
console.log('Example 3: ', getMarker(input3, MESSAGE_BUFFER_SIZE));
console.log('Example 4: ', getMarker(input4, MESSAGE_BUFFER_SIZE));
console.log('Example 5: ', getMarker(input5, MESSAGE_BUFFER_SIZE));
console.log('Solution 2: ', getMarker(input, MESSAGE_BUFFER_SIZE));
