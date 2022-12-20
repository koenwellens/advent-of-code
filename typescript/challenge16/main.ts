import {example} from './example-input';
import {input} from './input';
import {input as jurgenInput} from './jurgen-input';

const VALVE_STRING_LENGTH = 6;
const TUNNELS_STRING_LENGTH = 23;

const stringToObj = s => {
    const stringParts = s.split('; ');
    const valveAndFlowRate = stringParts[0].split('=');

    const valve = valveAndFlowRate[0].substring(VALVE_STRING_LENGTH, VALVE_STRING_LENGTH + 2);
    const flowRate = Number(valveAndFlowRate[1]);
    let tunnels;
    if (stringParts[1].indexOf('tunnels') < 0) {
        tunnels = [stringParts[1].substring(TUNNELS_STRING_LENGTH - 1)];
    } else {
        const tunnelsStr = stringParts[1].substring(TUNNELS_STRING_LENGTH);
        tunnels = tunnelsStr.split(', ');
    }

    return {name: valve, flowRate, tunnels};
}

const computeShortestPaths = (valves, nbOfValves, {name, flowRate, tunnels}) => {
    const shortestPaths = tunnels.reduce((res, valve) => ({
        ...res, [valve]: {
            distance: 1,
            flowRate: valves[valve].flowRate,
            via: [name, valve],
        }
    }), {[name]: {distance: 0, flowRate: flowRate}});

    while (Object.getOwnPropertyNames(shortestPaths).length < nbOfValves) {
        for (let valveName of Object.getOwnPropertyNames(shortestPaths)) {
            const currentValve = valves[valveName];
            for (let tunnel of currentValve.tunnels) {
                if (shortestPaths[tunnel] === undefined) {
                    const {flowRate: tunnelRate} = valves[tunnel];
                    shortestPaths[tunnel] = {
                        distance: shortestPaths[valveName].distance + 1,
                        flowRate: tunnelRate,
                        via: [...shortestPaths[valveName].via, tunnel],
                    };
                }
            }
        }
    }

    return shortestPaths;
}

const parse = str => {
    const parsedStr = str.split('\n')
        .map(s => stringToObj(s))
        .reduce((res, valve) => ({...res, [valve.name]: valve,}), {});

    const valveNames = Object.getOwnPropertyNames(parsedStr);
    for (let valveName of Object.getOwnPropertyNames(parsedStr)) {
        const valve = parsedStr[valveName];
        parsedStr[valveName] = {...valve, shortestPaths: computeShortestPaths(parsedStr, valveNames.length, valve)};
    }

    return parsedStr;
}

const pressureForRoutes = (valves, routes, maxTime) => {
    const openedValves = [];
    let pressure = 0;

    let offsets = routes.map(() => 0);
    let previousValves = routes.map(() => '');
    for (let time = 0; time < maxTime; time++) {
        for(let r = 0; r < routes.length; r++) {
            const route = routes[r];
            let offset = offsets[r];
            const previousValve = previousValves[r];
            if (time + offset < route.length) {
                let valve = route[time + offset];
                if (valve === previousValve && openedValves.indexOf(valve) < 0) {
                    openedValves.push(valve);
                } else if (valve === previousValve) {
                    offset = offset + 1;
                    offsets[r] = offset;
                    valve = route[time + offset];
                }
                previousValves[r] = valve;
            }

            if (time + offset === route.length && openedValves.indexOf(route[route.length - 1]) < 0) {
                openedValves.push(route[route.length - 1]);
            }
        }

        pressure += openedValves.reduce((res, v) => res + valves[v].flowRate, 0);
    }

    return pressure;
};

const computePressure = (valves, routes, maxTime) => {
    let maximumPressure = -1;
    for (let route of routes) {
        const pressure = pressureForRoutes(valves, [route.reduce((res, r) => [...res, ...r], [])], maxTime);
        if (pressure > maximumPressure) {
            maximumPressure = pressure;
        }
    }
    return maximumPressure;
}

