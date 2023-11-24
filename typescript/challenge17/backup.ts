

const watchTheRocksFallBackup = (jet, numberOfExpectedRocks) => {
    const jetStreamLength = jet.length;
    let currentHeight = 0;
    let shouldFallDown = false;
    let nextShapeIdx = 1;
    let nextJetId = 0;
    let currentShape = SHAPES[0](START_FROM_LEFT, START_HEIGHT);
    let chamber = [];
    let shapeSizes = [];

    while (nextShapeIdx <= numberOfExpectedRocks) {
        if (shouldFallDown) {
            if (currentShape.canMoveDown(chamber)) {
                currentShape.moveDown();
            } else {
                if (shapeSizes.length > CHAMBER_WIDTH) {
                    const [shapeSize, ...rest] = shapeSizes;
                    chamber = [...chamber.slice(shapeSize), ...currentShape.coordinates];
                    shapeSizes = [...rest, currentShape.numberOfCoordinates];
                } else {
                    chamber = [...chamber, ...currentShape.coordinates];
                    shapeSizes.push(currentShape.numberOfCoordinates);
                }

                const shapeY = currentShape.getHighestY();
                if (currentHeight < shapeY) {
                    currentHeight = shapeY;
                }
                currentShape = SHAPES[(nextShapeIdx % 5)](START_FROM_LEFT, currentHeight + START_HEIGHT);
                nextShapeIdx++;
            }
        } else {
            currentShape[MOVE_BY_JET[jet.charAt(nextJetId)]](chamber);
            nextJetId = (nextJetId + 1) % jetStreamLength;
        }

        shouldFallDown = !shouldFallDown;
    }

    return currentHeight;
}
