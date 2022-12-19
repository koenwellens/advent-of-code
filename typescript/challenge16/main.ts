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
            via: [valve],
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

const computeNextRoute = (valves, valve, openedValves) => {
    let bestValve = valve.tunnels[0];
    let bestDistance = valve.shortestPaths[bestValve].distance;
    let bestFlowRate = valve.shortestPaths[bestValve].flowRate;
    for (let tunnel of Object.getOwnPropertyNames(valve.shortestPaths)) {
        const path = valve.shortestPaths[tunnel];
        if (tunnel !== valve.name && path.flowRate !== 0 && openedValves.indexOf(tunnel) < 0) {
            if (bestDistance === -1) {
                bestDistance = path.distance;
                bestFlowRate = path.flowRate;
                bestValve = tunnel;
            } else {
                if ((path.distance > bestDistance && path.flowRate > bestFlowRate * (path.distance - bestDistance + 1))
                    || (path.distance <= bestDistance && path.flowRate * (bestDistance - path.distance + 1) > bestFlowRate)) {
                    bestFlowRate = path.flowRate;
                    bestDistance = path.distance;
                    bestValve = tunnel;
                }
            }
        }
    }

    return valve.shortestPaths[bestValve].via;
}

const runRunRun = (valves, startValve) => {
    const openValves = [];
    let pressure = 0;
    let currentTunnel = valves[startValve];
    const visitedValves = [currentTunnel];
    for (let time = 1; time <= 30; time++) {
        console.log(`\n== Minute ${time} ==`);
        pressure += openValves.map(v => valves[v].flowRate).reduce((res, flowRate) => res + flowRate, 0);

        if (!openValves.length) {
            console.log('No valves are open.');
        } else {
            console.log(`Valves ${openValves.join(', ')} are open. Releasing ${openValves.map(v => valves[v].flowRate).reduce((res, flowRate) => res + flowRate, 0)} pressure.`);
        }

        let {name, flowRate} = currentTunnel;
        if (flowRate !== 0 && openValves.indexOf(name) < 0) {
            console.log(`You open valve ${currentTunnel.name}`);
            openValves.push(currentTunnel.name);
        } else {
            const route = computeNextRoute(valves, currentTunnel, openValves);
            currentTunnel = valves[route[route.length - 1]];
            time += route.length - 1;
            for (let i = 0; i < route.length - 1; i++) {
                pressure += openValves.map(v => valves[v].flowRate).reduce((res, flowRate) => res + flowRate, 0);
            }
            route.forEach(r => visitedValves.push(r));
            console.log(`You move to valve ${currentTunnel.name} after following route ${JSON.stringify(route)}`);
        }
    }

    return pressure;
};

const exampleValves = parse(example);
const examplePressure = runRunRun(exampleValves, 'AA');
console.log('Example 1:', examplePressure);

const inputValves = parse(input);
const inputPressure = runRunRun(inputValves, 'AA');
// 975 is too low
// Niet 1110, 1126, 1578, 1454
console.log('Input 1:', inputPressure);

const jurgenInputValves = parse(jurgenInput);
const jurgenInputPressure = runRunRun(jurgenInputValves, 'AA');
// 2183
console.log('Jurgen input 1:', jurgenInputPressure);
