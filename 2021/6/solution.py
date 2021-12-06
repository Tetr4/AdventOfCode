#!/usr/bin/python3

# Usage: ./solution.py sample.txt 80

import sys

file = open(sys.argv[1], "r")
generations = int(sys.argv[2])
input = [int(number) for number in file.readline().split(",")]

fishes = {cycle: 0 for cycle in range(9)}
for cycle in input:
    fishes[cycle] += 1

for _ in range(generations):
    first = fishes[0]
    for cycle in range(8):
        fishes[cycle] = fishes[cycle+1]
    fishes[8] = first
    fishes[6] += first

print(sum(fishes.values()))