// npm install
// npm start sample1.txt

import * as fs from "fs";

class Cave {
    name: string;
    connections: Cave[] = [];

    public get small(): boolean {
        return this.name.toLocaleLowerCase() == this.name;
    }

    constructor(name: string) {
        this.name = name;
    }
}

function readCaves(input: string): Cave[] {
    const caves: Cave[] = [];
    const lines = input.split('\n');
    for (const line of lines) {
        const [leftName, rightName] = line.split("-");
        let left = caves.find((it) => it.name == leftName);
        if (!left) {
            left = new Cave(leftName)
            caves.push(left);
        }
        let right = caves.find((it) => it.name == rightName);
        if (!right) {
            right = new Cave(rightName)
            caves.push(right);
        }
        left.connections.push(right);
        right.connections.push(left);
    }
    return caves;
}

function solvePart1(caves: Cave[]): number {
    const start = caves.find((it) => it.name == 'start')!;
    const end = caves.find((it) => it.name == 'end')!;
    const paths = findPaths(start, end);
    return paths.length;
}

function solvePart2(caves: Cave[]): number {
    const start = caves.find((it) => it.name == 'start')!;
    const end = caves.find((it) => it.name == 'end')!;
    const smallCaves = caves.filter((it) => it.small && it != end && it != start);
    const paths = smallCaves
        .flatMap((doubleVisitCave) => findPaths(start, end, doubleVisitCave))
        .map((path) => path.map((it) => it.name).join())
    const distinct = new Set(paths);
    return distinct.size;
}

function findPaths(
    start: Cave,
    end: Cave,
    doubleVisitCave?: Cave,
    doubleVisited = false,
    visited: Cave[] = [start],
    path: Cave[] = [start]
): Cave[][] {
    let paths: Cave[][] = []
    const notVisited = start.connections.filter((it) => {
        return !visited.includes(it) || (it == doubleVisitCave && !doubleVisited)
    });
    for (const cave of notVisited) {
        const subPath = [...path, cave];
        if (cave == end) {
            paths = [...paths, subPath];
        } else {
            const subDoubleVisited = cave == doubleVisitCave ? visited.includes(cave) : doubleVisited;
            const subVisited = cave.small ? [...visited, cave] : [...visited];
            const subPaths = findPaths(cave, end, doubleVisitCave, subDoubleVisited, subVisited, subPath)
            paths = [...paths, ...subPaths];
        }
    }
    return paths;
}

const input = fs.readFileSync(process.argv[2], 'utf8');
const caves = readCaves(input);
console.log(`Part 1: ${solvePart1(caves)}`);
console.log(`Part 2: ${solvePart2(caves)}`);