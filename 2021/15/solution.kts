// To prevent OutOfMemoryError: export JAVA_OPTS="-Xmx8g"
// kotlinc -script solution.kts sample.txt
import java.io.File
import kotlin.system.measureTimeMillis
import java.util.PriorityQueue

data class Graph<V>(
    val vertices: Set<V>,
    val edges: Map<V, Set<V>>,
    val weights: Map<V, Int>,
) {
    fun findShortestPath(start: V, end: V): List<V> {
        val tree = dijkstra(start)
        fun pathTo(end: V): List<V> {
            if (tree[end] == null) return listOf(end)
            return pathTo(tree[end]!!) + listOf(end)
        }
        return pathTo(end)
    }

    private fun dijkstra(start: V): Map<V, V?> {
        val distances: MutableMap<V, Int> = vertices.associateWith { Int.MAX_VALUE }.toMutableMap().also {
            it[start] = 0
        }
        val unvisited = PriorityQueue<V> {a, b -> distances[a]!!.compareTo(distances[b]!!)}.also {
            it.addAll(vertices)
        }
        val previous: MutableMap<V, V?> = vertices.associateWith { null }.toMutableMap()
        while (unvisited.isNotEmpty()) {
            val current: V = unvisited.poll()!!
            printProgress(unvisited.size, vertices.size)
            edges[current]!!.forEach { neighbor ->
                val newPath = distances[current]!! + weights[neighbor]!!
                if (newPath < distances[neighbor]!!) {
                    distances[neighbor] = newPath
                    unvisited.remove(neighbor)
                    unvisited.add(neighbor)
                    previous[neighbor] = current
                }
            }
        }
        clearTerminal()
        return previous
    }

    private fun printProgress(remaining: Int, total: Int) {
        val progress = "%.2f".format((total - remaining) * 100f / total)
        print("\rProgress: $progress%")
    }

    private fun clearTerminal() = print("\r${" ".repeat(20)}\r")
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

    fun findShortestPath(start: Point, end: Point): List<Point> =
        this.toGraph().findShortestPath(start, end)

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
