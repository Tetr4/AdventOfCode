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

function solution() {
    const input = fs.readFileSync(process.argv[2], 'utf8');
    const caves = readCaves(input);
    const start = caves.find((it) => it.name == 'start')!;
    const end = caves.find((it) => it.name == 'end')!;
    const paths = findPaths(start, end);
    console.log(paths.length);
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

function findPaths(
    start: Cave,
    end: Cave,
    visited: Cave[] = [start],
    path: Cave[] = [start]
): Cave[][] {
    let paths: Cave[][] = []
    const notVisited = start.connections.filter((it) => !visited.includes(it));
    notVisited.sort((a, b) => a.name.localeCompare(b.name));
    for (const cave of notVisited) {
        const subPath = [...path, cave];
        if (cave == end) {
            paths = [...paths, subPath];
        } else {
            const subVisited = cave.small ? [...visited, cave] : [...visited];
            const subPaths = findPaths(cave, end, subVisited, subPath)
            paths = [...paths, ...subPaths];
        }
    }
    return paths;
}

solution();