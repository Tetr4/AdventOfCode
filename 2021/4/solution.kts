import java.io.File

val parser = Parser(File(args[0]))
val numbers = parser.parseNumbers();
var boards = parser.parseBoards();

var solutions = emptyList<Int>()
for (number in numbers) {
    boards.forEach { it.mark(number) }
    val winners = boards.filter { it.isSolved }
    winners.forEach { solutions += number * it.sumOfUnmarked }
    boards -= winners
}
println("part1: ${solutions.first()}")
println("part2: ${solutions.last()}")

class Parser(val file: File) {
    fun parseNumbers(): List<Int> = file
        .readLines()
        .first()
        .split(',')
        .map(String::toInt)

    fun parseBoards(): List<Board> = file
        .readLines()
        .drop(1) // instructions
        .filter { it.isNotEmpty() } // blank lines
        .chunked(Board.BOARD_SIZE)
        .map(::parseBoard)

    private fun parseBoard(chunk: List<String>) = Board(chunk.map(::parseRow))

    private fun parseRow(row: String): List<Field> = row
        .trim()
        .split(Regex("\\s+")) // whitespace
        .map(String::toInt)
        .map { Field(it) }
}

class Board(val rows: List<List<Field>>) {
    companion object {
        val BOARD_SIZE = 5;
    }

    val columns: List<List<Field>>
        get() = (0 until BOARD_SIZE).map { columnIndex ->
            rows.map { it[columnIndex] }
        }

    val isSolved
        get() = rows.any { it.isSolved } || columns.any { it.isSolved }

    val sumOfUnmarked
        get() = rows
            .flatten()
            .filter { !it.marked }
            .sumOf { it.number }

    fun mark(number: Int) {
        val field = rows.flatten().find { it.number == number }
        field?.marked = true
    }

    val List<Field>.isSolved
        get() = this.all { it.marked }
}

class Field(
    val number: Int,
    var marked: Boolean = false,
)
