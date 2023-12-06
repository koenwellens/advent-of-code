import {readFile} from '../common/readFile';

const example1 = readFile(5, 'example1');
const input1 = readFile(5, 'input1');

interface Range {
    source: number;
    range: number;
}

const basicSeedRanges = seeds => seeds.map(s => +s).map(source => ({source, range: 0}));
const actualSeedRanges = seeds => {
    const result = [];
    for (let i = 0; i < seeds.length / 2; i++) {
        result.push({source: +seeds[2 * i], range: +seeds[2 * i + 1]});
    }

    return result;
};

const toMapper = line => {
    const numbers = line.split(" ");
    const destination = +numbers[0];
    const source = +numbers[1];
    const range = +numbers[2];
    const difference = destination - source;
    const finalSource = source + range - 1;

    return {
        difference,
        next: (sr: Range) => {
            if (sr.source + sr.range < source || sr.source > finalSource) {
                return [];
            }

            const newSource = Math.max(sr.source, source);
            const newFinalSource = Math.min(finalSource, sr.source + sr.range);
            const newRange = Math.min(sr.range, newFinalSource - newSource);

            return [
                {source: newSource + difference, range: newRange, previousSource: newSource},
            ];
        },
    }
}

const mapInputToMappers = (text, startIndex) => {
    const result: { difference, next }[] = [];

    for (let lineIndex = startIndex; lineIndex < text.length; lineIndex++) {
        if (!text[lineIndex].trim()) {
            return result;
        }

        result.push(toMapper(text[lineIndex]));
    }

    return result;
}

const computeNextIndex = objectWithArrays =>
    Object.getOwnPropertyNames(objectWithArrays)
        .map(prop => objectWithArrays[prop].length)
        .reduce((res, v) => res + v + 2, 3)

const mapInput = (text, seedRangeMapper) => {
    const seedRanges = seedRangeMapper(text[0].split(": ")[1].split(" "));
    const result = {};

    result['seedToSoil'] = mapInputToMappers(text, computeNextIndex(result));
    result['soilToFertilizer'] = mapInputToMappers(text, computeNextIndex(result));
    result['fertilizerToWater'] = mapInputToMappers(text, computeNextIndex(result));
    result['waterToLight'] = mapInputToMappers(text, computeNextIndex(result));
    result['lightToTemperature'] = mapInputToMappers(text, computeNextIndex(result));
    result['temperatureToHumidity'] = mapInputToMappers(text, computeNextIndex(result));
    result['humidityToLocation'] = mapInputToMappers(text, computeNextIndex(result));

    result['seedRanges'] = seedRanges;
    return result;
}

const computeNextRanges = (ranges, rangeMappers) => {
    return ranges.reduce((res, sr) => {
        const mappers = rangeMappers.reduce((r, sts) => [...r, ...sts.next(sr)], []);
        if (!mappers.length) {
            return [...res, sr];
        }

        mappers.sort((l, r) => l.previousSource - r.previousSource);
        const extraMappings = [];
        let current = sr.source;
        let index = 0;
        while (index < mappers.length) {
            const next = mappers[index].previousSource;
            if (next - current > 0) {
                extraMappings.push({source: current, range: next - current});
            }
            current = next;
            index++;
        }
        const lastMap = mappers[index - 1];
        if (lastMap.previousSource + lastMap.range < sr.source + sr.range) {
            extraMappings.push({
                source: lastMap.previousSource + lastMap.range + 1,
                range: (sr.source + sr.range) - (lastMap.previousSource + lastMap.range + 1)
            });
        }

        return [...res, ...mappers, ...extraMappings];
    }, []);
}

const findShortestPath = (text, seedRangeMapper = basicSeedRanges) => {
    const {
        seedRanges,
        seedToSoil,
        soilToFertilizer,
        fertilizerToWater,
        waterToLight,
        lightToTemperature,
        temperatureToHumidity,
        humidityToLocation,
    } = mapInput(text, seedRangeMapper) as any;

    const soilRanges = computeNextRanges(seedRanges, seedToSoil);
    const fertilizerRanges = computeNextRanges(soilRanges, soilToFertilizer);
    const waterRanges = computeNextRanges(fertilizerRanges, fertilizerToWater);
    const lightRanges = computeNextRanges(waterRanges, waterToLight);
    const temperatureRanges = computeNextRanges(lightRanges, lightToTemperature);
    const humidityRanges = computeNextRanges(temperatureRanges, temperatureToHumidity);
    const locationRanges = computeNextRanges(humidityRanges, humidityToLocation);

    locationRanges.sort((l, r) => l.source - r.source);

    return locationRanges[0].source;
}

console.log(findShortestPath(example1)); // 35
console.log(findShortestPath(input1)); // 51580674

const example2 = readFile(5, 'example2');
const input2 = readFile(5, 'input2');

console.log(findShortestPath(example2, actualSeedRanges)); // 46
console.log(findShortestPath(input2, actualSeedRanges)); // 99751240