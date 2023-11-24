const CHAMBER_WIDTH = 7;

export const printBoard = (chamber, shape, nextShapeIdx) => {
    const allCoordinates = chamber;
    let minY = chamber.reduce((res, [x, y]) => res < y ? res : y, 9999999);
    minY = minY === 9999999 ? 1 : minY;

    console.log(`Shape ${nextShapeIdx} is falling!!`);
    for (let y = shape.getHighestY(); y >= minY - 1; y--) {
        const line = [0, 1, 2, 3, 4, 5, 6, 7, 8].map(x => {
            if (y === 0) {
                if (x === 0 || x === CHAMBER_WIDTH + 1) {
                    return '+';
                }
                return '-';
            }
            if (x === 0 || x === CHAMBER_WIDTH + 1) {
                return '|';
            }
            if (shape.coordinates.some(([xs, ys]) => xs === x && ys === y)) {
                return '@';
            }
            if (allCoordinates.some(([xc, yc]) => xc === x && yc === y)) {
                return '#'
            }
            return '.';
        }).join('');
        console.log(line);
    }
    console.log('');
}
