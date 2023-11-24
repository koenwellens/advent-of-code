// backup
class Shape {

    private lowered = 0;
    private movedHorizontally = 0;

    constructor(public coordinates: number[][],
                public numberOfCoordinates,
                private highestX,
                private highestY,
                private lowestX,
                private lowestY) {
    }

    public moveLeft(chamber: number[][], shouldCheckRocks: boolean) {
        if ((this.lowestX + this.movedHorizontally !== 1)
            && (!shouldCheckRocks || !chamber.some(([x1, y1]) => this.coordinates.some(([x2, y2]) => x2 === x1 + 1 && y1 === y2)))) {
            this.movedHorizontally--;
            this.coordinates = this.coordinates.map(([x, y]) => [x - 1, y]);
            this.highestX--;
            this.lowestX--;
        }
    }

    public moveRight(chamber: number[][], shouldCheckRocks: boolean) {
        if (this.highestX !== CHAMBER_WIDTH
            && (!shouldCheckRocks || !chamber.some(([x1, y1]) => this.coordinates.some(([x2, y2]) => x2 === x1 - 1 && y1 === y2)))) {
            this.coordinates = this.coordinates.map(([x, y]) => [x + 1, y]);
            this.highestX++;
            this.lowestX++;
        }
    }

    public move(direction: number, chamber: number[][]) {
        if (this.highestX !== (direction === 1 ? CHAMBER_WIDTH : 1)
            && !chamber.some(([x1, y1]) => this.coordinates.some(([x2, y2]) => x2 === x1 - direction && y1 === y2))) {
            this.coordinates = this.coordinates.map(([x, y]) => [x + direction, y]);
            this.highestX = this.highestX + direction;
            this.lowestX = this.lowestX + direction;
        }
    }

    public canMoveDown(chamber: number[][]) {
        return this.lowestY !== 1 && !chamber.some(([x1, y1]) => this.coordinates.some(([x2, y2]) => x2 === x1 && y1 === y2 - 1));
    }

    public moveDown() {
        this.coordinates = this.coordinates.map(([x, y]) => [x, y - 1]);
        this.highestY--;
        this.lowestY--;
    }

    public getHighestY() {
        return this.highestY;
    }

    public getLowestY() {
        return this.lowestY;
    }
}
