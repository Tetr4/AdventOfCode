import java.io.File

data class Square(val id: Int, val x: Int, val y: Int, val w: Int, val h: Int) {
    fun inches() = sequence {
        for (currentX in  x..(x+w-1)) {
            for (currentY in  y..(y+h-1)) {
                yield(Pair(currentX, currentY))
            }
        }
    }
    fun intactIn(board: Board) = inches()
        .all { (x, y) -> board.grid[x][y] == 1 }
    companion object {
        val regex = "#([0-9]+) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)".toRegex()
        fun parse(line: String) = regex.matchEntire(line)?.destructured?.let { 
            (id, x, y, w, h) -> Square(id.toInt(), x.toInt(), y.toInt(), w.toInt(), h.toInt()) 
        }
    }
}

data class Board(val size: Int) {
    val grid = Array (size) { IntArray(size) }
    operator fun plusAssign(square: Square) = square.inches()
        .forEach { (x, y) -> grid[x][y] += 1 }
    val overlaps: Int
        get() = grid.sumBy { it.count { it > 1 } }
}

// part 1
val board = Board(1000)
val squares = File(args[0])
    .readLines()
    .map { Square.parse(it) }
    .filterNotNull()
squares.forEach { board += it }
println("Part 1: ${board.overlaps}")

// part 2
val validSquare = squares.first { it.intactIn(board) }
println("Part 2: ${validSquare.id}")