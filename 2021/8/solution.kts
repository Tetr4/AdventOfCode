import java.io.File

data class Entry(val input: List<String>, val output: List<String>) {
    companion object {
        fun fromLine(line: String): Entry {
            val (left, right) = line.split("|")
            return Entry(
                input = left.trim().split(" "),
                output = right.trim().split(" "),
            )
        }

        val codeSpace = "abcdefg".permute() // 5040 possible combinations

        fun String.permute(result: String = ""): List<String> = if (this.isEmpty()) {
            listOf(result)
        } else {
            this.flatMapIndexed { i, c -> removeRange(i, i + 1).permute(result + c) }
        }

        val segmentsToDigits = mapOf(
            "abcefg" to 0,
            "cf" to 1,
            "acdeg" to 2,
            "acdfg" to 3,
            "bcdf" to 4,
            "abdfg" to 5,
            "abdefg" to 6,
            "acf" to 7,
            "abcdefg" to 8,
            "abcdfg" to 9,
        )
    }

    fun getSolution(): Int {
        val code = codeSpace.first { this.isSolvedBy(it) }
        return output
            .map { code.decode(it) }
            .map { it.toDigit() }
            .joinToString("")
            .toInt()
    }

    private fun isSolvedBy(code: String) = (input + output)
            .map { code.decode(it) }
            .all { it.isValidDigit() }

    private fun String.decode(scrambled: String) : String {
        val codeBook = this.toCodeBook()
        return scrambled.map { codeBook[it] }.joinToString("")
    }
    private fun String.toCodeBook(): Map<Char, Char> = "abcdefg".mapIndexed { i, c -> c to this[i] }.toMap()
    private fun String.toDigit() = segmentsToDigits[this.normalize()]
    private fun String.isValidDigit() = this.toDigit() != null
    private fun String.normalize() = this.toList().sorted().joinToString("")
}

val solutions = File(args[0])
    .readLines()
    .map { Entry.fromLine(it) }
    .map { it.getSolution() }

val part1 = solutions
    .flatMap { it.toString().toList() }
    .count { it in setOf('1', '4', '7', '8') }
println("part1: $part1")

val part2 = solutions.sum()
println("part2: $part2")
