
import java.io.File

data class Graph<T>(
    val vertices: Set<T>,
    val edges: Map<T, Set<T>>,
    val weights: Map<T, Int>,
) {
    fun dijkstra(start: T): Map<T, T?> {
        val solved = mutableSetOf<T>()
        val delta = vertices.map { it to Int.MAX_VALUE }.toMap().toMutableMap().also {
            it[start] = 0
        }
        val previous = vertices.map { it to null as T? }.toMap().toMutableMap()
        while (solved != vertices) {
            val current: T = delta
                .filter { !solved.contains(it.key) }
                .minByOrNull { it.value }!!
                .key
            edges.getValue(current).minus(solved).forEach { neighbor ->
                val newPath = delta.getValue(current) + weights.getValue(neighbor)
                if (newPath < delta.getValue(neighbor)) {
                    delta[neighbor] = newPath
                    previous[neighbor] = current
                }
            }
            solved.add(current)
        }
        return previous.toMap()
    }
}

data class Point(
    val x: Int,
    val y: Int,
    val weight: Int,
)

data class Maze(
    val points: Array<Array<Point>>,
    val size: Int,
) {
    companion object {
        fun fromLines(lines: List<String>): Maze {
            val size = lines.count()
            val points = Array(size) { x ->
                Array(size) { y ->
                    Point(
                        x = x,
                        y = y,
                        weight = Character.getNumericValue(lines[y][x]),
                    )
                }
            }
            return Maze(points, size)
        }
    }

    val start
        get() = points[0][0]

    val end
        get() = points[size-1][size-1]

    fun enlarge(times: Int) = Maze(
        size = size * times,
        points = Array(size * times) { x ->
            Array(size * times) { y ->
                val growth = (x / size) + (y / size)
                var weight = points[x % size][y % size].weight + growth
                weight = ((weight - 1) % 9) + 1
                Point(
                    x = x,
                    y = y,
                    weight = weight,
                )
            }
        },
    )


    fun findShortestPath(start: Point, end: Point): List<Point> {
        val tree = this.toGraph().dijkstra(start)
        fun pathTo(end: Point): List<Point> {
            if (tree[end] == null) return listOf(end)
            return pathTo(tree[end]!!) + listOf(end)
        }
        return pathTo(end)
    }

    private fun toGraph(): Graph<Point> {
        val vertices = this.points.flatten().toSet()
        val edges: Map<Point, Set<Point>> = vertices.map {
            val top = points.getOrNull(it.x)?.getOrNull(it.y-1)
            val left = points.getOrNull(it.x-1)?.getOrNull(it.y)
            val right = points.getOrNull(it.x)?.getOrNull(it.y+1)
            val bottom = points.getOrNull(it.x+1)?.getOrNull(it.y)
            Pair(it, listOf(top, left, right, bottom).filterNotNull().toSet())
        }.toMap()
        val weights: Map<Point, Int> = vertices.map { Pair(it, it.weight) }.toMap()
        return Graph(vertices, edges, weights)
    }
}

val lines = File(args[0]).readLines()
val maze = Maze.fromLines(lines)
val part1 = maze
    .findShortestPath(maze.start, maze.end)
    .sumOf { it.weight } - maze.start.weight
println("Part 1: $part1")

val enlarged = maze.enlarge(5)
val part2 = enlarged
    .findShortestPath(enlarged.start, enlarged.end)
    .sumOf { it.weight } - enlarged.start.weight
println("Part 2: $part2")