const computeRoutes = (valves, currentValve, previousRoutes, maxLength): string[][][] => {
    if (previousRoutes.reduce((res, r) => [...res, ...r], []).length < maxLength) {
        const routes: string[][][] = Object.getOwnPropertyNames(currentValve.shortestPaths)
            .map(name => currentValve.shortestPaths[name])
            .filter(({flowRate, distance}) => flowRate !== 0 && distance > 0)
            .reduce((res, {via}) => [...res, via], [])
            .filter(route => !previousRoutes.some(pr => pr[pr.length - 1] === route[route.length - 1]))
            .map(route => previousRoutes.length ? [...previousRoutes, route] : [route])
            .map(x => JSON.stringify(x))
            .filter((r, idx, arr) => arr.indexOf(r) === idx)
            .map(r => JSON.parse(r));

        if (routes.length) {
            let result: string[][][] = [];
            for (let route of routes) {
                const lastRoute = route[route.length - 1];
                const nextRoutes = computeRoutes(valves, valves[lastRoute[lastRoute.length - 1]], route, maxLength);
                result = [
                    ...result,
                    ...nextRoutes,
                ];
            }

            return result;
        }
    }

    return [previousRoutes];
}

const exampleValves = parse(example);
const exampleRoutes = computeRoutes(exampleValves, exampleValves['AA'], [], 30);
const examplePressure = computePressure(exampleValves, exampleRoutes, 30);
console.log('Example 1:', examplePressure);

const inputValves = parse(input);
const inputRoutes = computeRoutes(inputValves, inputValves['AA'], [], 30);
const inputPressure = computePressure(inputValves, inputRoutes, 30);
console.log('Input 1:', inputPressure);

const jurgenInputValves = parse(jurgenInput);
const jurgenRoutes = computeRoutes(jurgenInputValves, jurgenInputValves['AA'], [], 30);
const jurgenPressure = computePressure(jurgenInputValves, jurgenRoutes, 30);
console.log('Jurgen input 1:', jurgenPressure);

const startsSameAs = (arr1, arr2) => {
    return arr1[0][arr1[0].length - 1] === arr2[0][arr2[1].length - 1]
        || arr1[1][arr1[1].length - 1] === arr2[1][arr2[1].length - 1]
        || arr1[1][arr1[1].length - 1] === arr2[0][arr2[0].length - 1]
        || arr1[0][arr1[0].length - 1] === arr2[1][arr2[1].length - 1];
}

const computePressureForTwoRoutes = (valves, routes, maxTime) => {
    let maximumPressure = -1;
    for (let i = 0; i < routes.length; i++) {
        for (let j = i + 1; j < routes.length; j++) {
            if (!startsSameAs(routes[i], routes[j])) {
                const routeA = routes[i].reduce((res, r) => [...res, ...r], []);
                const routeB = routes[j].reduce((res, r) => [...res, ...r], []);
                const pressure = pressureForRoutes(valves, [routeA, routeB], maxTime);
                if (pressure > maximumPressure) {
                    maximumPressure = pressure;
                }
            }
        }
    }
    return maximumPressure;
}

const shorterExampleRoutes = computeRoutes(exampleValves, exampleValves['AA'], [], 12);
const examplePressureForTwo = computePressureForTwoRoutes(exampleValves, shorterExampleRoutes, 26);
console.log('Example 2:', examplePressureForTwo);

const shorterInputRoutes = computeRoutes(inputValves, inputValves['AA'], [], 18);
const inputPressureForTwo = computePressureForTwoRoutes(inputValves, shorterInputRoutes, 26);
console.log('Input 2:', inputPressureForTwo);

const shorterJurgenRoutes = computeRoutes(jurgenInputValves, jurgenInputValves['AA'], [], 18);
const jurgenPressureForTwo = computePressureForTwoRoutes(jurgenInputValves, shorterJurgenRoutes, 26);
console.log('Jurgen Input 2:', jurgenPressureForTwo);
