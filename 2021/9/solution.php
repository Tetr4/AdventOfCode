<?php
// try it here: https://www.tutorialspoint.com/execute_php_online.php

function isLowPoint($map, $x, $y) {
    $value = $map[$x][$y]; 
    $top = $map[$x][$y-1] ?? 9;
    $left = $map[$x-1][$y] ?? 9;
    $right = $map[$x+1][$y] ?? 9;
    $bottom = $map[$x][$y+1] ?? 9;
    return $value < $top
        && $value < $left
        && $value < $right
        && $value < $bottom;
}

function basinSize($map, $x, $y, &$visited = []) {
    if (!isset($map[$x][$y])) return 0;
    if ($map[$x][$y] == 9) return 0;
    if (in_array("$x $y", $visited)) return 0;
    array_push($visited, "$x $y");
    return 1
        + basinSize($map, $x, $y-1, $visited)
        + basinSize($map, $x-1, $y, $visited)
        + basinSize($map, $x+1, $y, $visited)
        + basinSize($map, $x, $y+1, $visited);
}

$input = stream_get_contents(STDIN);
$lines = explode(PHP_EOL, trim($input));
$map = array_map(function ($line) { return str_split(trim($line)); }, $lines);
$width = count($map);
$height = count($map[0]);

$riskLevels = [];
$basinSizes = [];
for($x = 0; $x < $width; $x++) {
    for($y = 0; $y < $height; $y++) {
        if (isLowPoint($map, $x, $y)) {
            array_push($riskLevels, $map[$x][$y] + 1);
            array_push($basinSizes, basinSize($map, $x, $y));
        }
    }
}

$part1 = array_sum($riskLevels);
echo "part1: $part1\n";

rsort($basinSizes);
$top3 = array_slice($basinSizes, 0, 3);
$part2 = array_product($top3);
echo "part2: $part2\n";