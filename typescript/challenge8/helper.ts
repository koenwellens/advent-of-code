type Coordinate = { x: number, y: number; };
type Antennas = { [key: string]: Coordinate[]; }

export const parse = (input: string[]) => {
    const result = {};

    for (let y = 0; y < input.length; y++) {
        for (let x = 0; x < input[y].length; x++) {
            const value = input[y][x];
            if (value !== '.') {
                result[value] = [...(result[value] || []), {x, y}];
            }
        }
    }

    return result;
};

export const computeAntinodes = (antennas: Antennas) => {
    const result: Coordinate[] = [];

    for (let antenna of Object.getOwnPropertyNames(antennas)) {
        const coordinates = antennas[antenna];
        for (let a = 0; a < coordinates.length; a++) {
            for (let b = a + 1; b < coordinates.length; b++) {
                const coordA = coordinates[a];
                const coordB = coordinates[b];
                const diffX = coordA.x - coordB.x;
                const diffY = coordA.y - coordB.y;

                result.push(
                    {x: coordA.x + diffX, y: coordA.y + diffY},
                    {x: coordB.x - diffX, y: coordB.y - diffY},
                );
            }
        }
    }


    return result;
}

export const computeAntinodesWithHarmonies = (antennas: Antennas, boundaries: number[]) => {
    const result: Coordinate[] = [];

    for (let antenna of Object.getOwnPropertyNames(antennas)) {
        const coordinates = antennas[antenna];
        for (let a = 0; a < coordinates.length; a++) {
            for (let b = a + 1; b < coordinates.length; b++) {
                const coordA = coordinates[a];
                const coordB = coordinates[b];
                const diffX = coordA.x - coordB.x;
                const diffY = coordA.y - coordB.y;
                const numberOnX = Math.abs(Math.round(boundaries[0] / diffX));
                const numberOnY = Math.abs(Math.round(boundaries[1] / diffY));
                let i = 0, j = 0;
                while (i < numberOnX && j < numberOnY) {
                    result.push(
                        {x: coordA.x + i * diffX, y: coordA.y + j * diffY},
                        {x: coordB.x - i * diffX, y: coordB.y - j * diffY},
                    );
                    i++;
                    j++;
                }
            }
        }
    }


    return result;
}
